package com.example.fix_my_ride.Features.SpareParts.Data.repository

// Features/SpareParts/Data/repository/SparePartsRepositoryImpl.kt

import com.example.fix_my_ride.Features.SpareParts.Data.source.SparePartsFirebaseSource
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Part
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Vendor
import com.example.fix_my_ride.Features.SpareParts.Domain.repository.SparePartsRepository
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SparePartsRepositoryImpl @Inject constructor(
    private val source: SparePartsFirebaseSource
) : SparePartsRepository {

    override fun searchParts(
        query    : String,
        category : String,
        token    : String
    ): Flow<AuthResult<List<Part>>> =
        source.searchParts(query, category, token).map { result ->
            when (result) {
                is AuthResult.Success ->
                    AuthResult.Success(result.data.map { it.toDomain() })
                is AuthResult.Error   -> AuthResult.Error(result.message)
                AuthResult.Loading    -> AuthResult.Loading
            }
        }

    override suspend fun getVendorsForPart(
        partId : String,
        token  : String
    ): AuthResult<List<Vendor>> {
        val result = source.getVendorsForPart(partId, token)
        return when (result) {
            is AuthResult.Success ->
                AuthResult.Success(result.data.map { it.toDomain() })
            is AuthResult.Error   -> AuthResult.Error(result.message)
            AuthResult.Loading    -> AuthResult.Loading
        }
    }

    override suspend fun getCategories(
        token: String
    ): AuthResult<List<String>> = source.getCategories(token)
}