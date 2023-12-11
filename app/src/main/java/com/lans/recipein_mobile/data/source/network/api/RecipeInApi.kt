package com.lans.recipein_mobile.data.source.network.api

import com.lans.recipein_mobile.data.source.network.dto.SignInRequestDto
import com.lans.recipein_mobile.data.source.network.dto.SignInResponseDto
import com.lans.recipein_mobile.data.source.network.dto.SignUpRequestDto
import com.lans.recipein_mobile.data.source.network.dto.SignUpResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface RecipeInApi {
    @POST("/api/login")
    suspend fun signin(
        @Body requestBody: SignInRequestDto,
    ): SignInResponseDto

    @POST("/api/signup")
    suspend fun signup(
        @Body requestBody: SignUpRequestDto,
    ): SignUpResponseDto
}