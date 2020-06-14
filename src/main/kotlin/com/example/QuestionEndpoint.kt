package com.example

import grpc_schemas.QuestionListRequest
import grpc_schemas.QuestionListResponse
import grpc_schemas.YearTime
import grpc_schemas.QuestionServiceGrpcKt
import javax.inject.Singleton

@Singleton
@Suppress("unused")
class QuestionEndpoint(private val questionService: QuestionService) : QuestionServiceGrpcKt.QuestionServiceCoroutineImplBase() {
    override suspend fun getQuestions(request: QuestionListRequest): QuestionListResponse {

        val questions = questionService.getQuestions(request.yearTimeId)
        return QuestionListResponse.newBuilder()
                .setYearTime(
                        YearTime.newBuilder()
                                .setId(1)
                                .setYear(2020)
                                .setTimeNum(1)
                                .build()
                )
                .addAllQuestions(questions).build()
    }
}