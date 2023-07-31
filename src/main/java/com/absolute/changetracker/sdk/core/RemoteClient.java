package com.absolute.changetracker.sdk.core;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.HttpsURLConnection;

import com.absolute.changetracker.sdk.models.GetStorageResponse;
import com.absolute.changetracker.sdk.models.StorageResponse;
import com.absolute.changetracker.sdk.models.Table;
import com.absolute.changetracker.sdk.utils.StringHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * RemoteClient - Store and retrieve data from ChangeTracker's remote host
 **/
public class RemoteClient implements ChangeStorage {

    private final String storageUrl;
    private final ObjectMapper objectMapper;

    public RemoteClient(String hostName) {
        this.storageUrl = String.format("https://%s.hosts.changetracker.it", hostName);
        this.objectMapper = new ObjectMapper();
    }

    /**
     * storeChanges - perform POST request to host
     * @param token - JWT post token for authentication
     * @param table - the table model
     **/
    @Override
    public StorageResponse storeChanges(String token, Table table) {
        try {
            var https_url = storageUrl
                    + "?tableName=" + URLEncoder.encode(table.getName(), StandardCharsets.UTF_8)
                    + "&token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
            var url = URI.create(https_url).toURL();
            var connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            //serialize request
            var jsonRequestBody = "TODO";

            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(jsonRequestBody);
            wr.flush();
            wr.close();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                var json = objectMapper.readValue(url, StorageResponse.class);
                return json;
            } else {
                return new StorageResponse(false, "ChangeTracker Store: response error");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new GetStorageResponse(false, e.getMessage());
        }
    }

    /**
     * getChanges - perform GET request to host
     * @param token - JWT get token for authentication
     * @param tableName - the table name
     **/
    @Override
    public GetStorageResponse getChanges(String token, String tableName) {
        return getChanges(token, tableName, "", "", true);
    }

    /**
     * getChanges - perform GET request to host
     * @param token - JWT get token for authentication
     * @param tableName - the table name
     * @param rowKey - the row key
     **/
    @Override
    public GetStorageResponse getChanges(String token, String tableName, String rowKey) {
        return getChanges(token, tableName, rowKey, "", true);
    }

    /**
     * getChanges - perform GET request to host
     * @param token - JWT get token for authentication
     * @param tableName - the table name
     * @param rowKey - the row key
     * @param paginationToken - the page index
     **/
    @Override
    public GetStorageResponse getChanges(String token, String tableName, String rowKey, String paginationToken) {
        return getChanges(token, tableName, rowKey, paginationToken, true);
    }

    /**
     * getChanges - perform GET request to host
     * @param token - JWT get token for authentication
     * @param tableName - the table name
     * @param rowKey - the row key
     * @param paginationToken - the page index
     * @param backwardSearch - revert sort direction (default: DESC, recent rows comes first)
     **/
    @Override
    public GetStorageResponse getChanges(String token, String tableName, String rowKey, String paginationToken, boolean backwardSearch) {
        try {

            var https_url = storageUrl + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8) +
                    (!StringHelper.isNullOrEmpty(tableName) ? "&tableName=" + tableName : "") +
                    (!StringHelper.isNullOrEmpty(rowKey) ? "&rowKey=" + rowKey : "") +
                    (!StringHelper.isNullOrEmpty(paginationToken) ? "&paginationToken=" + paginationToken : "") +
                    (!backwardSearch ? "&backwardSearch=false" : "");

            var url = URI.create(https_url).toURL();
            var connection = (HttpsURLConnection) url.openConnection();

            return (GetStorageResponse) connection.getContent(new Class[]{GetStorageResponse.class, StorageResponse.class});

        } catch (IOException e) {
            e.printStackTrace();
            return new GetStorageResponse(false, e.getMessage());
        }
    }
}