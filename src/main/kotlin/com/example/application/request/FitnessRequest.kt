package com.example.application.request

import com.example.domain.entity.Fitness
import com.example.domain.entity.FitnessDetails
import org.bson.types.ObjectId

data class FitnessRequest(
    val exerciseType: String,
    val notes: String,
    val details: FitnessDetails
)
fun FitnessRequest.toDomain(): Fitness {
    return Fitness(
        id = ObjectId(),
        exerciseType = exerciseType,
        notes = notes,
        details = details
    )
}