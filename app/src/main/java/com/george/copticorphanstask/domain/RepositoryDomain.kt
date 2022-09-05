package com.george.copticorphanstask.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepositoryDomain @JvmOverloads constructor(
    var id: Int,
    var name: String,
    var isPrivate: Boolean,
    var owner: OwnerDomain,
    var htmlUrl: String,
    var description: String? = "N/A",
    var createdAt: String? = "N/A",
    var language: String? = "N/A",
    var visibility: String? = "N/A",
    var defaultBranch: String? = "N/A",
    var forks: Int,
    var watchers: Int,
    var stars: Int,
) : Parcelable
