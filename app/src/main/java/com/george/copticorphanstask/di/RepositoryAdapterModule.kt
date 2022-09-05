package com.george.copticorphanstask.di

import com.george.copticorphanstask.adapter.repository.RepositoryAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object RepositoryAdapterModule {

    @Provides
    fun provideRepositoryAdapter() = RepositoryAdapter()

}