package com.dmt.bankingapp.service.interfaceClass;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.record.History;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;

public interface AccountsOfUserIntoHistoryList {
    public ResponseEntity<ArrayList<History>> getStoredHistoryByClient(Client client) throws IOException;
}
