package com.example.securityjwt.config

import com.example.securityjwt.SecurityJwtApplication
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import java.security.PublicKey

@Component
class ApplicationReadyListener(
        private val keyStoreService: KeystoreService,
        private val serverCryptoKeyRepository: ServerCryptoKeyRepository
) : ApplicationListener<ApplicationReadyEvent> {

    private val keystorePass = "pass123"
    private val keystoreAlias = "jwt-service"
    private val keystorePath = "sources/keys/jwt-service.p12"

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        if (event.source is SpringApplication) {
            if ((event.source as SpringApplication).allSources.contains(SecurityJwtApplication::class.java)) {
                loadPair()
            }
        }
    }

    private fun loadPair() {
        logger.info("Start generating key pairs ...")
        val privateKey = keyStoreService.getPrivateKeyFromKeystore(keystorePass, keystoreAlias, keystorePath)
        val certificate = keyStoreService.getCertificateFromKeystore(keystorePass, keystoreAlias, keystorePath)
        val publicKey: PublicKey = certificate.publicKey
        serverCryptoKeyRepository.save(KeyType.APPLICATION_TYPE, KeyContainer(privateKey, publicKey, certificate))
        logger.info("Generating key pairs completed")
        logger.info("publicKey: $publicKey")
        val result = serverCryptoKeyRepository.find(KeyType.APPLICATION_TYPE)
        logger.info("result: $result")

    }

}