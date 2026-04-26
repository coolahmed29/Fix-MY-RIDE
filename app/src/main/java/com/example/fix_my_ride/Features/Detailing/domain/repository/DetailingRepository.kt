package com.example.fix_my_ride.Features.Detailing.domain.repository

// Features/Detailing/domain/repository/DetailingRepository.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.domain.model.DetailingPackage
import kotlinx.coroutines.flow.Flow

interface DetailingRepository {
    fun getPackages(): Flow<AuthResult<List<DetailingPackage>>>
    suspend fun getPackageDetail(packageId: String): AuthResult<DetailingPackage>
}