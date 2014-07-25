package com.lyncode.dataencoder.persistence.impl;

import com.lyncode.dataencoder.persistence.spring.PersistenceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class)
public class PublicKeyRepositoryImplTest {
    @ReplaceWithMock
    @Autowired
    private Environment environment;

    @Autowired
    private PublicKeyRepositoryImpl publicKeyRepository;

    @Test
    public void testName() throws Exception {
        publicKeyRepository.create("testa");

        assertNotNull(publicKeyRepository.get("test"));

        System.out.println(publicKeyRepository.get("test").encrypt("joao"));
    }
}