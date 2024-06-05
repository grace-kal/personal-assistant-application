package com.example.personalassistantapp.models

data class Event(
    val time: String?,
    val title: String,
    val description: String,
    val invitedUsers: MutableList<String>?,
    val startDate: String?,
    val endDate: String?,
    val startTime: String?,
    val endTime: String?
)