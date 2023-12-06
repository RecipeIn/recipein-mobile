package com.lans.recipein_mobile.data.source.network.dto

import retrofit2.http.Query

data class SignInRequestDto(
    @Query("email") val email: String,
    @Query("password") val password: String
)