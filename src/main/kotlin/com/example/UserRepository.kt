package com.example

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.util.*

@JdbcRepository(dialect = Dialect.MYSQL)
interface UserRepository: CrudRepository<User, Long> {
    override fun findAll(): List<User>

    fun findById(id: String): Optional<User>

    fun findByLoginIdAndPassword(loginId: String, password: String): User?

    fun save(entity: User): User
}