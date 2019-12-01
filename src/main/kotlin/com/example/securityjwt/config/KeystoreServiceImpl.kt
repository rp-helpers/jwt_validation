package com.example.securityjwt.config

import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.Certificate

@Service
class KeystoreServiceImpl : KeystoreService {

    override fun getPrivateKeyFromKeystore(keystorePass: String, keystoreAlias: String, keystorePath: String): PrivateKey {
        val keyStore: KeyStore = KeyStore.getInstance(PKCS12)
        //InputStream stream, char[] password
        val file = File(keystorePath)
        val inputStream = FileInputStream(file)
        keyStore.load(inputStream, keystorePass.toCharArray())
        return keyStore.getKey(keystoreAlias, keystorePass.toCharArray()) as PrivateKey


    }

    override fun getCertificateFromKeystore(keystorePass: String, keystoreAlias: String, keystorePath: String): Certificate {
        val keyStore: KeyStore = KeyStore.getInstance(PKCS12)
        val file = File(keystorePath)
        val inputStream = FileInputStream(file)
        keyStore.load(inputStream, keystorePass.toCharArray())
        return keyStore.getCertificate(keystoreAlias)
    }

    //https://stackoverflow.com/questions/18621508/getting-a-privatekey-object-from-a-p12-file-in-java
    //https://stackoverflow.com/questions/19937890/how-to-retrieve-my-public-and-private-key-from-the-keystore-we-created
    //https://www.baeldung.com/convert-file-to-input-stream

}