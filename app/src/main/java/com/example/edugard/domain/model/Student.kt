package com.example.edugard.domain.model

import java.util.UUID

data class Student(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val grade: String,
    val studentId: String,
    val email: String,
    val phoneNumber: String,
    val deviceId: String? = null,
    val isActive: Boolean = true,
    val registrationDate: Long = System.currentTimeMillis()
)
