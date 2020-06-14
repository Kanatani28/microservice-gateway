package com.example

import grpc_schemas.Question
import javax.inject.Singleton

@Singleton
class QuestionService {
    fun getQuestions(yearTimeId: Int) : List<Question> {
        val q = Question.newBuilder().setId(1).setQuestion("ああああ").setSortOrder(2).setYearTimeId(yearTimeId).build()
        val q2 = Question.newBuilder().setId(2).setQuestion("いいいいい").setSortOrder(3).setYearTimeId(yearTimeId).build()
        return listOf(q, q2)
    }
}
