package com.george.copticorphanstask.adapter.repository

import androidx.recyclerview.widget.DiffUtil
import com.george.copticorphanstask.domain.RepositoryDomain

class RepositoryComparator : DiffUtil.ItemCallback<RepositoryDomain>() {
    override fun areItemsTheSame(oldItem: RepositoryDomain, newItem: RepositoryDomain) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: RepositoryDomain, newItem: RepositoryDomain) =
        oldItem == newItem
}