package com.example

import helloworld.GreeterGrpcKt
import helloworld.HelloReply
import helloworld.HelloRequest
import javax.inject.Singleton

@Singleton
@Suppress("unused")
class GreetingEndpoint(private val greetingService : GreetingService) : GreeterGrpcKt.GreeterCoroutineImplBase() {
    override suspend fun sayHello(request: HelloRequest): HelloReply {

        val message = greetingService.sayHello(request.name)
        return HelloReply.newBuilder().setMessage(message).build()
    }
}