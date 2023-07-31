package com.absolute.changetracker.sdk;

import com.absolute.changetracker.sdk.models.Row;
import org.junit.jupiter.api.Test;

public class ChangeTrackerTest {
    @Test
    void Store() {
        var hostName = "q8n7ziezwsc7";
        var tableName = "TESTTABLE1";
        var apiSecretGet = "DyawisDerBJ7g0U2bQs1Jgd1GU6wIPbr";
        var apiSecretPost = "Bg0LZQBeczpm0baEWUu94436l8u8QvqY";

        var changeTracker = new ChangeTrackerService(hostName, apiSecretGet, apiSecretPost);

        changeTracker.store(tableName, "TESTUSER", "test description",new Row("prevModel"), new Row("nextModel"), "127.0.0.1");
    }

    @Test
    void Get() {
        var hostName = "q8n7ziezwsc7";
        var tableName = "TESTTABLE1";
        var apiSecretGet = "DyawisDerBJ7g0U2bQs1Jgd1GU6wIPbr";
        var apiSecretPost = "Bg0LZQBeczpm0baEWUu94436l8u8QvqY";

        var changeTracker = new ChangeTrackerService(hostName, apiSecretGet, apiSecretPost);

        changeTracker.getToken(tableName, null);
    }
}
