package com.paykids.data.di

import com.kakao.sdk.user.UserApiClient
import com.paykids.domain.repository.SignInRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun providesUserApiClient(): UserApiClient = UserApiClient.instance
}