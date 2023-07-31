package com.absolute.changetracker.sdk;

import com.absolute.changetracker.sdk.core.*;
import com.absolute.changetracker.sdk.models.Row;
import com.absolute.changetracker.sdk.models.StorageResponse;
import com.absolute.changetracker.sdk.models.Table;

import java.util.List;

public class ChangeTrackerService {
    private ChangeStorage storage;
    private ChangeTrackerCalculator calculator;
    private final String apiSecretGet;
    private final String apiSecretPost;
    private int duration;

    public ChangeTrackerService(String hostName, String apiSecretGet, String apiSecretPost) {
        this.apiSecretGet = apiSecretGet;
        this.apiSecretPost = apiSecretPost;
        this.setStorage(new RemoteClient(hostName));
        this.setCalculator(new StandardChangeTrackerCalculator());
    }

    public ChangeTrackerService(String hostName, String apiSecretGet, String apiSecretPost, int tokenDuration) {
        this(hostName, apiSecretGet, apiSecretPost);
        this.duration = tokenDuration;
    }

    public ChangeStorage getStorage() {
        return storage;
    }

    public void setStorage(ChangeStorage storage) {
        this.storage = storage;
    }

    public ChangeTrackerCalculator getCalculator() {
        return calculator;
    }

    public void setCalculator(ChangeTrackerCalculator calculator) {
        this.calculator = calculator;
    }

    /**
     * store - stores data on change tracker
     * @param tableName - the changed table name
     * @param userName - the name of the user who perform the modification (for log purposes)
     * @param rowDescription - log notes
     * @param prevModel - model before change
     * @param nextModel - model after change
     **/
    public StorageResponse store(String tableName, String userName, String rowDescription, Row prevModel, Row nextModel) {
        return store(tableName, userName, rowDescription, prevModel, nextModel, "");
    }
    /**
     * store - stores data on change tracker
     * @param tableName - the changed table name
     * @param userName - the name of the user who perform the modification (for log purposes)
     * @param rowDescription - log notes
     * @param prevModel - model before change
     * @param nextModel - model after change
     * @param ipAddress - current request ip address (for log purposes)
     **/
    public StorageResponse store(String tableName, String userName, String rowDescription, Row prevModel, Row nextModel, String ipAddress) {
        var token = TokenGenerator.generateToken(this.apiSecretPost, tableName, null, this.duration);

        var row = calculator.diff(prevModel, nextModel);

        if (row == null)
            return new StorageResponse(false, "ChangeTracker, diff: missing or invalid diff models");

        row.setDesc(rowDescription);

        var table = Table.createTable(List.of(row), tableName, userName, ipAddress);

        if (table == null)
            return new StorageResponse(false, "ChangeTracker, createTable: invalid rows model");

        return storage.storeChanges(token, table);
    }

    /**
     * getToken - create a jwtToken for retrieving changeTracker records
     * @param tableName - the name of the accessed table
     * @param rowKey - the key of the accessed record
     **/
    public String getToken(String tableName, String rowKey) {
        return TokenGenerator.generateToken(this.apiSecretGet, tableName, rowKey, this.duration);
    }

    /**
     * getToken - create a jwtToken for retrieving changeTracker records
     * @param tableName - the name of the accessed table
     * @param rowKey - the key of the accessed record
     * @param duration - the token duration in minutes
     **/
    public String getToken(String tableName, String rowKey, int duration) {
        return TokenGenerator.generateToken(this.apiSecretGet, tableName, rowKey, duration);
    }
}
