package com.example

import com.google.protobuf.Descriptors
import com.google.protobuf.util.JsonFormat
import grpc_schemas.*
import helloworld.GreeterGrpcKt
import helloworld.HelloRequest
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import org.apache.commons.codec.digest.DigestUtils
import java.security.Principal
import javax.inject.Singleton

@Controller("/")
class ExampleController(private val userRepository: UserRepository,
                        private val questionClient: QuestionServiceGrpcKt.QuestionServiceCoroutineStub,
                        private val scoreClient: ScoreServiceGrpcKt.ScoreServiceCoroutineStub,
                        private val greeterClient: GreeterGrpcKt.GreeterCoroutineStub) {

    @Secured("isAnonymous()")
    @Get("/hello")
    fun helloWorld(): String {
        return "Hello. ";
    }

    @Secured("isAnonymous()")
    @Post("/login2")
    fun login(@Body user: User) {
        println(user);
    }

    @Secured("isAnonymous()")
    @Post("/authFailed")
    fun authFailed(): String {
        return "Failed";
    }

    @Secured("isAuthenticated()")
    @Get("/hello2")
    fun index(principal: Principal): Principal {
        return principal
    }

    @Secured("isAnonymous()")
    @Get("/users")
    fun all(): List<User> {
        return userRepository.findAll()
    }

    @Post("/users")
    @Secured("isAnonymous()")
    fun save(user: User): String {
        user.password = DigestUtils.sha256Hex(user.password)
        userRepository.save(user)
        return "Done"
    }

    @Post("/users2")
    @Secured("isAnonymous()")
    fun lo(user: User): User? {
        user.password = DigestUtils.sha256Hex(user.password)
        return userRepository.findByLoginIdAndPassword(user.loginId, user.password)
    }


    @Get("/grpc")
    @Secured("isAnonymous()")
    suspend fun g(): String {
        val rep = greeterClient.sayHello(HelloRequest.newBuilder().setName("John").build())

        return rep.toString();
    }

    @Get("/grpc_q")
    @Secured("isAnonymous()")
    suspend fun questions(): String {
        val rep = questionClient.getQuestions(QuestionListRequest.newBuilder().setYearTimeId(1).build())
        return JsonFormat.printer().print(rep)
    }

    @Get("/grpc_s")
    @Secured("isAnonymous()")
    suspend fun scores(): String {
        val rep = scoreClient.getScores(ScoreRequest.newBuilder().setUserId(1).build())
        return JsonFormat.printer().print(rep)
    }
}

@Factory
class Clients {
    @Singleton
    fun greetingClient( @GrpcChannel(GrpcServerChannel.NAME) channel : ManagedChannel) : GreeterGrpcKt.GreeterCoroutineStub {
        return GreeterGrpcKt.GreeterCoroutineStub(
                channel
        )
    }
    @Singleton
    fun questionClient( @GrpcChannel(GrpcServerChannel.NAME) channel : ManagedChannel) : QuestionServiceGrpcKt.QuestionServiceCoroutineStub {
        return QuestionServiceGrpcKt.QuestionServiceCoroutineStub(
                channel
        )
    }

    @Singleton
    fun scoreClient( @GrpcChannel(GrpcServerChannel.NAME) channel : ManagedChannel) : ScoreServiceGrpcKt.ScoreServiceCoroutineStub {
        return ScoreServiceGrpcKt.ScoreServiceCoroutineStub(
                channel
        )
    }

}