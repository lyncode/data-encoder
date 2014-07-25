package com.lyncode.dataencoder.persistence.model;

import com.lyncode.dataencoder.crypt.CryptUtils;
import com.lyncode.dataencoder.crypt.PKCS12Example;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class NamedKeyPair {
    public static NamedKeyPair parse(File file) throws IOException {
        RSAPublicKey publicKey = CryptUtils.parseRSA(FileUtils.readFileToString(file));
        RSAPrivateKey privateKey = CryptUtils.parseRSAPrivate(FileUtils.readFileToString(new File(file.getPath()+".key")));
        return new NamedKeyPair(file.getName(), publicKey, privateKey);
    }
    private final String name;
    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    public NamedKeyPair(String name, RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.name = name;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String name() {
        return name;
    }

    public String encrypt (String content) throws CryptUtils.EncryptException {
        return CryptUtils.encrypt(publicKey, content);
    }

    public String pub() {
        return CryptUtils.convertToString(publicKey);
    }

    public void writeKeyStore(String alias, String password, OutputStream outputStream) throws Exception {
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(null);
        Certificate masterCert = PKCS12Example.createMasterCert(publicKey, privateKey);
        keystore.setCertificateEntry(alias, masterCert);
        keystore.store(outputStream, password.toCharArray());
    }
}
