package com.george.copticorphanstask.ui.main

import androidx.lifecycle.*
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.network.asDomainModel
import com.george.copticorphanstask.network.model.remote_models.RepositoryRemote
import com.george.copticorphanstask.repository.GithubRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubViewModel @Inject constructor(
    private val repo: GithubRepo
) : ViewModel() {

    private var _page = 1
    private var _perPage = 10
    private var _refreshPage = false
    private var _firstTime = true
    private var _usersReposResponse: MutableList<RepositoryRemote>? = null
    private val _usersReposMutableLiveData = MutableLiveData<Resource<MutableList<RepositoryRemote>>?>()

    // info: list of clashes
    val publicReposList = Transformations.map(_usersReposMutableLiveData) {
        it?.data?.map { repo -> repo.asDomainModel() } ?: mutableListOf()
    }
    // info: first time load the page to show shimmer effect
    val shimmerShowEvent = Transformations.map(_usersReposMutableLiveData) {
        it?.let { it.success.isLoading() && _firstTime }
    }
    // info: first time load the page  to show bottom progress bar
    val progressShowEvent = Transformations.map(_usersReposMutableLiveData) {
        it?.let { it.success.isLoading() && !_firstTime }
    }
    // info: when error
    /*val errorShowEvent = Transformations.map(_usersReposMutableLiveData) {
        it?.success?.isError()
    }*/
    // info: when start refresh the page
    val refreshSwipeEvent = Transformations.map(_usersReposMutableLiveData) {
        it?.let { it.success.isLoading() && _refreshPage }
    }

    init { getUserRepos() }

    private fun getUserRepos() = viewModelScope.launch {
        _usersReposMutableLiveData.value = Resource.loading()
        try {
            _usersReposMutableLiveData.value = repo.getUserRepositories(_page, _perPage) { response ->
                _page++
                if (_usersReposResponse == null) {
                    _usersReposResponse = response
                    _firstTime = false
                    _refreshPage = false
                } else {
                    val oldArticles = _usersReposResponse
                    oldArticles?.addAll(response)
                }
                Resource.success(_usersReposResponse ?: response)
            }
        } catch (e:Exception) {
            _usersReposMutableLiveData.value = Resource.error(e.toString())
        }
    }

    /**
     * rest all values again to call api starting from the first page
     */
    fun onResetUserRepos() {
        _page = 1
        _perPage = 10
        _firstTime = true
        _refreshPage = true
        _usersReposResponse = null
        _usersReposMutableLiveData.value = null
        getUserRepos()
    }

    /**
     * call api with the next page in case we not in the last page
     */
    fun onLoadMoreUserRepos() {
        // if (isLastPageCalculator()) return
        getUserRepos()
    }

    private fun isLastPageCalculator(): Boolean {
        /*val meta = _publicReposMutableLiveData.value?.data?.repositories
        val lastPage = meta?.lastPage ?: 0
        val currentPage = meta?.currentPage ?: 0
        return lastPage - currentPage < 1*/
        return false
    }

}