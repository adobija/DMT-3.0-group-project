package com.dmt.bankingapp.service.interfaceClass;

import com.dmt.bankingapp.entity.Client;
import jakarta.servlet.http.HttpServletRequest;

public interface DetailsOfLoggedClient {
    public String getNameFromClient(HttpServletRequest request);
    public Client getLoggedClientInstance(HttpServletRequest request);
}
