package it.changetracker.sdk.models;

public enum RowStatus {
    Modified("M"),
    New("N"),
    Deleted("D"),
    Unchanged("U"),
    Unknown("K");

    private final String label;
    @Override
    public String toString() {
        return this.label;
    }
    private RowStatus(String label) {
        this.label = label;
    }
}
