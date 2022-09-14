package com.midokura.restoranttask;

public class CustomerGroup {

    private final int size; //number of people in the group

    private Table seatedTable;

    public CustomerGroup(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void seat(Table table) {
        this.seatedTable = table;
    }

    public boolean isSeated() {
        return seatedTable != null;
    }

    public Table leaveSeatedTable() {
        Table tableToLeave = this.seatedTable;
        this.seatedTable = null;
        tableToLeave.leave(this);
        return tableToLeave;
    }

    public Table getSeatedTable() {
        return seatedTable;
    }

}