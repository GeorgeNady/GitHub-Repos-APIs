package com.george.copticorphanstask.adapter.repository

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.george.copticorphanstask.adapter.repository.RepositoryViewHolder.Companion.from
import com.george.copticorphanstask.domain.RepositoryDomain

class RepositoryAdapter : ListAdapter<RepositoryDomain, RepositoryViewHolder>(RepositoryComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = from(parent)

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


}