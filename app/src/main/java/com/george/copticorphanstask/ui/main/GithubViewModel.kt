package com.george.copticorphanstask.ui.main

import androidx.lifecycle.*
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.network.asDomainModels
import com.george.copticorphanstask.network.model.responses.PublicRepoResponse
import com.george.copticorphanstask.repository.GithubRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubViewModel @Inject constructor(
    private val repo: GithubRepo
) : ViewModel() {

    private var _page = 1
    private val _perPage = 10
    private var _refreshPage = false
    private var _homeFirstTime = true
    private var _publicReposResponse: PublicRepoResponse? = null
    private val _publicReposMutableLiveData = MutableLiveData<Resource<PublicRepoResponse>?>()

    // info: list of clashes
    val publicReposList = Transformations.map(_publicReposMutableLiveData) {
        it?.data?.asDomainModels() ?: emptyList()
    }
    // info: first time load the page to show shimmer effect
    val shimmerShowEvent = Transformations.map(_publicReposMutableLiveData) {
        it?.let { it.success.isLoading() && _homeFirstTime }
    }
    // info: first time load the page  to show bottom progress bar
    val progressShowEvent = Transformations.map(_publicReposMutableLiveData) {
        it?.let { it.success.isLoading() && !_homeFirstTime }
    }
    // info: when error
    val errorShowEvent = Transformations.map(_publicReposMutableLiveData) {
        it?.success?.isError()
    }
    // info: when start refresh the page
    val refreshSwipeEvent = Transformations.map(_publicReposMutableLiveData) {
        it?.let { it.success.isLoading() && _refreshPage }
    }

    init { getAllPublicGithubRepositories() }

    private fun getAllPublicGithubRepositories() = viewModelScope.launch {
        _publicReposMutableLiveData.value = Resource.loading()
        try {
            _publicReposMutableLiveData.value = repo.getAllPublicGithubRepositories(_page,_perPage) { response ->
                _page++
                if (_publicReposResponse == null) {
                    _publicReposResponse = response
                    _homeFirstTime = false
                    _refreshPage = false
                } else {

                    val oldArticles = _publicReposResponse?.repositories
                    val newsArticles = response.repositories
                    oldArticles?.addAll(newsArticles ?: emptyList())
                }
                Resource.success(_publicReposResponse ?: response)
            }
        } catch (e:Exception) {
            _publicReposMutableLiveData.value = Resource.error(e.toString())
        }
    }

    /**
     * rest all values again to call api starting from the first page
     */
    fun onResetPublicRepos() {
        _page = 1
        _homeFirstTime = true
        _publicReposResponse = null
        _refreshPage = true
        _publicReposMutableLiveData.value = null
        getAllPublicGithubRepositories()
    }

    /**
     * call api with the next page in case we not in the last page
     */
    /*fun onLoadMorePublicRepos() {
        if (isLastPageCalculator()) return
        getAllPublicGithubRepositories()
    }*/

    /*private fun isLastPageCalculator(): Boolean {
        val meta = _publicReposMutableLiveData.value?.data?.repositories.
        val lastPage = meta?.lastPage ?: 0
        val currentPage = meta?.currentPage ?: 0
        return lastPage - currentPage < 1
    }*/

}