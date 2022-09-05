package com.george.copticorphanstask.util

import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class RecyclerViewScrollListener(
    private val onLoadMoreListener: () -> Unit
): RecyclerView.OnScrollListener() {

    private var isLoading = false
    private var isScrolling = false

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) isScrolling = true
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy <= 0) return

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount

        val isNotLoadingAndNotLastPage = !isLoading
        val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
        val isNotAtTheBeginning = firstVisibleItemPosition >= 0
        val isTotalMoreThanVisible = totalItemCount >= 10
        val shouldPaginate =
            isNotLoadingAndNotLastPage && isAtLastItem && isNotAtTheBeginning &&
                    isTotalMoreThanVisible && isScrolling

        if (shouldPaginate) {
            onLoadMoreListener()
            isScrolling = false
        }
    }
}