package com.paykids.data.di

import com.paykids.data.datasource.AuthRemoteDatasource
import com.paykids.data.datasource.ChatRemoteDatasource
import com.paykids.data.datasource.ExpenseCategoryRemoteDatasource
import com.paykids.data.datasource.ExpenseRemoteDatasource
import com.paykids.data.datasource.IncomeCategoryRemoteDatasource
import com.paykids.data.datasource.IncomeRemoteDatasource
import com.paykids.data.datasource.UserRemoteDatasource
import com.paykids.data.datasourceImpl.AuthRemoteDatasourceImpl
import com.paykids.data.datasourceImpl.ChatRemoteDatasourceImpl
import com.paykids.data.datasourceImpl.ExpenseCategoryRemoteDatasourceImpl
import com.paykids.data.datasourceImpl.ExpenseRemoteDatasourceImpl
import com.paykids.data.datasourceImpl.IncomeCategoryRemoteDatasourceImpl
import com.paykids.data.datasourceImpl.IncomeRemoteDatasourceImpl
import com.paykids.data.datasourceImpl.UserRemoteDatasourceImpl
import com.paykids.domain.repository.ExpenseCategoryRepository
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

    @Binds
    abstract fun bindUserRemoteDatasource(
        impl: UserRemoteDatasourceImpl
    ): UserRemoteDatasource

    @Binds
    abstract fun bindExpenseRemoteDatasource(
        impl: ExpenseRemoteDatasourceImpl
    ): ExpenseRemoteDatasource

    @Binds
    abstract fun bindIncomeRemoteDatasource(
        impl: IncomeRemoteDatasourceImpl
    ): IncomeRemoteDatasource

    @Binds
    abstract fun bindExpenseCategoryRemoteDatasource(
        impl: ExpenseCategoryRemoteDatasourceImpl
    ): ExpenseCategoryRemoteDatasource

    @Binds
    abstract fun bindIncomeCategoryRemoteDatasource(
        impl: IncomeCategoryRemoteDatasourceImpl
    ): IncomeCategoryRemoteDatasource

}