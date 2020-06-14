package com.example

import grpc_schemas.Score
import javax.inject.Singleton

@Singleton
class ScoreService {
    fun getScores(userId: Int): List<Score> {
        val s1 = Score.newBuilder().setId(1).setScore(100).setUserId(userId).setYear(2020).setTimeNum(1).build()
        val s2 = Score.newBuilder().setId(2).setScore(200).setUserId(userId).setYear(2020).setTimeNum(2).build()
        return listOf(s1, s2)
    }
}