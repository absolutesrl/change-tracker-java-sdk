package it.changetracker.sdk;

import it.changetracker.sdk.core.RemoteClient;
import it.changetracker.sdk.core.TokenGenerator;
import it.changetracker.sdk.models.Row;
import it.changetracker.sdk.models.Table;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RemoteClientTest {
    @Test
    void getChangesIntegrationTest() {
        var secretGet = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        var tableName = "testTable";
        var remoteClient = new RemoteClient("hostname");
        var token = TokenGenerator.generateToken(secretGet, tableName, null);
        var response = remoteClient.getChanges(token, tableName);

        assertNotNull(response);
        assertTrue(response.isOk());
    }

    @Test
    void postChangesIntegrationTest() {

        var hostName = "hostname"; //hostname
        var secret = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"; //post token
        var tableName = "testTable";
        var rowKey = "testRowKey";

        var token = TokenGenerator.generateToken(secret, tableName, rowKey);

        assertNotNull(token);

        var remoteClient = new RemoteClient(hostName);

        var response = remoteClient.storeChanges(token, Table.createTable(List.of(new Row("TESTROWKEY")), "TESTTABLE", "TESTUSER", "127.0.0.1"));

        //tlnIIZOPrP6txyqAxqe3RHpVTy661nXA
        assertNotNull(response);
        assertTrue(response.isOk());
    }
}
