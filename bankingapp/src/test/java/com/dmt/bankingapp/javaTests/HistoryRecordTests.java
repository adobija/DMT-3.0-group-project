package com.dmt.bankingapp.javaTests;


import com.dmt.bankingapp.record.History;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HistoryRecordTests {

    @Test
    public void validateHistoryRecord(){
        //arrange
        String typeOf = "Reciving: ";
        String contributorName = "TestGiver";
        String contributorAccountNumber = "testAccountNumber";
        String amount = "+10 zł";
        LocalDateTime dateOfTransaction = LocalDateTime.now();
        Integer transactionID = 1;
        //act
        History history = new History(typeOf, contributorName, contributorAccountNumber, amount, dateOfTransaction, transactionID);
        //assert
        assertEquals(history.typeOf(), typeOf);
        assertEquals(history.contributorName(), contributorName);
        assertEquals(history.contributorAccountNumber(), contributorAccountNumber);
        assertEquals(history.amount(), amount);
        assertEquals(history.dateOfTransaction(), dateOfTransaction);
        assertEquals(history.transactionID(), transactionID);
    }

    @Test
    void testHistoryWithDifferentData() {
        // Arrange
        String typeOf = "Withdrawal";
        String contributorName = "Johny";
        String contributorAccountNumber = "testAccountNumber";
        String amount = "500.00";
        LocalDateTime dateOfTransaction = LocalDateTime.now().minusDays(1);
        Integer transactionID = 2;

        // Act
        History history = new History(typeOf, contributorName, contributorAccountNumber, amount, dateOfTransaction, transactionID);

        // Assert
        assertNotNull(history);
        assertEquals(typeOf, history.typeOf());
        assertEquals(contributorName, history.contributorName());
        assertEquals(contributorAccountNumber, history.contributorAccountNumber());
        assertEquals(amount, history.amount());
        assertEquals(dateOfTransaction, history.dateOfTransaction());
        assertEquals(transactionID, history.transactionID());
    }
}
