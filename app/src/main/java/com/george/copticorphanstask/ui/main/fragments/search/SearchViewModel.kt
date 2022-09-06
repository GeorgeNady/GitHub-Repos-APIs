package com.george.copticorphanstask.ui.main.fragments.search

import androidx.lifecycle.*
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.network.asDomainModels
import com.george.copticorphanstask.network.model.responses.SearchRepositoriesResponse
import com.george.copticorphanstask.repository.GithubRepo
import com.george.copticorphanstask.util.SEARCH_TIME_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: GithubRepo
) : ViewModel() {

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery

    /**
     * # [EditText] text watcher
     * ### we used this function that related to the UI here to
     */
    private var job: Job? = null
    fun textChangeListener(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
        job?.cancel()
        job = viewModelScope.launch {
            delay(SEARCH_TIME_DELAY)
            Timber.d("textChangeListener >>>> $charSequence")
            charSequence?.let {
                _searchQuery.value = it.toString()
            }
        }
    }

    private var _searchPage = 1
    private var _searchFirstTime = true
    private var _searchRefreshPage = false
    private var _searchResponse: SearchRepositoriesResponse? = null
    private var _searchResponseMutableLiveData = MutableLiveData<Resource<SearchRepositoriesResponse>?>()

    /**
     * __post a new full list in these below situation/s__
     * * Resources Status => isSuccess
     * * Search Query for => not empty
     */
    val searchList = Transformations.map(_searchResponseMutableLiveData) {
        // if success
        it?.data?.asDomainModels() ?: emptyList()
    }
    val searchShimmerShowEvent = Transformations.map(_searchResponseMutableLiveData) { it?.let { it.success.isLoading() && _searchFirstTime } }
    val searchProgressShowEvent = Transformations.map(_searchResponseMutableLiveData) { it?.let { it.success.isLoading() && !_searchFirstTime } }
    val searchErrorShowEvent = Transformations.map(_searchResponseMutableLiveData) {
        if (it?.success?.isError() == true) it.message else null
    }
    val searchRefreshSwipeEvent = Transformations.map(_searchResponseMutableLiveData) { it?.let { it.success.isLoading() && _searchRefreshPage } }
    val searchEmptyTextShowEvent = Transformations.map(searchList) {
        _searchResponseMutableLiveData.value?.success?.isSuccess() == true && it?.isEmpty() ?: true
                || _searchQuery.value == "" }

    private fun searchClashes() = viewModelScope.launch {
        _searchResponseMutableLiveData.value = Resource.loading()
        try {
            val response =
                repo.searchForPublicRepo(searchQuery.value, _searchPage) { resultRes ->
                    _searchPage++
                    if (_searchResponse == null) {
                        _searchResponse = resultRes
                        _searchFirstTime = false
                        _searchRefreshPage = false
                    } else {

                        val oldList = _searchResponse?.items
                        val newList = resultRes.items
                        oldList?.addAll(newList ?: emptyList())
                    }
                    Resource.success(_searchResponse ?: resultRes)
                }
            _searchResponseMutableLiveData.value = response
        } catch (e: Exception) {
            _searchResponseMutableLiveData.value = Resource.failed(e.stackTraceToString())
        }
    }

    // info: reset all variables when refresh with another api call
    fun onResetSearchClashes(isApiCallRequired: Boolean = false) {
        _searchPage = 1
        _searchFirstTime = true
        _searchResponse = null
        _searchRefreshPage = true
        _searchResponseMutableLiveData.value = null
        if (isApiCallRequired) searchClashes()
    }

    // info: on load more repos
    fun onLoadMoreClashes() {
        if (_searchResponse?.isLastPage(_searchPage) == true) return
        searchClashes()
    }

}