package com.dmt.bankingapp.service.interfaceClass;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

public interface DetailsOfLoggedClient {
    public String getNameFromClient(HttpServletRequest request);
}
