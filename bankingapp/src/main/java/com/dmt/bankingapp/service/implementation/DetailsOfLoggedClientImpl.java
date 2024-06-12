package com.dmt.bankingapp.service.implementation;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetailsOfLoggedClientImpl implements DetailsOfLoggedClient {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public String getNameFromClient(HttpServletRequest request) {
        return request.getRemoteUser();
    }

    @Override
    public Client getLoggedClientInstance(HttpServletRequest request) {
        String login = getNameFromClient(request);
        Client client = clientRepository.findByClientName(login);
        return client;
    }
}
