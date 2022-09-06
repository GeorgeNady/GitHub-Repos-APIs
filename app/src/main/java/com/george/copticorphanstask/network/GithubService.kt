package com.george.copticorphanstask.network

import com.george.copticorphanstask.network.model.remote_models.RepositoryRemote
import com.george.copticorphanstask.network.model.responses.SearchRepositoriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface GithubService {

    @GET("repositories")
    suspend fun getPublicRepositories(
        @Query("since") since: Int,
    ): Response<List<RepositoryRemote>>

    @GET("search/repositories")
    @Headers("Accept: application/vnd.github+json")
    suspend fun searchRepositories(
        @Query("q") query: String? = "",
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Header("Authorization") token: String
    ): Response<SearchRepositoriesResponse>

    @GET("user/repos")
    suspend fun getUserRepositories(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") token: String
    ): Response<MutableList<RepositoryRemote>>

}