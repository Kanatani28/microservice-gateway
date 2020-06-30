package com.example

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.reactivex.Flowable
import org.apache.commons.codec.digest.DigestUtils
import org.reactivestreams.Publisher
import java.util.*
import javax.inject.Singleton

@Singleton
class AuthenticationProviderUserPassword(private val userRepository: UserRepository): AuthenticationProvider {

    override fun authenticate(httpRequest: HttpRequest<*>?, authenticationRequest: AuthenticationRequest<*, *>?): Publisher<AuthenticationResponse> {
        if (authenticationRequest != null && authenticationRequest.identity != null && authenticationRequest.secret != null) {

            val sha256Password = DigestUtils.sha256Hex(authenticationRequest.secret.toString())
            val u = userRepository.findByLoginIdAndPassword(authenticationRequest.identity.toString(), sha256Password)
//            if (authenticationRequest.identity == "sherlock" && authenticationRequest.secret == "password") {
            if (u != null) {
                val resp = Flowable.just<AuthenticationResponse>(UserDetails(authenticationRequest.identity as String, ArrayList()))
                return resp
            }
        }
        return Flowable.just<AuthenticationResponse>(AuthenticationFailed())
    }
}