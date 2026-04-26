package com.example.fix_my_ride.Features.Detailing.domain.usecase

// Features/Detailing/domain/usecase/GetPackagesUseCase.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.domain.model.DetailingPackage
import com.example.fix_my_ride.Features.Detailing.domain.repository.DetailingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPackagesUseCase @Inject constructor(
    private val repository: DetailingRepository
) {
    operator fun invoke(): Flow<AuthResult<List<DetailingPackage>>> =
        repository.getPackages()
}