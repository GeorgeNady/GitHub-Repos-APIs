package com.george.copticorphanstask.network

import com.george.copticorphanstask.network.model.responses.PublicRepoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GithubService {

    @GET("user/repos")
    suspend fun getAllPublicGithubRepositories(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") token: String
    ): Response<PublicRepoResponse>

}