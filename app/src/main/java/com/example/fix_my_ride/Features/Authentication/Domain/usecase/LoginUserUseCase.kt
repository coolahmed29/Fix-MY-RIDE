package com.example.fix_my_ride.Features.Authentication.Domain.usecase


import android.util.Patterns
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Authentication.Domain.model.UserModel
import com.example.fix_my_ride.Features.Authentication.Domain.repository.AuthRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email    : String,
        password : String
    ): AuthResult<UserModel> {

        // ── Validation ────────────────────────────────
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
        return repository.loginUser(email, password)
    }
}