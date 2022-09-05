package com.george.copticorphanstask.ui.main.fragments

import com.george.copticorphanstask.domain.OwnerDomain
import com.george.copticorphanstask.domain.RepositoryDomain


val fakeRepositories = listOf(
    repoGenerator(519378666),
    repoGenerator(519378667),
    repoGenerator(519378668),
    repoGenerator(519378669),
    repoGenerator(519378670),
    repoGenerator(519378671),
    repoGenerator(519378672),
    repoGenerator(519378673),
    repoGenerator(519378674),
)

fun repoGenerator(id: Int) = RepositoryDomain(
    id = id,
    name = "Asteroid-Radar-App",
    isPrivate = false,
    owner = OwnerDomain(
        id = 29967846,
        name = "GeorgeNady",
        avatarUrl = "https://avatars.githubusercontent.com/u/29967846?v=4",
        htmlUrl = "https://github.com/GeorgeNady",
    ),
    htmlUrl = "https://github.com/GeorgeNady/Asteroid-Radar-App",
    description = "Asteroid Radar App",
    createdAt = "2022-07-30T00:08:27Z",
    language = "Kotlin",
    visibility = if (id % 2 == 0) "public" else "private",
    defaultBranch = "George_Branch",
    forks = 50,
    watchers = 150,
    stars = 3,
)