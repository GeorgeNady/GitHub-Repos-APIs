package com.george.copticorphanstask.di

import androidx.fragment.app.Fragment
import com.george.copticorphanstask.adapter.repository.RepositoryAdapter
import com.george.copticorphanstask.base.fragments.MainBaseFragment
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
    fun provideRepositoryAdapter(
        fragment: Fragment
    ) = RepositoryAdapter(fragment as MainBaseFragment<*>)

}