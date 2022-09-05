package com.george.copticorphanstask.adapter.repository

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.george.copticorphanstask.databinding.ItemGithubRepoBinding
import com.george.copticorphanstask.domain.RepositoryDomain

class RepositoryViewHolder(private val binding: ItemGithubRepoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RepositoryDomain) {
        with(binding) {
            bRepository = item
            // executePendingBindings()
        }
    }

    companion object {
        fun from(parent: ViewGroup) =
            RepositoryViewHolder(ItemGithubRepoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }
}