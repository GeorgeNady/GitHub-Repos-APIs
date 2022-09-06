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
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class PublicReposViewModel @Inject constructor(
    private val repo: GithubRepo
) : ViewModel() {

    private var _firstTime = true
    private var _refreshPage = false
    private var _since = 0
    private var _publicReposResponse: MutableList<RepositoryRemote>? = null
    private val _publicReposMutableLiveData = MutableLiveData<Resource<List<RepositoryRemote>>?>()

    // info: list of public repos
    val publicReposList = Transformations.map(_publicReposMutableLiveData) {
        it?.data?.asDomainModels() ?: emptyList()
    }

    // info: first time load the page to show shimmer effect
    val shimmerShowEvent = Transformations.map(_publicReposMutableLiveData) {
        it?.let { it.success.isLoading() && _firstTime }
    }

    // info: when success and list is empty
    val searchEmptyTextShowEvent = Transformations.map(publicReposList) {
        _publicReposMutableLiveData.value?.success?.isSuccess() == true && it?.isEmpty() ?: true
    }

    // info: first time load the page  to show bottom progress bar
    val progressShowEvent = Transformations.map(_publicReposMutableLiveData) {
        it?.let { it.success.isLoading() && !_firstTime }
    }

    // info: when error
    val errorShowEvent = Transformations.map(_publicReposMutableLiveData) {
        if (it?.success?.isError() == true) it.message else null
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
            val result = repo.getPublicRepositories(_since) { response ->
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
            _since = getSince() ?: 0
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
        _since = 0
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
        val link = _publicReposMutableLiveData.value!!.message ?: ""
        val regex = Regex("since=[0-9]\\w+")
        val notInTheLastPage = link.contains(regex)
        return !notInTheLastPage
    }

    /**
     * ## this regex used to extract the next since parameter from the header
     */
    private fun getSince(): Int? {
        val link = _publicReposMutableLiveData.value!!.message
        link?.let {
            var regex = Regex("since=[0-9]\\w+")
            var matcher = regex.find(link)
            regex = Regex("[0-9]\\w+")
            matcher = regex.find(matcher?.value.toString())
            if (matcher != null) {
                return matcher.value.toInt()
            }
        }
        return null
    }

}