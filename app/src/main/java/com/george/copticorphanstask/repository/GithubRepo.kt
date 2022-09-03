package com.george.copticorphanstask.repository

import com.george.copticorphanstask.BuildConfig
import com.george.copticorphanstask.network.BaseDataSource
import com.george.copticorphanstask.network.GithubService
import javax.inject.Inject

class GithubRepo @Inject constructor(
    private val githubService: GithubService
) : BaseDataSource() {

    companion object {
        private val token = BuildConfig.GITHUB_API_TOKEN
    }



}