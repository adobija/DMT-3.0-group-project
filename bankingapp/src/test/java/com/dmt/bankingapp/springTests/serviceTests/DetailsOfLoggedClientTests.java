package com.dmt.bankingapp.springTests.serviceTests;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.service.implementation.DetailsOfLoggedClientImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class DetailsOfLoggedClientTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DetailsOfLoggedClientImpl detailsOfLoggedClient;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Test
    public void testForNameOfUser(){
        //arrange
        String username = "TestUser";
        Mockito.when(httpServletRequest.getRemoteUser()).thenReturn(username);
        //act
        String actualUsername = detailsOfLoggedClient.getNameFromClient(httpServletRequest);
        //assert
        assertEquals(username, actualUsername);
    }

    @Test
    public void testForInstanceOfUser(){
        //arrange
        String username = "TestClient";
        Client client = new Client(username, false, "password");
        entityManager.persist(client);

        Mockito.when(httpServletRequest.getRemoteUser()).thenReturn(username);
        //act
        Client foundClient = detailsOfLoggedClient.getLoggedClientInstance(httpServletRequest);
        //assert
        assertEquals(client, foundClient);
    }
}
