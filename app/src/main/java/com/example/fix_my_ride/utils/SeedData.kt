// utils/SeedData.kt
package com.example.fix_my_ride.utils

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object SeedData {

    private val db = FirebaseFirestore.getInstance()

    // ── Parts ─────────────────────────────────────────
    private val parts = listOf(
        mapOf(
            "id"           to "part_001",
            "name"         to "Car Battery",
            "category"     to "Battery",
            "price"        to 8500.0,
            "image_url"    to "",
            "rating"       to 4.5,
            "description"  to "12V heavy duty car battery",
            "car_model"    to "Toyota Corolla",
            "vendor_count" to 3,
            "created_at"   to System.currentTimeMillis()
        ),
        mapOf(
            "id"           to "part_002",
            "name"         to "Engine Oil Filter",
            "category"     to "Filters",
            "price"        to 1200.0,
            "image_url"    to "",
            "rating"       to 4.2,
            "description"  to "OEM quality oil filter",
            "car_model"    to "Honda Civic",
            "vendor_count" to 5,
            "created_at"   to System.currentTimeMillis()
        ),
        mapOf(
            "id"           to "part_003",
            "name"         to "Front Brake Pads",
            "category"     to "Brakes",
            "price"        to 3500.0,
            "image_url"    to "",
            "rating"       to 4.7,
            "description"  to "High performance ceramic brake pads",
            "car_model"    to "Suzuki Alto",
            "vendor_count" to 2,
            "created_at"   to System.currentTimeMillis()
        ),
        mapOf(
            "id"           to "part_004",
            "name"         to "Tubeless Tyre",
            "category"     to "Tires",
            "price"        to 12000.0,
            "image_url"    to "",
            "rating"       to 4.3,
            "description"  to "185/65 R15 all season tyre",
            "car_model"    to "Toyota Corolla",
            "vendor_count" to 4,
            "created_at"   to System.currentTimeMillis()
        ),
        mapOf(
            "id"           to "part_005",
            "name"         to "Headlight Bulb",
            "category"     to "Lights",
            "price"        to 800.0,
            "image_url"    to "",
            "rating"       to 4.0,
            "description"  to "H4 halogen bulb 60/55W",
            "car_model"    to "Honda CD 70",
            "vendor_count" to 6,
            "created_at"   to System.currentTimeMillis()
        ),
        mapOf(
            "id"           to "part_006",
            "name"         to "Air Filter",
            "category"     to "Filters",
            "price"        to 950.0,
            "image_url"    to "",
            "rating"       to 4.1,
            "description"  to "High flow air filter",
            "car_model"    to "Suzuki Mehran",
            "vendor_count" to 3,
            "created_at"   to System.currentTimeMillis()
        ),
        mapOf(
            "id"           to "part_007",
            "name"         to "Alternator Belt",
            "category"     to "Engine",
            "price"        to 1800.0,
            "image_url"    to "",
            "rating"       to 4.4,
            "description"  to "OEM grade alternator belt",
            "car_model"    to "Honda Civic",
            "vendor_count" to 2,
            "created_at"   to System.currentTimeMillis()
        ),
        mapOf(
            "id"           to "part_008",
            "name"         to "Shock Absorber",
            "category"     to "Engine",
            "price"        to 5500.0,
            "image_url"    to "",
            "rating"       to 4.6,
            "description"  to "Front shock absorber",
            "car_model"    to "Toyota Vitz",
            "vendor_count" to 3,
            "created_at"   to System.currentTimeMillis()
        )
    )

    // ── Vendors ───────────────────────────────────────
    private val vendors = listOf(
        // Battery vendors
        mapOf(
            "id"        to "vendor_001",
            "part_id"   to "part_001",
            "name"      to "Ali Auto Parts",
            "price"     to 8200.0,
            "rating"    to 4.5,
            "distance"  to 2.3,
            "in_stock"  to true,
            "stock_qty" to 5,
            "phone"     to "03001234567",
            "address"   to "Gulberg III, Lahore",
            "latitude"  to 31.5204,
            "longitude" to 74.3587
        ),
        mapOf(
            "id"        to "vendor_002",
            "part_id"   to "part_001",
            "name"      to "Raza Motors",
            "price"     to 8700.0,
            "rating"    to 4.2,
            "distance"  to 4.1,
            "in_stock"  to true,
            "stock_qty" to 2,
            "phone"     to "03111234567",
            "address"   to "DHA Phase 5, Lahore",
            "latitude"  to 31.4697,
            "longitude" to 74.4071
        ),
        mapOf(
            "id"        to "vendor_003",
            "part_id"   to "part_001",
            "name"      to "Khan Spare Parts",
            "price"     to 9000.0,
            "rating"    to 3.8,
            "distance"  to 6.5,
            "in_stock"  to false,
            "stock_qty" to 0,
            "phone"     to "03211234567",
            "address"   to "Model Town, Lahore",
            "latitude"  to 31.4820,
            "longitude" to 74.3290
        ),
        // Oil Filter vendors
        mapOf(
            "id"        to "vendor_004",
            "part_id"   to "part_002",
            "name"      to "Ahmed Auto Store",
            "price"     to 1100.0,
            "rating"    to 4.6,
            "distance"  to 1.8,
            "in_stock"  to true,
            "stock_qty" to 10,
            "phone"     to "03331234567",
            "address"   to "Johar Town, Lahore",
            "latitude"  to 31.4698,
            "longitude" to 74.2718
        ),
        mapOf(
            "id"        to "vendor_005",
            "part_id"   to "part_002",
            "name"      to "Bilal Parts Center",
            "price"     to 1250.0,
            "rating"    to 4.0,
            "distance"  to 3.2,
            "in_stock"  to true,
            "stock_qty" to 7,
            "phone"     to "03451234567",
            "address"   to "Faisal Town, Lahore",
            "latitude"  to 31.5025,
            "longitude" to 74.3201
        ),
        // Brake Pads vendors
        mapOf(
            "id"        to "vendor_006",
            "part_id"   to "part_003",
            "name"      to "Master Auto Works",
            "price"     to 3300.0,
            "rating"    to 4.8,
            "distance"  to 2.0,
            "in_stock"  to true,
            "stock_qty" to 4,
            "phone"     to "03001112233",
            "address"   to "Wapda Town, Lahore",
            "latitude"  to 31.4412,
            "longitude" to 74.2698
        ),
        mapOf(
            "id"        to "vendor_007",
            "part_id"   to "part_003",
            "name"      to "Speedy Parts",
            "price"     to 3700.0,
            "rating"    to 4.3,
            "distance"  to 5.5,
            "in_stock"  to false,
            "stock_qty" to 0,
            "phone"     to "03002223344",
            "address"   to "Cantt, Lahore",
            "latitude"  to 31.5497,
            "longitude" to 74.3823
        ),
        // Tyre vendors
        mapOf(
            "id"        to "vendor_008",
            "part_id"   to "part_004",
            "name"      to "Tyre World",
            "price"     to 11500.0,
            "rating"    to 4.4,
            "distance"  to 3.0,
            "in_stock"  to true,
            "stock_qty" to 8,
            "phone"     to "03003334455",
            "address"   to "Liberty Market, Lahore",
            "latitude"  to 31.5214,
            "longitude" to 74.3531
        ),
        mapOf(
            "id"        to "vendor_009",
            "part_id"   to "part_004",
            "name"      to "Pakistan Tyres",
            "price"     to 12500.0,
            "rating"    to 4.1,
            "distance"  to 7.2,
            "in_stock"  to true,
            "stock_qty" to 3,
            "phone"     to "03004445566",
            "address"   to "Multan Road, Lahore",
            "latitude"  to 31.4756,
            "longitude" to 74.2812
        )
    )

    // ── Seed Function — Suspend ───────────────────────
    suspend fun seedAll(): Result<Unit> {
        return try {
            // Check karo pehle data hai ya nahi
            val existing = db.collection("parts")
                .limit(1)
                .get()
                .await()

            // ✅ Already seeded hai toh skip karo
            if (!existing.isEmpty) {
                println("✅ Data already exists — skipping seed")
                return Result.success(Unit)
            }

            // Parts seed karo
            val partsBatch = db.batch()
            parts.forEach { part ->
                val ref = db.collection("parts")
                    .document(part["id"] as String)
                partsBatch.set(ref, part)
            }
            partsBatch.commit().await()
            println("✅ Parts added: ${parts.size}")

            // Vendors seed karo
            val vendorsBatch = db.batch()
            vendors.forEach { vendor ->
                val ref = db.collection("vendors")
                    .document(vendor["id"] as String)
                vendorsBatch.set(ref, vendor)
            }
            vendorsBatch.commit().await()
            println("✅ Vendors added: ${vendors.size}")

            Result.success(Unit)

        } catch (e: Exception) {
            println("❌ Seed failed: ${e.message}")
            Result.failure(e)
        }
    }
}