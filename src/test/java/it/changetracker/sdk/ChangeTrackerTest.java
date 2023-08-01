package it.changetracker.sdk;

import it.changetracker.sdk.models.Row;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeTrackerTest {
    @Test
    void Store() {
        var hostName = "hostname";
        var tableName = "TESTTABLE1";
        var apiSecretGet = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        var apiSecretPost = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

        var changeTracker = new ChangeTrackerService(hostName, apiSecretGet, apiSecretPost);

        var response = changeTracker.store(tableName, "TESTUSER", "test description",new Row("prevModel"), new Row("nextModel"), "127.0.0.1");

        assertNotNull(response);
        assertTrue(response.isOk());
    }

    @Test
    void Get() {
        var hostName = "hostname";
        var tableName = "TESTTABLE1";
        var apiSecretGet = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        var apiSecretPost = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

        var changeTracker = new ChangeTrackerService(hostName, apiSecretGet, apiSecretPost);

        var response = changeTracker.getToken(tableName, null);
        assertNotNull(response);
    }
}
