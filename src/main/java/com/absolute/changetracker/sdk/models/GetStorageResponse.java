package com.absolute.changetracker.sdk.models;

import java.util.ArrayList;
import java.util.List;

public class GetStorageResponse extends StorageResponse {
    private String paginationToken;
    private List<Table> changes;

    public String getPaginationToken() {
        return paginationToken;
    }

    public void setPaginationToken(String paginationToken) {
        this.paginationToken = paginationToken;
    }

    public List<Table> getChanges() {
        return changes;
    }

    public void setChanges(List<Table> changes) {
        this.changes = changes;
    }

    public GetStorageResponse() {
        changes = new ArrayList<Table>();
    }

    public GetStorageResponse(boolean ok, String errorText) {
        super(ok, errorText);
        changes = new ArrayList<Table>();
    }
}
