package com.lyncode.dataencoder.persistence.spring;

import com.lyncode.dataencoder.persistence.api.PublicKeyRepository;
import com.lyncode.dataencoder.persistence.impl.PublicKeyRepositoryImpl;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Security;

@Configuration
public class PersistenceConfig {
    @Bean
    public PublicKeyRepository publicKeyRepository () {
        Security.addProvider(new BouncyCastleProvider());
        return new PublicKeyRepositoryImpl();
    }
}
