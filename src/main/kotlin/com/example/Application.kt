package com.example

import com.example.application.routes.fitnessRoutes
import com.example.domain.port.FitnessRepository
import com.example.infrastructure.repository.FitnessRepositoryImpl
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        gson {
        }
    }
    install(Koin) {
        slf4jLogger()
        modules(module {
            single { MongoClient.create(
                environment.config.propertyOrNull("ktor.mongo.uri")?.getString() ?: throw RuntimeException("Failed to access MongoDB URI.")
            ) }
            single { get<MongoClient>().getDatabase(environment.config.property("ktor.mongo.database").getString()) }
        }, module {
            single<FitnessRepository> { FitnessRepositoryImpl(get()) }
        })
    }
    routing {
        swaggerUI(path = "swagger-ui", swaggerFile = "openapi/documentation.yaml") {
            version = "4.15.5"
        }
        fitnessRoutes()
    }
}
