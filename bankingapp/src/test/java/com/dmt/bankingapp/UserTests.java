package com.dmt.bankingapp;

import com.dmt.bankingapp.entity.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.mindrot.jbcrypt.BCrypt;

public class UserTests {

    // Test to verify whether method 'createBcryptHashedPassword' used in 'setUserPassword' works properly
    @Test
    public void testCreateBcryptHashedPassword() {
        // Arrange
        String inputPassword = "password123";
        User user = new User();
        
        // Act
        user.setUserPassword(inputPassword);
        String hashedPassword = user.getUserPassword();
        
        // Assert
        assertThat(hashedPassword).isNotNull();
        assertThat(hashedPassword).startsWith("{bcrypt}");
        String extractedHash = hashedPassword.substring("{bcrypt}".length());
        assertTrue(BCrypt.checkpw(inputPassword, extractedHash));
    }
}
