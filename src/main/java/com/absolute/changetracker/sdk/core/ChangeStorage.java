package com.absolute.changetracker.sdk.core;

import com.absolute.changetracker.sdk.models.GetStorageResponse;
import com.absolute.changetracker.sdk.models.StorageResponse;
import com.absolute.changetracker.sdk.models.Table;

/**
 * ChangeStorage - data storage interface
 **/
public interface ChangeStorage {
    /**
     * storeChanges - perform POST request to host
     * @param token - JWT post token for authentication
     * @param table - the table model
     **/
    StorageResponse storeChanges(String token, Table table);

    /**
     * getChanges - perform GET request to host
     * @param token - JWT get token for authentication
     * @param tableName - the table name
     **/
    GetStorageResponse getChanges(String token, String tableName);

    /**
     * getChanges - perform GET request to host
     * @param token - JWT get token for authentication
     * @param tableName - the table name
     * @param rowKey - the row key
     **/
    GetStorageResponse getChanges(String token, String tableName, String rowKey);

    /**
     * getChanges - perform GET request to host
     * @param token - JWT get token for authentication
     * @param tableName - the table name
     * @param rowKey - the row key
     * @param paginationToken - the page index
     **/
    GetStorageResponse getChanges(String token, String tableName, String rowKey, String paginationToken);

    /**
     * getChanges - perform GET request to host
     * @param token - JWT get token for authentication
     * @param tableName - the table name
     * @param rowKey - the row key
     * @param paginationToken - the page index
     * @param backwardSearch - revert sort direction (default: DESC, recent rows comes first)
     **/
    GetStorageResponse getChanges(String token, String tableName, String rowKey, String paginationToken, boolean backwardSearch);
}
