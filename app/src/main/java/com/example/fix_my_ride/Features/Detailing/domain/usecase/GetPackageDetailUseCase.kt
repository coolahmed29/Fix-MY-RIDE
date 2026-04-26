package com.example.fix_my_ride.Features.Detailing.domain.usecase

// Features/Detailing/domain/usecase/GetPackageDetailUseCase.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.domain.model.DetailingPackage
import com.example.fix_my_ride.Features.Detailing.domain.repository.DetailingRepository
import javax.inject.Inject

class GetPackageDetailUseCase @Inject constructor(
    private val repository: DetailingRepository
) {
    suspend operator fun invoke(
        packageId: String
    ): AuthResult<DetailingPackage> =
        repository.getPackageDetail(packageId)
}