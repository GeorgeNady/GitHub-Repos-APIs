package com.george.copticorphanstask.adapter.repository

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.george.copticorphanstask.databinding.ItemGithubRepoBinding
import com.george.copticorphanstask.domain.RepositoryDomain


/**
 * # ViewHolder
 * @param binding [ItemGithubRepoBinding]
 */
class RepositoryViewHolder(private val binding: ItemGithubRepoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /** ## bind model to the view holder */
    fun bind(item: RepositoryDomain) {
        with(binding) {
            bRepository = item

            ivAvatar.setOnClickListener {
                openLinkOnTheBrowser(item.owner.htmlUrl)
            }

            tvOwnerName.setOnClickListener {
                openLinkOnTheBrowser(item.owner.htmlUrl)
            }

            repoDetails.setOnClickListener {
                openLinkOnTheBrowser(item.htmlUrl)
            }

        }
    }

    /**
     * ## open links on the browser
     * if the github was installed
     * user cann open specific profile page or repo
     * direct on the github mobile app
     */
    private fun openLinkOnTheBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(binding.root.context, browserIntent, null)
    }

    companion object {
        /** ## inflater method */
        fun from(parent: ViewGroup) =
            RepositoryViewHolder(ItemGithubRepoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }
}