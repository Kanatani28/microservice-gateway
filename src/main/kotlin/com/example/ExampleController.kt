package com.example

import com.fasterxml.jackson.module.kotlin.*
import com.google.protobuf.util.JsonFormat
import grpc_schemas.QuestionListRequest
import grpc_schemas.QuestionServiceGrpcKt
import grpc_schemas.ScoreRequest
import grpc_schemas.ScoreServiceGrpcKt
import helloworld.GreeterGrpcKt
import helloworld.HelloRequest
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.cookie.Cookie
import io.micronaut.security.annotation.Secured
import org.apache.commons.codec.digest.DigestUtils
import java.net.URL
import java.security.Principal
import javax.inject.Singleton


@Controller("/")
class ExampleController(private val userRepository: UserRepository,
                        private val questionClient: QuestionServiceGrpcKt.QuestionServiceCoroutineStub,
                        private val scoreClient: ScoreServiceGrpcKt.ScoreServiceCoroutineStub,
                        private val greeterClient: GreeterGrpcKt.GreeterCoroutineStub) {

    @Secured("isAnonymous()")
    @Post("/auth")
    fun auth(@Body authInfo: Map<String, String>): MutableHttpResponse<Any>? {

        val client = RxHttpClient.create(URL("http://localhost:8080"))
        val res = client.retrieve(HttpRequest.POST("/login", authInfo))
        val jsonMapper = jacksonObjectMapper()
        val authResult = jsonMapper.readValue<Map<String, String>>(res.blockingFirst())
        val cookie = Cookie.of("JWT", authResult.getValue("access_token"))
        return HttpResponse.ok<Any>().cookie(cookie)
    }

    @Secured("isAnonymous()")
    @Get("/authSuccess")
    fun authSuccess(): MutableHttpResponse<Any>? {
        val cookie = Cookie.of("JWT", "a")
        return HttpResponse.ok<Any>().cookie(cookie)
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
    fun questionClient( @GrpcChannel("question") channel : ManagedChannel) : QuestionServiceGrpcKt.QuestionServiceCoroutineStub {
        return QuestionServiceGrpcKt.QuestionServiceCoroutineStub(
                channel
        )
    }

    @Singleton
    fun scoreClient( @GrpcChannel("score") channel : ManagedChannel) : ScoreServiceGrpcKt.ScoreServiceCoroutineStub {
        return ScoreServiceGrpcKt.ScoreServiceCoroutineStub(
                channel
        )
    }

}