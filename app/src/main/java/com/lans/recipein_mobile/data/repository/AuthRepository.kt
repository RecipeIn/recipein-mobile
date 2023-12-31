package com.lans.recipein_mobile.data.repository

import com.lans.recipein_mobile.common.Resource
import com.lans.recipein_mobile.data.source.network.SafeApiCall
import com.lans.recipein_mobile.data.source.network.api.RecipeInApi
import com.lans.recipein_mobile.data.source.network.dto.SignInRequestDto
import com.lans.recipein_mobile.data.source.network.dto.SignUpRequestDto
import com.lans.recipein_mobile.data.source.network.dto.toDomain
import com.lans.recipein_mobile.domain.model.Auth
import com.lans.recipein_mobile.domain.model.Token
import com.lans.recipein_mobile.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: RecipeInApi,
) : IAuthRepository, SafeApiCall {
    override suspend fun signin(auth: Auth): Flow<Resource<Token>> {
        return flow {
            emit(Resource.Loading)
            emit(
                safeCall {
                    api.signin(
                        SignInRequestDto(
                            email = auth.email,
                            password = auth.password
                        )
                    ).toDomain()
                }
            )
        }
    }

    override suspend fun signup(auth: Auth): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading)
            emit(
                safeCall {
                    api.signup(
                        SignUpRequestDto(
                            email = auth.email,
                            username = auth.username,
                            password = auth.password
                        )
                    ).status == 201
                }
            )
        }
    }
}