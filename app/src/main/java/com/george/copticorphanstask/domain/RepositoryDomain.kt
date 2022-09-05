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
    var fork: Boolean,
    var createdAt: String,
    var language: String,
    var visibility: String,
    var default_branch: String
) : Parcelable
