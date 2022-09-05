package com.george.copticorphanstask.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OwnerDomain(
    var id: Int,
    var name: String,
    var nodeId: String,
    var avatarUrl: String,
    var url: String,
    var htmlUrl: String,
    var type: String,
) : Parcelable