package com.example.personalassistantapp.models

data class Cloth(
    val id: Int,
    val title: String,
    val description: String,
    val season: String,
    val imageUrl: String // URL azure blob storage
)
