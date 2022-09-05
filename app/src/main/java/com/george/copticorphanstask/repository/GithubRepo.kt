package com.george.copticorphanstask.repository

import com.george.copticorphanstask.BuildConfig
import com.george.copticorphanstask.network.BaseDataSource
import com.george.copticorphanstask.network.GithubService
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.network.model.remote_models.RepositoryRemote
import com.george.copticorphanstask.network.model.responses.PublicRepoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Inject

class GithubRepo @Inject constructor(
    private val githubService: GithubService
) : BaseDataSource() {

    companion object {
        private const val token = BuildConfig.GITHUB_API_TOKEN
        private const val perPage = 10
    }

    suspend fun getPublicRepositories(
        since: Int = 0,
        pagingLogic: (List<RepositoryRemote>) -> Resource<List<RepositoryRemote>>
    ) = safeApiCallPaging(pagingLogic) { githubService.getPublicRepositories(since) }

    suspend fun getUserRepositories(
        page: Int, pagingLogic: (MutableList<RepositoryRemote>) -> Resource<MutableList<RepositoryRemote>>
    ) = safeApiCallPaging(pagingLogic) { githubService.getUserRepositories(page, perPage, "Bearer $token") }


}