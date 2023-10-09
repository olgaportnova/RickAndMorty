package com.example.rickandmorty.utils

interface SearchCategories {
    val title: String
}

enum class SearchCategoriesCharacters(override val title: String) : SearchCategories {
    NAME("Name"),
    SPECIES("Species"),
    TYPE("Unknown")
}

enum class SearchCategoriesEpisodes(override val title: String) : SearchCategories {
    NAME("Name"),
    EPISODE("Episode")
}

enum class SearchCategoriesLocations(override val title: String) : SearchCategories {
    NAME("Name"),
    TYPE("Type"),
    DIMENSION("Dimension")
}
