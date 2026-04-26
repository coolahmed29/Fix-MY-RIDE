package com.example.fix_my_ride.Features.Detailing.domain.usecase

// Features/Detailing/domain/usecase/UploadShowcaseUseCase.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.domain.repository.ShowcaseRepository
import javax.inject.Inject

class UploadShowcaseUseCase @Inject constructor(
    private val repository: ShowcaseRepository
) {
    suspend operator fun invoke(
        title       : String,
        description : String,
        imageUrls   : List<String>,
        offerText   : String
    ): AuthResult<Unit> {
        if (title.isBlank())
            return AuthResult.Error("Title zaroori hai")
        if (imageUrls.isEmpty())
            return AuthResult.Error("Kam az kam ek image zaroori hai")
        return repository.uploadShowcase(
            title       = title,
            description = description,
            imageUrls   = imageUrls,
            offerText   = offerText
        )
    }
}