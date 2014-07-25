package com.lyncode.dataencoder.crypt;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.output.StringBuilderWriter;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;

import java.io.IOException;
import java.io.StringReader;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class CryptUtils {
    public static KeyPair createKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySize);
        return keyGen.genKeyPair();
    }

    public static String convertToString(PrivateKey privateKey) {
        StringBuilderWriter writer = new StringBuilderWriter();
        PEMWriter pemWriter = new PEMWriter(writer);
        try {
            pemWriter.writeObject(privateKey);
            pemWriter.flush();
            return writer.toString();
        } catch (IOException e) {
            // No IO exception (it's memory)
            throw new RuntimeException(e);
        }
    }

    public static String convertToString(PublicKey publicKey) {
        StringBuilderWriter writer = new StringBuilderWriter();
        PEMWriter pemWriter = new PEMWriter(writer);
        try {
            pemWriter.writeObject(publicKey);
            pemWriter.flush();
            return writer.toString();
        } catch (IOException e) {
            // No IO exception (it's memory)
            throw new RuntimeException(e);
        }
    }

    public static RSAPublicKey parseRSA(String string) {
        PEMReader pemReader = new PEMReader(new StringReader(string));
        try {
            Object publicKey = pemReader.readObject();
            return (RSAPublicKey) publicKey;
        } catch (IOException e) {
            // Should not throw IO Exception
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(RSAPublicKey publicKey, String content) throws EncryptException {
        String value = "";
        try {

            AsymmetricKeyParameter asymmetricKeyParameter = new RSAKeyParameters(
                    false,
                    publicKey.getModulus(),
                    publicKey.getPublicExponent()
            );

            AsymmetricBlockCipher cipher = new RSAEngine();
            cipher = new PKCS1Encoding(cipher);
            cipher.init(true, asymmetricKeyParameter);

            byte[] messageBytes = content.getBytes();
            int i = 0;
            int len = cipher.getInputBlockSize();
            while (i < messageBytes.length)
            {
                if (i + len > messageBytes.length)
                    len = messageBytes.length - i;

                byte[] hexEncodedCipher = cipher.processBlock(messageBytes, i, len);
                value = value + Hex.encodeHexString(hexEncodedCipher);
                i += cipher.getInputBlockSize();
            }

            return value;
        } catch (InvalidCipherTextException e) {
            throw new EncryptException(e);
        }
    }

    public static RSAPrivateKey parseRSAPrivate(String string) {
        PEMReader pemReader = new PEMReader(new StringReader(string));
        try {
            Object publicKey = pemReader.readObject();
            return (RSAPrivateKey) ((KeyPair) publicKey).getPrivate();
        } catch (IOException e) {
            // Should not throw IO Exception
            throw new RuntimeException(e);
        }
    }

    public static class EncryptException extends Exception {
        public EncryptException(Throwable e) {
            super(e);
        }
    }
}
