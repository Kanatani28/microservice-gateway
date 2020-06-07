package com.example

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(@Id
                @GeneratedValue
                var id: Long?,
                var loginId: String,
                var password: String)