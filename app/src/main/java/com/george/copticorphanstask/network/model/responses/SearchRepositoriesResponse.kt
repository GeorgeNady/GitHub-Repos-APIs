package com.george.copticorphanstask.network.model.responses

import com.george.copticorphanstask.network.model.remote_models.RepositoryRemote
import com.george.copticorphanstask.util.PER_PAGE
import com.google.gson.annotations.SerializedName

data class SearchRepositoriesResponse(
    @SerializedName("total_count") val totalCount:Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    var items: MutableList<RepositoryRemote>
) {

    fun isLastPage(currentPage: Int): Boolean {
        val retrievedItems = currentPage * PER_PAGE
        return retrievedItems >= totalCount
    }

}