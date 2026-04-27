package com.example.fix_my_ride.utils

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DetailingSeedData {

    private val db = FirebaseFirestore.getInstance()

    // ── Detailing Packages ────────────────────────────
    private val packages = listOf(
        mapOf(
            "id"                to "pkg_001",
            "name"              to "Basic Exterior Wash",
            "description"       to "Complete exterior hand wash, wheel cleaning, window cleaning and air dry.",
            "price"             to 800.0,
            "type"              to "EXTERIOR",
            "rating"            to 4.2f,
            "duration_hours"    to 1,              // ✅ duration_hours
            "included_services" to listOf(         // ✅ included_services
                "Hand wash",
                "Wheel cleaning",
                "Window cleaning",
                "Air dry"
            ),
            "workshop_name"     to "Ali's Auto Spa", // ✅ workshop_name
            "workshop_id"       to "workshop_001",   // ✅ workshop_id
            "thumbnail_url"     to "",              // ✅ thumbnail_url
            "before_after_urls" to listOf<String>(), // ✅ before_after_urls
            "is_available"      to true,            // ✅ is_available
            "created_at"        to System.currentTimeMillis()
        ),
        mapOf(
            "id"                to "pkg_002",
            "name"              to "Premium Interior Detailing",
            "description"       to "Full interior vacuum, dashboard cleaning, seat shampooing, door panel wipe, AC vent cleaning and air freshener.",
            "price"             to 2500.0,
            "type"              to "INTERIOR",
            "rating"            to 4.7f,
            "duration_hours"    to 2,
            "included_services" to listOf(
                "Vacuum",
                "Dashboard clean",
                "Seat shampoo",
                "Door panels",
                "AC vents",
                "Air freshener"
            ),
            "workshop_name"     to "Ali's Auto Spa",
            "workshop_id"       to "workshop_001",
            "thumbnail_url"     to "",
            "before_after_urls" to listOf<String>(),
            "is_available"      to true,
            "created_at"        to System.currentTimeMillis()
        ),
        mapOf(
            "id"                to "pkg_003",
            "name"              to "Full Car Detailing",
            "description"       to "Our most complete package. Exterior wash, clay bar treatment, wax polish, full interior detailing, engine bay cleaning and tyre dressing.",
            "price"             to 6500.0,
            "type"              to "FULL",         // ✅ FULL — PackageType.FULL
            "rating"            to 4.9f,
            "duration_hours"    to 4,
            "included_services" to listOf(
                "Exterior wash",
                "Clay bar",
                "Wax polish",
                "Interior detailing",
                "Engine bay",
                "Tyre dressing"
            ),
            "workshop_name"     to "Ali's Auto Spa",
            "workshop_id"       to "workshop_001",
            "thumbnail_url"     to "",
            "before_after_urls" to listOf<String>(),
            "is_available"      to true,
            "created_at"        to System.currentTimeMillis()
        ),
        mapOf(
            "id"                to "pkg_004",
            "name"              to "Paint Protection",
            "description"       to "Professional paint decontamination, ceramic coating application and UV protection.",
            "price"             to 12000.0,
            "type"              to "EXTERIOR",
            "rating"            to 4.8f,
            "duration_hours"    to 6,
            "included_services" to listOf(
                "Paint decontamination",
                "Ceramic coating",
                "UV protection",
                "Paint sealant"
            ),
            "workshop_name"     to "Premium Auto Care",
            "workshop_id"       to "workshop_002",
            "thumbnail_url"     to "",
            "before_after_urls" to listOf<String>(),
            "is_available"      to true,
            "created_at"        to System.currentTimeMillis()
        ),
        mapOf(
            "id"                to "pkg_005",
            "name"              to "Leather Interior Care",
            "description"       to "Specialized leather cleaning, conditioning and protection treatment.",
            "price"             to 3500.0,
            "type"              to "INTERIOR",
            "rating"            to 4.5f,
            "duration_hours"    to 2,
            "included_services" to listOf(
                "Leather cleaning",
                "Conditioning",
                "Stain removal",
                "Protection coat"
            ),
            "workshop_name"     to "Premium Auto Care",
            "workshop_id"       to "workshop_002",
            "thumbnail_url"     to "",
            "before_after_urls" to listOf<String>(),
            "is_available"      to true,
            "created_at"        to System.currentTimeMillis()
        ),
        mapOf(
            "id"                to "pkg_006",
            "name"              to "Engine Bay Cleaning",
            "description"       to "Full engine bay degreasing, steam cleaning and dressing.",
            "price"             to 1800.0,
            "type"              to "EXTERIOR",
            "rating"            to 4.3f,
            "duration_hours"    to 1,
            "included_services" to listOf(
                "Degreasing",
                "Steam clean",
                "Engine dressing",
                "Leak inspection"
            ),
            "workshop_name"     to "Premium Auto Care",
            "workshop_id"       to "workshop_002",
            "thumbnail_url"     to "",
            "before_after_urls" to listOf<String>(),
            "is_available"      to true,
            "created_at"        to System.currentTimeMillis()
        )
    )
    // ── Time Slots ────────────────────────────────────
    private fun generateSlots(): List<Map<String, Any>> {
        val slots = mutableListOf<Map<String, Any>>()
        val workshopIds = listOf("workshop_001", "workshop_002")
        val times = listOf(
            "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
            "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM"
        )

        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())

        // Agle 7 din ke slots banao
        val calendar = Calendar.getInstance()
        repeat(7) { dayOffset ->
            calendar.add(Calendar.DAY_OF_YEAR, if (dayOffset == 0) 0 else 1)
            val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(calendar.time)

            workshopIds.forEach { workshopId ->
                times.forEach { time ->
                    val dateTime = sdf.parse("$dateStr $time")?.time
                        ?: System.currentTimeMillis()
                    slots.add(
                        mapOf(
                            "id" to "slot_${workshopId}_${dateStr}_${
                                time.replace(":", "").replace(" ", "")
                            }",
                            "workshopId" to workshopId,
                            "dateTime" to dateTime,
                            "time" to time,
                            "isAvailable" to true
                        )
                    )
                }
            }
        }
        return slots
    }

    // ── Seed Function ─────────────────────────────────
    suspend fun seedAll(): Result<Unit> {
        return try {
            // Check karo pehle data hai ya nahi
            val existing = db.collection("detailing_packages")
                .limit(1)
                .get()
                .await()

            if (!existing.isEmpty) {
                println("✅ Detailing data already exists — skipping seed")
                return Result.success(Unit)
            }

            // Packages seed karo
            val packagesBatch = db.batch()
            packages.forEach { pkg ->
                val ref = db.collection("detailing_packages")
                    .document(pkg["id"] as String)
                packagesBatch.set(ref, pkg)
            }
            packagesBatch.commit().await()
            println("✅ Packages added: ${packages.size}")

            // Time slots seed karo
            val slots = generateSlots()
            val slotsBatch = db.batch()
            slots.forEach { slot ->
                val ref = db.collection("time_slots")
                    .document(slot["id"] as String)
                slotsBatch.set(ref, slot)
            }
            slotsBatch.commit().await()
            println("✅ Time slots added: ${slots.size}")

            Result.success(Unit)

        } catch (e: Exception) {
            println("❌ Detailing seed failed: ${e.message}")
            Result.failure(e)
        }
    }


    suspend fun clearAndReseed(): Result<Unit> {
        return try {
            // Old data delete karo
            val old = db.collection("detailing_packages").get().await()
            val deleteBatch = db.batch()
            old.documents.forEach { deleteBatch.delete(it.reference) }
            deleteBatch.commit().await()
            println("✅ Old detailing data cleared")

            // Force seedAll
            val packagesBatch = db.batch()
            packages.forEach { pkg ->
                val ref = db.collection("detailing_packages")
                    .document(pkg["id"] as String)
                packagesBatch.set(ref, pkg)
            }
            packagesBatch.commit().await()
            println("✅ Fresh detailing data seeded!")

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



}