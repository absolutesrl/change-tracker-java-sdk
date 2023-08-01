package it.changetracker.sdk.models;

public class StorageResponse {
    private boolean ok;
    private String errorText;

    public StorageResponse() {
    }

    public StorageResponse(boolean ok, String errorText) {
        this.ok = ok;
        this.errorText = errorText;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}
