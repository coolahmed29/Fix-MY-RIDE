package com.example.fix_my_ride.Features.Detailing.data.source

// Features/Detailing/data/source/DetailingFirebaseSource.kt
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.data.model.DetailingPackageDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailingFirebaseSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val PACKAGES = "detailing_packages"
    }

    // ── Get All Packages — Realtime ───────────────────
    fun getPackages(): Flow<AuthResult<List<DetailingPackageDto>>> =
        callbackFlow {
            trySend(AuthResult.Loading)

            val listener = firestore
                .collection(PACKAGES)
                .whereEqualTo("is_available", true)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(AuthResult.Error(
                            error.message ?: "Packages load nahi hue"
                        ))
                        return@addSnapshotListener
                    }

                    val packages = snapshot?.documents
                        ?.mapNotNull {
                            it.toObject(DetailingPackageDto::class.java)
                        }
                        ?: emptyList()

                    trySend(AuthResult.Success(packages))
                }

            awaitClose { listener.remove() }
        }

    // ── Get Package Detail ────────────────────────────
    suspend fun getPackageDetail(
        packageId: String
    ): AuthResult<DetailingPackageDto> {
        return try {
            val doc = firestore
                .collection(PACKAGES)
                .document(packageId)
                .get()
                .await()

            val pkg = doc.toObject(DetailingPackageDto::class.java)
                ?: return AuthResult.Error("Package nahi mila")

            AuthResult.Success(pkg)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Package detail load nahi hua")
        }
    }
}