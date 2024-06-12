package com.dmt.bankingapp.springTests.repositoryTests;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ClientRepositoryTests {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    public void clientRepositoryTestInsertion(){
        //arrange
        Client client = new Client("TestClient",false, "password");
        //act
        Client savedClient = clientRepository.save(client);
        //assert
        assertThat(entityManager.find(Client.class, savedClient.getClientID())).isEqualTo(client);
    }

    @Test
    @Transactional
    public void clientRepositoryTestUpdate(){
        //arrange
        Client client = new Client("TestClient",false, "password");
        entityManager.persist(client);
        String newName = "ClientForTest";
        //act
        client.setClientName(newName);
        clientRepository.save(client);
        //assert
        assertThat(entityManager.find(Client.class, client.getClientID()).getClientName()).isEqualTo(newName);
    }

    @Test
    @Transactional
    public void clientRepositoryTestDelete(){
        //arrange
        Client client = new Client("TestClient",false, "password");
        entityManager.persist(client);
        //act
        clientRepository.delete(client);
        //assert
        assertThat(entityManager.find(Client.class, client.getClientID())).isNull();
    }

    @Test
    @Transactional
    public void clientRepositoryTestFindById(){
        //arrange
        Client client = new Client("TestClient",false, "password");
        entityManager.persist(client);
        //act
        Optional<Client> foundClient = clientRepository.findById(client.getClientID());
        //assert
        assertThat(foundClient)
                .isNotNull()
                .contains(client);
        assertEquals(client.getClientName(), foundClient.get().getClientName());

    }

    @Test
    @Transactional
    public void clientRepositoryTestFindByClientName(){
        //arrange
        String username = "TestClient";
        Client client = new Client(username,false, "password");
        entityManager.persist(client);
        //act
        Client foundClient = clientRepository.findByClientName(username);
        //assert
        assertThat(foundClient).isNotNull();
        assertEquals(client, foundClient);
    }



}
