package com.george.copticorphanstask.repository

import com.george.copticorphanstask.BuildConfig
import com.george.copticorphanstask.network.BaseDataSource
import com.george.copticorphanstask.network.GithubService
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.network.model.responses.PublicRepoResponse
import javax.inject.Inject

class GithubRepo @Inject constructor(
    private val githubService: GithubService
) : BaseDataSource() {

    companion object {
        private const val token = BuildConfig.GITHUB_API_TOKEN
    }

    suspend fun getAllPublicGithubRepositories(
        page: Int, perPage: Int, pagingLogic: (PublicRepoResponse) -> Resource<PublicRepoResponse>
    ) = safeApiCallPaging(pagingLogic) { githubService.getAllPublicGithubRepositories(page, perPage, "Bearer $token") }


}