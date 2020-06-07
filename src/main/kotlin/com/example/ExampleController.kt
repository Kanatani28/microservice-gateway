package com.example

import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import org.apache.commons.codec.digest.DigestUtils
import java.security.Principal

@Controller("/")
class ExampleController(private val userRepository: UserRepository) {

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
}