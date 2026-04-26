package com.example.fix_my_ride.Features.Detailing.data.repository

// Features/Detailing/data/repository/ShowcaseRepositoryImpl.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.data.source.ShowcaseFirebaseSource
import com.example.fix_my_ride.Features.Detailing.domain.model.Showcase
import com.example.fix_my_ride.Features.Detailing.domain.repository.ShowcaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowcaseRepositoryImpl @Inject constructor(
    private val source: ShowcaseFirebaseSource
) : ShowcaseRepository {

    override suspend fun uploadShowcase(
        title       : String,
        description : String,
        imageUrls   : List<String>,
        offerText   : String
    ): AuthResult<Unit> =
        source.uploadShowcase(title, description, imageUrls, offerText)

    override fun getShowcases(): Flow<AuthResult<List<Showcase>>> =
        source.getShowcases().map { result ->
            when (result) {
                is AuthResult.Success ->
                    AuthResult.Success(result.data.map { it.toDomain() })
                is AuthResult.Error   -> AuthResult.Error(result.message)
                AuthResult.Loading    -> AuthResult.Loading
            }
        }
}