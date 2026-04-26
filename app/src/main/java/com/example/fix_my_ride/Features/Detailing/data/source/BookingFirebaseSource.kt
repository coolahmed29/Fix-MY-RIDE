package com.example.fix_my_ride.Features.Detailing.data.source

// Features/Detailing/data/source/BookingFirebaseSource.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.data.model.BookingDto
import com.example.fix_my_ride.Features.Detailing.data.model.TimeSlotDto
import com.example.fix_my_ride.Features.Detailing.domain.model.BookingStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingFirebaseSource @Inject constructor(
    private val firestore    : FirebaseFirestore,
    private val firebaseAuth : FirebaseAuth
) {
    companion object {
        private const val BOOKINGS   = "bookings"
        private const val TIME_SLOTS = "time_slots"
    }

    private val currentUserId get() =
        firebaseAuth.currentUser?.uid ?: ""

    // ── Get Available Slots ───────────────────────────
    suspend fun getAvailableSlots(
        packageId : String,
        date      : String
    ): AuthResult<List<TimeSlotDto>> {
        return try {
            val snapshot = firestore
                .collection(TIME_SLOTS)
                .whereEqualTo("package_id", packageId)
                .whereEqualTo("date", date)
                .get()
                .await()

            // Agar Firestore mein slots nahi hain
            // toh default slots generate karo
            val slots = if (snapshot.isEmpty) {
                generateDefaultSlots(packageId, date)
            } else {
                snapshot.documents
                    .mapNotNull { it.toObject(TimeSlotDto::class.java) }
            }

            AuthResult.Success(slots)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Slots load nahi hue")
        }
    }

    // ── Default Slots Generate karo ───────────────────
    private fun generateDefaultSlots(
        packageId : String,
        date      : String
    ): List<TimeSlotDto> {
        val times = listOf(
            "09:00 AM", "10:00 AM", "11:00 AM",
            "12:00 PM", "01:00 PM", "02:00 PM",
            "03:00 PM", "04:00 PM", "05:00 PM"
        )
        return times.mapIndexed { index, time ->
            TimeSlotDto(
                id          = "${packageId}_${date}_$index",
                time        = time,
                isAvailable = true,
                date        = date,
                packageId   = packageId
            )
        }
    }

    // ── Create Booking ────────────────────────────────
    suspend fun createBooking(
        packageId    : String,
        packageName  : String,
        price        : Double,
        date         : String,
        timeSlot     : String,
        workshopName : String,
        workshopId   : String
    ): AuthResult<BookingDto> {
        return try {
            val docRef = firestore.collection(BOOKINGS).document()

            val bookingDto = BookingDto(
                id           = docRef.id,
                userId       = currentUserId,
                packageId    = packageId,
                packageName  = packageName,
                workshopName = workshopName,
                workshopId   = workshopId,
                date         = date,
                timeSlot     = timeSlot,
                price        = price,
                status       = BookingStatus.PENDING.name,
                createdAt    = System.currentTimeMillis()
            )

            docRef.set(bookingDto).await()

            // Slot ko unavailable mark karo
            markSlotUnavailable(packageId, date, timeSlot)

            AuthResult.Success(bookingDto)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Booking create nahi hui")
        }
    }

    // ── Mark Slot Unavailable ─────────────────────────
    private suspend fun markSlotUnavailable(
        packageId : String,
        date      : String,
        timeSlot  : String
    ) {
        try {
            val slots = firestore
                .collection(TIME_SLOTS)
                .whereEqualTo("package_id", packageId)
                .whereEqualTo("date", date)
                .whereEqualTo("time", timeSlot)
                .get()
                .await()

            slots.documents.firstOrNull()?.reference
                ?.update("is_available", false)
                ?.await()
        } catch (e: Exception) {
            // Non-critical — ignore karo
        }
    }

    // ── Get Bookings — Realtime ───────────────────────
    fun getBookings(
        status: BookingStatus
    ): Flow<AuthResult<List<BookingDto>>> = callbackFlow {
        trySend(AuthResult.Loading)

        val listener = firestore
            .collection(BOOKINGS)
            .whereEqualTo("user_id", currentUserId)
            .whereEqualTo("status", status.name)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(AuthResult.Error(
                        error.message ?: "Bookings load nahi hue"
                    ))
                    return@addSnapshotListener
                }

                val bookings = snapshot?.documents
                    ?.mapNotNull { it.toObject(BookingDto::class.java) }
                    ?: emptyList()

                trySend(AuthResult.Success(bookings))
            }

        awaitClose { listener.remove() }
    }

    // ── Update Booking Status ─────────────────────────
    suspend fun updateBookingStatus(
        bookingId : String,
        status    : BookingStatus
    ): AuthResult<Unit> {
        return try {
            firestore
                .collection(BOOKINGS)
                .document(bookingId)
                .update("status", status.name)
                .await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Status update nahi hua")
        }
    }
}