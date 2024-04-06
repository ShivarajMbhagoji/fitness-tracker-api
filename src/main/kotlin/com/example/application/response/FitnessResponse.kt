package com.example.application.response

import com.example.domain.entity.FitnessDetails



data class FitnessResponse(
    val id: String,
    val exerciseType: String,
    val notes: String,
    val details: FitnessDetails
)