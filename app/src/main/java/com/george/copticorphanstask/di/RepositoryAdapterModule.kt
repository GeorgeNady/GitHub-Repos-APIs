package com.george.copticorphanstask.di

import com.george.copticorphanstask.adapter.repository.RepositoryAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object RepositoryAdapterModule {

    /**
     * we create a providers here
     * to use it if we will inject some dependencies to the constructor of the adapter class
     */
    @Provides
    fun provideRepositoryAdapter() = RepositoryAdapter()

}