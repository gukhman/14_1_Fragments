package com.example.a14_1_fragments

import java.io.Serializable

data class Note(
    val id: Int,
    var text: String,
    val timestamp: Long,
    var isCompleted: Boolean = false
) : Serializable