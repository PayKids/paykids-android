package com.paykids.data.di

import com.paykids.data.repository.KakaoAuthRepositoryImpl
import com.paykids.data.repository.SignInRepositoryImpl
import com.paykids.domain.repository.KakaoAuthRepository
import com.paykids.domain.repository.SignInRepository
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
    abstract fun bindsSignInRepository(impl: SignInRepositoryImpl): SignInRepository

    @Binds
    @Singleton
    abstract fun bindsKakaoAuthRepository(impl: KakaoAuthRepositoryImpl): KakaoAuthRepository
}