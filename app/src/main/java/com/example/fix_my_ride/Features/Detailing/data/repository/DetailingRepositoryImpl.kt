package com.example.fix_my_ride.Features.Detailing.data.repository

// Features/Detailing/data/repository/DetailingRepositoryImpl.kt
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.data.source.DetailingFirebaseSource
import com.example.fix_my_ride.Features.Detailing.domain.model.DetailingPackage
import com.example.fix_my_ride.Features.Detailing.domain.repository.DetailingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailingRepositoryImpl @Inject constructor(
    private val source: DetailingFirebaseSource
) : DetailingRepository {

    override fun getPackages(): Flow<AuthResult<List<DetailingPackage>>> =
        source.getPackages().map { result ->
            when (result) {
                is AuthResult.Success ->
                    AuthResult.Success(result.data.map { it.toDomain() })
                is AuthResult.Error   -> AuthResult.Error(result.message)
                AuthResult.Loading    -> AuthResult.Loading
            }
        }

    override suspend fun getPackageDetail(
        packageId: String
    ): AuthResult<DetailingPackage> {
        val result = source.getPackageDetail(packageId)
        return when (result) {
            is AuthResult.Success ->
                AuthResult.Success(result.data.toDomain())
            is AuthResult.Error   -> AuthResult.Error(result.message)
            AuthResult.Loading    -> AuthResult.Loading
        }
    }
}