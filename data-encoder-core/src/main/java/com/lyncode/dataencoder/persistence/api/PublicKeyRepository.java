package com.lyncode.dataencoder.persistence.api;

import com.lyncode.dataencoder.persistence.model.NamedKeyPair;

import java.util.Collection;

public interface PublicKeyRepository {
    Collection<NamedKeyPair> findAll ();
    NamedKeyPair get (String name);
    boolean delete(String name);
    String create (String name) throws KeyPairCreationException;
    void create(String name, String publicKeyAscii) throws KeyPairCreationException;

    public static class KeyPairCreationException extends Exception {
        public KeyPairCreationException(Throwable e) {
            super(e);
        }
    }
}
