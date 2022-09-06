package com.george.copticorphanstask.ui.main.fragments.my_repo_frag

import androidx.lifecycle.*
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.network.asDomainModels
import com.george.copticorphanstask.network.model.remote_models.RepositoryRemote
import com.george.copticorphanstask.repository.GithubRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyReposViewModel @Inject constructor(
    private val repo: GithubRepo
) : ViewModel() {

    private var _page = 1
    private var _refreshPage = false
    private var _firstTime = true
    private var _usersReposResponse: MutableList<RepositoryRemote>? = null
    private val _usersReposMutableLiveData =
        MutableLiveData<Resource<MutableList<RepositoryRemote>>?>()

    // never used
    val usersReposMutableLiveData: LiveData<Resource<MutableList<RepositoryRemote>>?> =
        _usersReposMutableLiveData

    // info: list of user repos
    val userReposList = Transformations.map(_usersReposMutableLiveData) {
        Timber.tag("GEORGE").i("ViewModel: list size >>> ${it?.data?.size}")
        it?.data?.asDomainModels() ?: emptyList()
    }

    // info: first time load the page to show shimmer effect
    val shimmerShowEvent = Transformations.map(_usersReposMutableLiveData) {
        it?.let { it.success.isLoading() && _firstTime }
    }

    // info: when success and list is empty
    val searchEmptyTextShowEvent = Transformations.map(userReposList) {
        _usersReposMutableLiveData.value?.success?.isSuccess() == true && it?.isEmpty() ?: true
    }

    // info: first time load the page  to show bottom progress bar
    val progressShowEvent = Transformations.map(_usersReposMutableLiveData) {
        it?.let { it.success.isLoading() && !_firstTime }
    }

    // info: when error
    val errorShowEvent = Transformations.map(_usersReposMutableLiveData) {
        if (it?.success?.isError() == true) it.message else null
    }

    // info: when start refresh the page
    val refreshSwipeEvent = Transformations.map(_usersReposMutableLiveData) {
        it?.let { it.success.isLoading() && _refreshPage }
    }

    init {
        getUserRepos()
    }

    private fun getUserRepos() = viewModelScope.launch {
        _usersReposMutableLiveData.value = Resource.loading()
        try {
            val result = repo.getUserRepositories(_page) { response ->
                _page++
                if (_usersReposResponse == null) {
                    _usersReposResponse = response
                    _firstTime = false
                    _refreshPage = false
                } else {
                    _usersReposResponse?.addAll(response)
                }
                Resource.success(_usersReposResponse ?: response)
            }
            Timber.d("""
                count: ${result.data?.size}, 
                result: $result
            """.trimIndent())
            _usersReposMutableLiveData.value = result
        } catch (e: Exception) {
            Timber.e("$e")
            _usersReposMutableLiveData.value = Resource.error("$e")
        }
    }

    /**
     * rest all values again to call api starting from the first page
     */
    fun onResetUserRepos() {
        _page = 1
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
        if (isLastPageCalculator()) return
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