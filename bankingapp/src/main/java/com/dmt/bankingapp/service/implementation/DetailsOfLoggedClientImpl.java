package com.dmt.bankingapp.service.implementation;

import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class DetailsOfLoggedClientImpl implements DetailsOfLoggedClient {
    @Override
    public String getNameFromClient(HttpServletRequest request) {
        return request.getRemoteUser();
    }
}
