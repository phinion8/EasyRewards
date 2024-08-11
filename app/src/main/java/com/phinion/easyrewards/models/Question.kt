package com.phinion.easyrewards.models

data class Question(
    val index: Int = -1,
    val question: String = "",
    val option1: String = "",
    val option2: String = "",
    val option3: String = "",
    val option4: String = "",
    val answer: String = ""
)