package com.example.fix_my_ride.DI

// di/RepositoryModule.kt

import com.example.fix_my_ride.Features.Authentication.data.repository.AuthRepositoryImpl
import com.example.fix_my_ride.Features.Authentication.Domain.repository.AuthRepository
import com.example.fix_my_ride.Features.Chat.data.repository.ChatRepositoryImpl
import com.example.fix_my_ride.Features.Chat.domain.repository.ChatRepository
import com.example.fix_my_ride.Features.Detailing.data.repository.BookingRepositoryImpl
import com.example.fix_my_ride.Features.Detailing.data.repository.DetailingRepositoryImpl
import com.example.fix_my_ride.Features.Detailing.data.repository.ReviewRepositoryImpl
import com.example.fix_my_ride.Features.Detailing.data.repository.ShowcaseRepositoryImpl
import com.example.fix_my_ride.Features.Detailing.domain.repository.BookingRepository
import com.example.fix_my_ride.Features.Detailing.domain.repository.DetailingRepository
import com.example.fix_my_ride.Features.Detailing.domain.repository.ReviewRepository
import com.example.fix_my_ride.Features.Detailing.domain.repository.ShowcaseRepository
import com.example.fix_my_ride.Features.SpareParts.Data.repository.SparePartsRepositoryImpl
import com.example.fix_my_ride.Features.SpareParts.Domain.repository.SparePartsRepository
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


    @Binds
    @Singleton
    abstract fun bindSparePartsRepository(
        impl: SparePartsRepositoryImpl
    ): SparePartsRepository



    // DI/RepositoryModule.kt mein add karo
    @Binds @Singleton
    abstract fun bindDetailingRepository(
        impl: DetailingRepositoryImpl
    ): DetailingRepository

    @Binds @Singleton
    abstract fun bindBookingRepository(
        impl: BookingRepositoryImpl
    ): BookingRepository

    @Binds @Singleton
    abstract fun bindReviewRepository(
        impl: ReviewRepositoryImpl
    ): ReviewRepository

    @Binds @Singleton
    abstract fun bindShowcaseRepository(
        impl: ShowcaseRepositoryImpl
    ): ShowcaseRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository





}