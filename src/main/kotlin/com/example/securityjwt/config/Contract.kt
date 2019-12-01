package com.example.securityjwt.config

const val PKCS12 = "PKCS12"
val SECRET: String = "Secret_key_to_generate_JWTs"
val EXPIRATION_TIME: Long = 10 * 60 * 60 * 1000
val TOKEN_PREFIX: String = "Bearer "
val HEADER_AUTH: String = "Authorization"
val WRONG_AUTH = "WRONG DATA - you cannot get token"

//https://stackoverflow.com/questions/26432112/jwtsecuritytokenhandler-says-signature-of-jwt-valid-after-changing-1-char