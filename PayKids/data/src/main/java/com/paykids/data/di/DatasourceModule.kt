package com.paykids.data.di

import com.paykids.data.datasource.AuthRemoteDatasource
import com.paykids.data.datasource.ChatRemoteDatasource
import com.paykids.data.datasourceImpl.AuthRemoteDatasourceImpl
import com.paykids.data.datasourceImpl.ChatRemoteDatasourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DatasourceModule {

    @Binds
    abstract fun bindChatRemoteDatasource(
        impl: ChatRemoteDatasourceImpl
    ): ChatRemoteDatasource

    @Binds
    abstract fun bindAuthRemoteDatasource(
        impl: AuthRemoteDatasourceImpl
    ): AuthRemoteDatasource
}