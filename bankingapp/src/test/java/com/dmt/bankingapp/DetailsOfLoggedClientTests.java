package com.dmt.bankingapp;

import com.dmt.bankingapp.service.implementation.DetailsOfLoggedClientImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class DetailsOfLoggedClientTests {

    @Test
    public void testForNameOfUser(){
        //arrange
        DetailsOfLoggedClientImpl detailsOfLoggedClient = new DetailsOfLoggedClientImpl();
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        String username = "TestUser";
        Mockito.when(httpServletRequest.getRemoteUser()).thenReturn(username);
        //act
        String actualUsername = httpServletRequest.getRemoteUser();
        //assert
        assertEquals(username, actualUsername);
    }
}
