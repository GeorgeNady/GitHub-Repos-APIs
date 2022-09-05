package com.george.copticorphanstask.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepositoryDomain(
    var id: Int,
    var name: String,
    var isPrivate: Boolean,
    var owner: OwnerDomain,
    var htmlUrl: String,
    var description: String,
    var createdAt: String,
    var language: String,
    var visibility: String,
    var defaultBranch: String,
    var forks: Int,
    var watchers: Int,
    var stars: Int,
) : Parcelable
