package com.example.securityjwt.config

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class ServerCryptoKeyRepositoryImpl : ServerCryptoKeyRepository {

    private val container = ConcurrentHashMap<KeyType, KeyContainer>()

    override fun find(keytype: KeyType): KeyContainer {
        return container[keytype]!!
    }

    override fun save(keytype: KeyType, keyContainer: KeyContainer) {
        container[keytype] = keyContainer
    }
}