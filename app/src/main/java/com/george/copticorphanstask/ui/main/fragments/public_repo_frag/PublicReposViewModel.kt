package com.george.copticorphanstask.ui.main.fragments.public_repo_frag

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.network.asDomainModels
import com.george.copticorphanstask.network.model.remote_models.RepositoryRemote
import com.george.copticorphanstask.repository.GithubRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PublicReposViewModel @Inject constructor(
    private val repo: GithubRepo
) : ViewModel() {

    private var _firstTime = true
    private var _refreshPage = false
    private var _publicReposResponse: MutableList<RepositoryRemote>? = null
    private val _publicReposMutableLiveData = MutableLiveData<Resource<List<RepositoryRemote>>?>()

    // info: list of clashes
    val publicReposList = Transformations.map(_publicReposMutableLiveData) {
        it?.data?.asDomainModels() ?: emptyList()
    }

    // info: first time load the page to show shimmer effect
    val shimmerShowEvent = Transformations.map(_publicReposMutableLiveData) {
        it?.let { it.success.isLoading() && _firstTime }
    }

    // info: first time load the page  to show bottom progress bar
    val progressShowEvent = Transformations.map(_publicReposMutableLiveData) {
        it?.let { it.success.isLoading() && !_firstTime }
    }

    // info: when error
    val errorShowEvent = Transformations.map(_publicReposMutableLiveData) {
        it?.success?.isError()
    }

    // info: when start refresh the page
    val refreshSwipeEvent = Transformations.map(_publicReposMutableLiveData) {
        it?.let { it.success.isLoading() && _refreshPage }
    }

    init {
        getPublicRepos()
    }

    private fun getPublicRepos() = viewModelScope.launch {
        _publicReposMutableLiveData.value = Resource.loading()
        try {
            val result = repo.getPublicRepositories { response ->
                if (_publicReposResponse == null) {
                    _publicReposResponse = response.toMutableList()
                    _firstTime = false
                    _refreshPage = false
                } else {
                    val oldList = _publicReposResponse
                    oldList?.addAll(response)
                }
                Timber.d("new response >>> $_publicReposResponse")
                Resource.success(_publicReposResponse ?: response)
            }
            _publicReposMutableLiveData.postValue(result)
        } catch (e: Exception) {
            Timber.e("$e")
            _publicReposMutableLiveData.value = Resource.error("$e")
        }
    }

    /**
     * rest all values again to call api starting from the first page
     */
    fun onResetPublicRepos() {
        _firstTime = true
        _refreshPage = true
        _publicReposResponse = null
        _publicReposMutableLiveData.value = null
        getPublicRepos()
    }

    /**
     * call api with the next page in case we not in the last page
     */
    fun onLoadMorePublicRepos() {
        if (isLastPageCalculator()) return
        getPublicRepos()
    }

    private fun isLastPageCalculator(): Boolean {
        /*val meta = _publicReposMutableLiveData.value?.data?.repositories
        val lastPage = meta?.lastPage ?: 0
        val currentPage = meta?.currentPage ?: 0
        return lastPage - currentPage < 1*/
        return false
    }

}