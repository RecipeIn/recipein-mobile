package com.lans.recipein_mobile.di

import android.content.Context
import androidx.room.Room
import com.lans.recipein_mobile.common.Constants.BASE_URL
import com.lans.recipein_mobile.data.interactor.CheckSessionInteractor
import com.lans.recipein_mobile.data.interactor.GetCategoriesInteractor
import com.lans.recipein_mobile.data.interactor.GetCategoryByIdInteractor
import com.lans.recipein_mobile.data.interactor.GetFavoriteRecipeInteractor
import com.lans.recipein_mobile.data.interactor.GetProfileInteractor
import com.lans.recipein_mobile.data.interactor.GetRecipeByCategoryIdInteractor
import com.lans.recipein_mobile.data.interactor.GetRecipeByIdInteractor
import com.lans.recipein_mobile.data.interactor.GetRecipeIngredientsInteractor
import com.lans.recipein_mobile.data.interactor.GetRecipeInstructionsInteractor
import com.lans.recipein_mobile.data.interactor.GetRecipeNutritionsInteractor
import com.lans.recipein_mobile.data.interactor.GetRecipesInteractor
import com.lans.recipein_mobile.data.interactor.GetUserNutritionInteractor
import com.lans.recipein_mobile.data.interactor.InsertFavoriteRecipeInteractor
import com.lans.recipein_mobile.data.interactor.InsertUserNutritionInteractor
import com.lans.recipein_mobile.data.interactor.SaveSessionInteractor
import com.lans.recipein_mobile.data.interactor.SignInInteractor
import com.lans.recipein_mobile.data.interactor.SignOutInteractor
import com.lans.recipein_mobile.data.interactor.SignUpInteractor
import com.lans.recipein_mobile.data.interactor.UpdateProfileInteractor
import com.lans.recipein_mobile.data.interactor.UpdateUserNutritionInteractor
import com.lans.recipein_mobile.data.interactor.validator.ValidateConfirmPasswordInteractor
import com.lans.recipein_mobile.data.interactor.validator.ValidateEmailInteractor
import com.lans.recipein_mobile.data.interactor.validator.ValidatePasswordInteractor
import com.lans.recipein_mobile.data.interactor.validator.ValidateUsernameInteractor
import com.lans.recipein_mobile.data.interactor.validator.ValidatorInteractor
import com.lans.recipein_mobile.data.repository.AuthRepository
import com.lans.recipein_mobile.data.repository.CategoryRepository
import com.lans.recipein_mobile.data.repository.FavoriteRepository
import com.lans.recipein_mobile.data.repository.RecipeRepository
import com.lans.recipein_mobile.data.repository.UserRepository
import com.lans.recipein_mobile.data.source.local.AppDatabase
import com.lans.recipein_mobile.data.source.local.DataStoreManager
import com.lans.recipein_mobile.data.source.local.dao.FavoriteDao
import com.lans.recipein_mobile.data.source.network.AuthAuthenticator
import com.lans.recipein_mobile.data.source.network.AuthInterceptor
import com.lans.recipein_mobile.data.source.network.api.RecipeInApi
import com.lans.recipein_mobile.domain.repository.IAuthRepository
import com.lans.recipein_mobile.domain.repository.ICategoryRepository
import com.lans.recipein_mobile.domain.repository.IFavoriteRepository
import com.lans.recipein_mobile.domain.repository.IRecipeRepository
import com.lans.recipein_mobile.domain.repository.IUserRepository
import com.lans.recipein_mobile.domain.usecase.CheckSessionUseCase
import com.lans.recipein_mobile.domain.usecase.GetCategoriesUseCase
import com.lans.recipein_mobile.domain.usecase.GetCategoryByIdUseCase
import com.lans.recipein_mobile.domain.usecase.GetFavoriteRecipeUseCase
import com.lans.recipein_mobile.domain.usecase.GetProfileUseCase
import com.lans.recipein_mobile.domain.usecase.GetRecipeByCategoryIdUseCase
import com.lans.recipein_mobile.domain.usecase.GetRecipeByIdUseCase
import com.lans.recipein_mobile.domain.usecase.GetRecipeIngredientsUseCase
import com.lans.recipein_mobile.domain.usecase.GetRecipeInstructionsUseCase
import com.lans.recipein_mobile.domain.usecase.GetRecipeNutritionsUseCase
import com.lans.recipein_mobile.domain.usecase.GetRecipesUseCase
import com.lans.recipein_mobile.domain.usecase.GetUserNutritionUseCase
import com.lans.recipein_mobile.domain.usecase.InsertFavoriteRecipeUseCase
import com.lans.recipein_mobile.domain.usecase.InsertUserNutritionUseCase
import com.lans.recipein_mobile.domain.usecase.SaveSessionUseCase
import com.lans.recipein_mobile.domain.usecase.SignInUseCase
import com.lans.recipein_mobile.domain.usecase.SignOutUseCase
import com.lans.recipein_mobile.domain.usecase.SignUpUseCase
import com.lans.recipein_mobile.domain.usecase.UpdateProfileUseCase
import com.lans.recipein_mobile.domain.usecase.UpdateUserNutritionUseCase
import com.lans.recipein_mobile.domain.usecase.validator.ValidateConfirmPasswordUseCase
import com.lans.recipein_mobile.domain.usecase.validator.ValidateEmailUseCase
import com.lans.recipein_mobile.domain.usecase.validator.ValidatePasswordUseCase
import com.lans.recipein_mobile.domain.usecase.validator.ValidateUsernameUseCase
import com.lans.recipein_mobile.domain.usecase.validator.ValidatorUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofitClient(dataStoreManager: DataStoreManager): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .authenticator(AuthAuthenticator(dataStoreManager))
            .addInterceptor(AuthInterceptor(dataStoreManager))
            .build()
    }

    @Provides
    @Singleton
    fun provideRecipeInApi(client: OkHttpClient): RecipeInApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "recipeinDB"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(appDatabase: AppDatabase): FavoriteDao {
        return appDatabase.favoriteDao
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: RecipeInApi): IAuthRepository {
        return AuthRepository(api)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        api: RecipeInApi,
        dataStoreManager: DataStoreManager,
    ): IUserRepository {
        return UserRepository(api, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(api: RecipeInApi): ICategoryRepository {
        return CategoryRepository(api)
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(api: RecipeInApi): IRecipeRepository {
        return RecipeRepository(api)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(favoriteDao: FavoriteDao): IFavoriteRepository {
        return FavoriteRepository(favoriteDao)
    }

    @Provides
    @Singleton
    fun provideSignInUseCase(authRepository: IAuthRepository): SignInUseCase {
        return SignInInteractor(authRepository)
    }

    @Provides
    @Singleton
    fun provideSignUpUseCase(authRepository: IAuthRepository): SignUpUseCase {
        return SignUpInteractor(authRepository)
    }

    @Provides
    @Singleton
    fun provideCheckSessionUseCase(userRepository: IUserRepository): CheckSessionUseCase {
        return CheckSessionInteractor(userRepository)
    }

    @Provides
    @Singleton
    fun provideSaveSessionUseCase(userRepository: IUserRepository): SaveSessionUseCase {
        return SaveSessionInteractor(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetProfile(userRepository: IUserRepository): GetProfileUseCase {
        return GetProfileInteractor(userRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateProfile(userRepository: IUserRepository): UpdateProfileUseCase {
        return UpdateProfileInteractor(userRepository)
    }

    @Provides
    @Singleton
    fun getUserNutrition(userRepository: IUserRepository): GetUserNutritionUseCase {
        return GetUserNutritionInteractor(userRepository)
    }

    @Provides
    @Singleton
    fun insertUserNutrition(userRepository: IUserRepository): InsertUserNutritionUseCase {
        return InsertUserNutritionInteractor(userRepository)
    }

    @Provides
    @Singleton
    fun updateUserNutrition(userRepository: IUserRepository): UpdateUserNutritionUseCase {
        return UpdateUserNutritionInteractor(userRepository)
    }


    @Provides
    @Singleton
    fun provideSignOutUseCase(userRepository: IUserRepository): SignOutUseCase {
        return SignOutInteractor(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(categoryRepository: ICategoryRepository): GetCategoriesUseCase {
        return GetCategoriesInteractor(categoryRepository)
    }

    @Provides
    @Singleton
    fun provideGetCategoryByIdUseCase(categoryRepository: ICategoryRepository): GetCategoryByIdUseCase {
        return GetCategoryByIdInteractor(categoryRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecipesUseCase(
        recipeRepository: IRecipeRepository,
    ): GetRecipesUseCase {
        return GetRecipesInteractor(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecipeByIdUseCase(
        recipeRepository: IRecipeRepository,
    ): GetRecipeByIdUseCase {
        return GetRecipeByIdInteractor(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecipeByCategoryIdUseCase(recipeRepository: IRecipeRepository): GetRecipeByCategoryIdUseCase {
        return GetRecipeByCategoryIdInteractor(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecipeIngredientsUseCase(
        recipeRepository: IRecipeRepository,
    ): GetRecipeIngredientsUseCase {
        return GetRecipeIngredientsInteractor(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecipeInstructionsUseCase(
        recipeRepository: IRecipeRepository,
    ): GetRecipeInstructionsUseCase {
        return GetRecipeInstructionsInteractor(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecipeNutritionsUseCase(
        recipeRepository: IRecipeRepository,
    ): GetRecipeNutritionsUseCase {
        return GetRecipeNutritionsInteractor(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideInsertFavoriteRecipeUseCase(
        favoriteRepository: IFavoriteRepository,
    ): InsertFavoriteRecipeUseCase {
        return InsertFavoriteRecipeInteractor(favoriteRepository)
    }

    @Provides
    @Singleton
    fun provideGetFavoriteRecipeUseCase(
        favoriteRepository: IFavoriteRepository,
    ): GetFavoriteRecipeUseCase {
        return GetFavoriteRecipeInteractor(favoriteRepository)
    }

    @Provides
    @Singleton
    fun provideValidatorUseCase(
        validateEmailUseCase: ValidateEmailUseCase,
        validateUsernameUseCase: ValidateUsernameUseCase,
        validatePasswordUseCase: ValidatePasswordUseCase,
        validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase,
    ): ValidatorUseCase {
        return ValidatorInteractor(
            validateEmailUseCase,
            validateUsernameUseCase,
            validatePasswordUseCase,
            validateConfirmPasswordUseCase
        )
    }

    @Provides
    @Singleton
    fun provideValidateEmailUseCase(): ValidateEmailUseCase {
        return ValidateEmailInteractor()
    }

    @Provides
    @Singleton
    fun provideValidateUsernameUseCase(): ValidateUsernameUseCase {
        return ValidateUsernameInteractor()
    }

    @Provides
    @Singleton
    fun provideValidatePasswordUseCase(): ValidatePasswordUseCase {
        return ValidatePasswordInteractor()
    }

    @Provides
    @Singleton
    fun provideValidateConfirmPasswordUseCase(): ValidateConfirmPasswordUseCase {
        return ValidateConfirmPasswordInteractor()
    }
}