package com.example.a14_1_fragments

data class Note(
    val id: Int,
    val text: String,
    val timestamp: Long,
    var isCompleted: Boolean = false
)