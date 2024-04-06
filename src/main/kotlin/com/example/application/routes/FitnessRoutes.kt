package com.example.application.routes

import com.example.application.request.FitnessRequest
import com.example.application.request.toDomain
import com.example.domain.port.FitnessRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.fitnessRoutes(){
    val repository by inject<FitnessRepository>()

    route("/fitness"){

        post {
            val fitness=call.receive<FitnessRequest>()
            val insertedId=repository.insertOne(fitness.toDomain())
            call.respond(HttpStatusCode.Created, "Created fitness with id $insertedId")
        }

        delete("/{id?}") {
            val id=call.parameters["id"]?:return@delete call.respondText(
                text = "Missing fitness id",
                status = HttpStatusCode.BadRequest
            )
            val delete:Long=repository.deleteById(ObjectId(id))
            if (delete==1L){
                return@delete call.respondText("Fitness Deleted successfully", status = HttpStatusCode.OK)
            }
            return@delete call.respondText("Fitness not found", status = HttpStatusCode.NotFound)
        }

        get("/{id?}") {
            val id = call.parameters["id"]
            if (id.isNullOrEmpty()) {
                return@get call.respondText(
                    text = "Missing id",
                    status = HttpStatusCode.BadRequest
                )
            }
            repository.findById(ObjectId(id))?.let {
                call.respond(it.toResponse())
            } ?: call.respondText("No records found for id $id")
        }

        patch("/{id?}") {
            val id = call.parameters["id"] ?: return@patch call.respondText(
                text = "Missing fitness id",
                status = HttpStatusCode.BadRequest
            )
            val updated = repository.updateOne(ObjectId(id), call.receive())
            call.respondText(
                text = if (updated == 1L) "Fitness updated successfully" else "Fitness not found",
                status = if (updated == 1L) HttpStatusCode.OK else HttpStatusCode.NotFound
            )
        }
    }
}