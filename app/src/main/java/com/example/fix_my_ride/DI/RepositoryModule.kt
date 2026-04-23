package com.example.fix_my_ride.DI

// di/RepositoryModule.kt

import com.example.fix_my_ride.Features.Authentication.data.repository.AuthRepositoryImpl
import com.example.fix_my_ride.Features.Authentication.Domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}