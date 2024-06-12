package com.dmt.bankingapp.service.interfaceClass;

import com.dmt.bankingapp.entity.Client;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

public interface DetailsOfLoggedClient {
    public String getNameFromClient(HttpServletRequest request);
    public Client getLoggedClientInstance(HttpServletRequest request);
}
