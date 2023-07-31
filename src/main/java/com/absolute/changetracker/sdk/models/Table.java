package com.absolute.changetracker.sdk.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Table {
    Date odt;
    String name;

    public Date getOdt() {
        return odt;
    }

    public void setOdt(Date odt) {
        this.odt = odt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    String ip;
    String user;
    List<Row> rows;

    public Table() {
        rows = new ArrayList<>();
    }

    public Table(String name) {
        this();
        this.name = name;
    }

    public Table(String name, List<Row> rows) {
        this(name);
        this.rows = rows;
    }

    public static Table createTable(List<Row> rows, String tableName, String userName, String ipAddress) {
        if (rows == null) {
            System.out.println("ChangeTracker, createTable: invalid rows model");
            return null;
        }

        var model = new Table(tableName, rows);

        model.user = userName;
        model.odt = new Date();
        model.ip = ipAddress;
        model.rows = rows;

        return model;
    }
}
