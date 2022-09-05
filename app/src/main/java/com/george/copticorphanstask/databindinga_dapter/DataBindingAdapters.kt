package com.george.copticorphanstask.databindinga_dapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.george.copticorphanstask.R
import com.george.copticorphanstask.domain.RepositoryDomain
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

@BindingAdapter("setupRecyclerViewAdapter")
fun RecyclerView.setupRecyclerViewAdapter(adapter:  ListAdapter<*,*>) {
    this.adapter = adapter
}
