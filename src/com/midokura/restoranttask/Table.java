package com.midokura.restoranttask;

public class Table {

    private final int size; //number of chairs around this table
    private int freeChairs;

    public Table(int size) {
        this.size = size;
        this.freeChairs = size;
    }

    public int getFreeChairs() {
        return freeChairs;
    }

    public void leave(CustomerGroup customerGroup) {
        this.setFreeChairs(freeChairs + customerGroup.getSize());
    }

    public void seat(CustomerGroup customerGroup) {
        this.setFreeChairs(freeChairs - customerGroup.getSize());
    }

    public boolean isFullyOccupied() {
        return freeChairs == 0;
    }

    private void setFreeChairs(int freeChairs) {
        this.freeChairs = freeChairs;
    }
}
