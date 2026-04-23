package com.example.fix_my_ride.Features.Authentication.Domain.usecase

// features/auth/domain/usecase/RegisterUserUseCase.kt

import android.util.Patterns
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Authentication.Domain.model.UserModel
import com.example.fix_my_ride.Features.Authentication.Domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email    : String,
        password : String,
        name     : String,
        phone    : String
    ): AuthResult<UserModel> {

        // ── Validation ────────────────────────────────
        if (name.isBlank())
            return AuthResult.Error("Naam zaroori hai")

        if (name.length < 2)
            return AuthResult.Error("Naam bahut chhota hai")

        if (phone.isBlank())
            return AuthResult.Error("Phone number zaroori hai")

        if (phone.length < 11)
            return AuthResult.Error("Phone 11 digits ka hona chahiye")

        if (!phone.startsWith("03"))
            return AuthResult.Error("Phone 03 se shuru hona chahiye")

        if (email.isBlank())
            return AuthResult.Error("Email zaroori hai")

        if (!Patterns.EMAIL_ADDRESS
                .matcher(email).matches())
            return AuthResult.Error("Email sahi format mein nahi")

        if (password.isBlank())
            return AuthResult.Error("Password zaroori hai")

        if (password.length < 6)
            return AuthResult.Error("Password 6 characters se kam nahi hona chahiye")

        // ── Validation pass — Repository ko do ────────
        return repository.registerUser(email, password, name, phone)
    }
}