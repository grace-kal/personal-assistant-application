package com.example.personalassistantapp.models

data class Cloth(
    val id: Int,
    val title: String,
    val description: String,
    val season: String,
    val color: String,
    val area: String,
    val kind: String,
    val weatherKind: String,
    val thickness: String,
    val length: String,
    val imageUrl: String // URL azure blob storage
)
