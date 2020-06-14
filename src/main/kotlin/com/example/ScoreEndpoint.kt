package com.example

import grpc_schemas.*
import javax.inject.Singleton

@Singleton
@Suppress("unused")
class ScoreEndpoint(private val scoreService: ScoreService) : ScoreServiceGrpcKt.ScoreServiceCoroutineImplBase() {
    override suspend fun getScores(request: ScoreRequest): ScoreListResponse {

        val scores = scoreService.getScores(request.userId)

        return ScoreListResponse.newBuilder().addAllScores(scores).build()
    }
}