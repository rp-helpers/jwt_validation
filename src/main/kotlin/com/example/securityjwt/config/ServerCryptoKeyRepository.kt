package com.example.securityjwt.config

interface ServerCryptoKeyRepository {
    fun find(keytype: KeyType): KeyContainer
    fun save(keytype: KeyType, keyContainer: KeyContainer)
}