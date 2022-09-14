package com.midokura.restoranttask;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        SeatingManager seatingManager = new SeatingManager(List.of(new Table(6), new Table(4), new Table(2)));
        CustomerGroup group2 = new CustomerGroup(2);
        CustomerGroup group3 = new CustomerGroup(3);
        CustomerGroup group4 = new CustomerGroup(4);
        CustomerGroup group5 = new CustomerGroup(5);
        CustomerGroup group22 = new CustomerGroup(2);
        CustomerGroup group42 = new CustomerGroup(4);
        seatingManager.arrives(group2);
        seatingManager.arrives(group3);
        seatingManager.arrives(group4);

        seatingManager.arrives(group5);
        seatingManager.arrives(group22);
        seatingManager.arrives(group42);

        seatingManager.leaves(group2);
        seatingManager.leaves(group42);
        CustomerGroup group23 = new CustomerGroup(2);
        seatingManager.arrives(group23);
        seatingManager.leaves(group3);
        seatingManager.leaves(group4);
        seatingManager.leaves(group5);
    }
}
