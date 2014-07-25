package com.lyncode.dataencoder.persistence.impl;

import com.lyncode.dataencoder.crypt.CryptUtils;
import com.lyncode.dataencoder.persistence.api.PublicKeyRepository;
import com.lyncode.dataencoder.persistence.model.NamedKeyPair;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PublicKeyRepositoryImpl implements PublicKeyRepository {
    private static String DIRECTORY = "keys";

    @Autowired
    private Environment environment;

    @Override
    public Collection<NamedKeyPair> findAll() {
        List<NamedKeyPair> namedKeyPairs = new ArrayList<>();
        File[] files = new File(DIRECTORY).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return !name.endsWith(".key");
            }
        });
        if (files != null) {
            for (File file : files) {
                try {
                    namedKeyPairs.add(NamedKeyPair.parse(file));
                } catch (IOException e) {
                    // Don't do anything!
                }
            }
        }
        return namedKeyPairs;
    }

    @Override
    public NamedKeyPair get(String name) {
        try {
            return NamedKeyPair.parse(new File(DIRECTORY, name));
        } catch (IOException e) {
            // Not able to get it
            return null;
        }
    }

    @Override
    public boolean delete(String name) {
        return new File(DIRECTORY, name).delete();
    }

    @Override
    public String create(String name) throws KeyPairCreationException {
        try {
            KeyPair keyPair = CryptUtils.createKeyPair(environment.getProperty("crypt.key.size", Integer.class, 512));
            String result = CryptUtils.convertToString(keyPair.getPrivate());
            FileUtils.writeStringToFile(new File(DIRECTORY, name), CryptUtils.convertToString(keyPair.getPublic()));
            FileUtils.writeStringToFile(new File(DIRECTORY, name + ".key"), result);
            return result;
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new KeyPairCreationException(e);
        }
    }

    @Override
    public void create(String name, String publicKeyPEM) throws KeyPairCreationException {
        try {
            FileUtils.writeStringToFile(new File(DIRECTORY, name), publicKeyPEM);
        } catch (IOException e) {
            throw new KeyPairCreationException(e);
        }
    }
}
