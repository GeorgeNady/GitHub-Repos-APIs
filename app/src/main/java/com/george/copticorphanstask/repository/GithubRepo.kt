package com.george.copticorphanstask.repository

import com.george.copticorphanstask.BuildConfig
import com.george.copticorphanstask.network.BaseDataSource
import com.george.copticorphanstask.network.GithubService
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.network.model.remote_models.RepositoryRemote
import com.george.copticorphanstask.network.model.responses.PublicRepoResponse
import com.george.copticorphanstask.network.model.responses.SearchRepositoriesResponse
import com.george.copticorphanstask.util.PER_PAGE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Inject

class GithubRepo @Inject constructor(
    private val githubService: GithubService
) : BaseDataSource() {

    companion object {
        private const val token = "Bearer ${BuildConfig.GITHUB_API_TOKEN}"
    }

    enum class SortType(val sort: String) {
        STARS("stars"),
        FORKS("forks"),
        HELP_WANTED_ISSUE("help-wanted-issues"),
        UPDATED("updated");
    }

    enum class OrderType(val order: String) {
        DESC("desc"),
        ASC("asc");
    }

    suspend fun getPublicRepositories(
        since: Int = 0,
        pagingLogic: (List<RepositoryRemote>) -> Resource<List<RepositoryRemote>>
    ) = safeApiCallPaging(pagingLogic) { githubService.getPublicRepositories(since) }

    suspend fun searchForPublicRepo(
        query: String?,
        page: Int = 1,
        sort: SortType = SortType.UPDATED,
        order: OrderType = OrderType.DESC,
        pagingLogic: (SearchRepositoriesResponse) -> Resource<SearchRepositoriesResponse>
    ) = safeApiCallPaging(pagingLogic) {
        githubService.searchRepositories(query, page, PER_PAGE, sort.sort, order.order, token)
    }

    suspend fun getUserRepositories(
        page: Int, pagingLogic: (MutableList<RepositoryRemote>) -> Resource<MutableList<RepositoryRemote>>
    ) = safeApiCallPaging(pagingLogic) { githubService.getUserRepositories(page, PER_PAGE, token) }


}