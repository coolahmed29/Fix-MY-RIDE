package com.example.fix_my_ride.utils


import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object MechanicSeedData {

    private val db = FirebaseFirestore.getInstance()

    // ── Mechanic Data ────────────────────────────────
    private val mechanics = listOf(
        mapOf(
            "id" to "mech_001",
            "name" to "Ali's Auto Repair",
            "phone" to "03001234567",
            "email" to "ali@autorepair.com",
            "rating" to 4.8,
            "experience" to 8,
            "latitude" to 24.8657,  // ✅ Karachi coordinates
            "longitude" to 67.0061,
            "is_available" to true,
            "specializations" to listOf("Oil Change", "Tire Repair", "Engine Diagnostic"),
            "average_response_time" to 15,
            "completed_jobs" to 342,
            "profile_image_url" to "",
            "workshop_name" to "Ali's Workshop",
            "workshop_address" to "Gulshan-e-Iqbal, Karachi",
            "hourly_rate" to 500.0,
            "reviews" to listOf(
                mapOf(
                    "id" to "rev_001",
                    "user_id" to "user_123",
                    "user_name" to "Ahmed Khan",
                    "rating" to 5.0,
                    "comment" to "Excellent service! Very professional.",
                    "timestamp" to System.currentTimeMillis() - 86400000
                ),
                mapOf(
                    "id" to "rev_002",
                    "user_id" to "user_124",
                    "user_name" to "Sara Ahmed",
                    "rating" to 4.5,
                    "comment" to "Good work, reasonable prices.",
                    "timestamp" to System.currentTimeMillis() - 172800000
                )
            ),
            "created_at" to System.currentTimeMillis()
        ),
        mapOf(
            "id" to "mech_002",
            "name" to "Hassan's Garage",
            "phone" to "03002345678",
            "email" to "hassan@garage.com",
            "rating" to 4.6,
            "experience" to 6,
            "latitude" to 24.8550,
            "longitude" to 67.0110,
            "is_available" to true,
            "specializations" to listOf("Brake Service", "Tire Replacement", "AC Service"),
            "average_response_time" to 20,
            "completed_jobs" to 215,
            "profile_image_url" to "",
            "workshop_name" to "Hassan's Garage",
            "workshop_address" to "Defence, Karachi",
            "hourly_rate" to 450.0,
            "reviews" to listOf(
                mapOf(
                    "id" to "rev_003",
                    "user_id" to "user_125",
                    "user_name" to "Fatima Ali",
                    "rating" to 4.5,
                    "comment" to "Quick and efficient service.",
                    "timestamp" to System.currentTimeMillis() - 259200000
                )
            ),
            "created_at" to System.currentTimeMillis()
        ),
        mapOf(
            "id" to "mech_003",
            "name" to "Kareem's Expert Motors",
            "phone" to "03003456789",
            "email" to "kareem@expertmotors.com",
            "rating" to 4.9,
            "experience" to 12,
            "latitude" to 24.8609,
            "longitude" to 67.0005,
            "is_available" to false,
            "specializations" to listOf("Engine Overhaul", "Transmission Service", "Suspension Repair"),
            "average_response_time" to 10,
            "completed_jobs" to 512,
            "profile_image_url" to "",
            "workshop_name" to "Kareem's Expert Motors",
            "workshop_address" to "Saddar, Karachi",
            "hourly_rate" to 600.0,
            "reviews" to listOf(
                mapOf(
                    "id" to "rev_004",
                    "user_id" to "user_126",
                    "user_name" to "Usman Malik",
                    "rating" to 5.0,
                    "comment" to "Best mechanic in town! Highly recommended.",
                    "timestamp" to System.currentTimeMillis() - 345600000
                ),
                mapOf(
                    "id" to "rev_005",
                    "user_id" to "user_127",
                    "user_name" to "Zainab Khan",
                    "rating" to 4.8,
                    "comment" to "Expert work, fair pricing.",
                    "timestamp" to System.currentTimeMillis() - 432000000
                )
            ),
            "created_at" to System.currentTimeMillis()
        ),
        mapOf(
            "id" to "mech_004",
            "name" to "Farooq's Quick Fix",
            "phone" to "03004567890",
            "email" to "farooq@quickfix.com",
            "rating" to 4.3,
            "experience" to 4,
            "latitude" to 24.8700,
            "longitude" to 67.0008,
            "is_available" to true,
            "specializations" to listOf("General Maintenance", "Battery Service", "Spark Plugs"),
            "average_response_time" to 25,
            "completed_jobs" to 98,
            "profile_image_url" to "",
            "workshop_name" to "Farooq's Quick Fix",
            "workshop_address" to "Clifton, Karachi",
            "hourly_rate" to 350.0,
            "reviews" to listOf(
                mapOf(
                    "id" to "rev_006",
                    "user_id" to "user_128",
                    "user_name" to "Muhammad Ali",
                    "rating" to 4.0,
                    "comment" to "Good service, average prices.",
                    "timestamp" to System.currentTimeMillis() - 518400000
                )
            ),
            "created_at" to System.currentTimeMillis()
        )
    )

    // ── Seed Function ─────────────────────────────────
    suspend fun seedAll(): Result<Unit> {
        return try {
            // Check کریں پہلے data ہے یا نہیں
            val existing = db.collection("mechanics")
                .limit(1)
                .get()
                .await()

            if (!existing.isEmpty) {
                println("✅ Mechanic data already exists — skipping seed")
                return Result.success(Unit)
            }

            // Mechanics seed کریں
            val mechanicsBatch = db.batch()
            mechanics.forEach { mechanic ->
                val ref = db.collection("mechanics")
                    .document(mechanic["id"] as String)
                mechanicsBatch.set(ref, mechanic)
            }
            mechanicsBatch.commit().await()
            println("✅ Mechanics added: ${mechanics.size}")

            Result.success(Unit)

        } catch (e: Exception) {
            println("❌ Mechanic seed failed: ${e.message}")
            Result.failure(e)
        }
    }

    // ── Clear and Reseed ───────────────────────────────
    suspend fun clearAndReseed(): Result<Unit> {
        return try {
            // پرانا data delete کریں
            val old = db.collection("mechanics").get().await()
            val deleteBatch = db.batch()
            old.documents.forEach { deleteBatch.delete(it.reference) }
            deleteBatch.commit().await()
            println("✅ Old mechanic data cleared")

            // نیا data seed کریں
            val mechanicsBatch = db.batch()
            mechanics.forEach { mechanic ->
                val ref = db.collection("mechanics")
                    .document(mechanic["id"] as String)
                mechanicsBatch.set(ref, mechanic)
            }
            mechanicsBatch.commit().await()
            println("✅ Fresh mechanic data seeded!")

            Result.success(Unit)
        } catch (e: Exception) {
            println("❌ Reseed failed: ${e.message}")
            Result.failure(e)
        }
    }
}