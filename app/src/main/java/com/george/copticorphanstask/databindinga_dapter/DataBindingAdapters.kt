package com.george.copticorphanstask.databindinga_dapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.george.copticorphanstask.R
import com.george.copticorphanstask.domain.RepositoryDomain
import com.george.copticorphanstask.util.SEARCH_TIME_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("setAvatar")
fun ImageView.setAvatar(avatarUrl: String?) {
    Glide.with(context)
        .load(avatarUrl)
        .centerCrop()
        .into(this)
}

@BindingAdapter("setTimeStamp")
fun TextView.setTimeStamp(createdAt: String) {
    val longDate = createdAt.toLong()
    val formatter = SimpleDateFormat("dd-MM-yyyy")
    val dateString = formatter.format(Date(longDate))
    text = dateString
}

@BindingAdapter("setVisibilityIcon")
fun ImageView.setVisibilityIcon(repo: RepositoryDomain) {
    val public = ContextCompat.getDrawable(context, R.drawable.ic_public)
    val private = ContextCompat.getDrawable(context, R.drawable.ic_private)
    when (repo.visibility) {
        "public" -> setImageDrawable(public)
        "private" -> setImageDrawable(private)
        else -> visibility = View.GONE
    }
}

@BindingAdapter("setVisiblyWithBoolean")
fun View.setVisiblyWithBoolean(boolean: Boolean?) {
    visibility = boolean?.let {
        if (it) View.VISIBLE else View.GONE
    } ?: View.GONE
}

@BindingAdapter("setOnStartSwipeRefresh")
fun SwipeRefreshLayout.setOnStartSwipeRefresh(isRefreshing: Boolean?) {
    this.isRefreshing = isRefreshing ?: false
}

@BindingAdapter("onQueryTextChange")
fun SearchView.onQueryTextChange(void: () -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            void()
            return false
        }
    })

}

/*@BindingAdapter("setupRecyclerViewAdapter")
fun RecyclerView.setupRecyclerViewAdapter(adapter:  ListAdapter<*,*>) {
    this.adapter = adapter
}*/
