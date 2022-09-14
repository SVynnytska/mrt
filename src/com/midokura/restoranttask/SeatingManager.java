package com.midokura.restoranttask;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatingManager {

    private static final int MAXIMUM_FREE_SEATS = 6;
    // In both cases I decided to use HashMap as it accessing is done in constant time and in LinkedList as modification is done in constant time.
    private final Map<Integer, LinkedList<Table>> tablesGropedByNumberOfFreeSeats = new HashMap<>();
    private final Map<Integer, LinkedList<CustomerGroup>> waitingQueue = new HashMap<>();

    public SeatingManager(List<Table> tables) {
        for (Table table : tables) {
            putTableInProperGroupBasedOnFreeSeatsNumber(table);
        }
    }

    /* Group arrives and wants to be seated. */
    public void arrives(CustomerGroup group) {
        if (group.getSize() > MAXIMUM_FREE_SEATS) {
            throw new IllegalArgumentException("The group is too big");
        }

        if (waitingQueue.isEmpty()) {
            Table locateTable = locate(group);
            if (locateTable == null) {
                LinkedList<CustomerGroup> groups = new LinkedList<>();
                groups.add(group);
                waitingQueue.put(group.getSize(), groups);
            }
        } else {
            LinkedList<CustomerGroup> waitingCustomerGroups = waitingQueue.get(group.getSize());
            if (waitingCustomerGroups == null || waitingCustomerGroups.isEmpty()) {
                waitingCustomerGroups = new LinkedList<>();
                waitingCustomerGroups.add(group);
                waitingQueue.put(group.getSize(), waitingCustomerGroups);
            }
            waitingCustomerGroups.add(group);
        }
    }

    /* Whether seated or not, the group leaves the restaurant. */
    public void leaves(CustomerGroup group) {
        if (group.isSeated()) {
            Table leftTable = group.leaveSeatedTable();
            removeTableFromOldTablesGroup(leftTable, group);
            putTableInProperGroupBasedOnFreeSeatsNumber(leftTable);

            // this is additional step to locale a group that is waiting, I know that it was not in the task. Better solution is to  send an event when free seats are available.
            locateNextGroupFromWaitingQueue(leftTable);
        } else {
            List<CustomerGroup> customerGroups = waitingQueue.get(group.getSize());
            customerGroups.remove(customerGroups.size() / 2); // here I assume that people from the middle of the queue are bored and leave
        }
    }

    /* Return the table at which the group is seated, or null if
    they are not seated (whether they're waiting or already left). */
    public Table locate(CustomerGroup group) {
        Table tableToSit = findTableWithFeeSeats(group.getSize());
        if (tableToSit == null) {
            return null;
        }
        group.seat(tableToSit);
        tableToSit.seat(group);
        putTableInProperGroupBasedOnFreeSeatsNumber(tableToSit);

        return tableToSit;
    }


    private void locateNextGroupFromWaitingQueue(Table table) {
        if (waitingQueue.isEmpty()) {
            return;
        }
        int freeSeats = table.getFreeChairs();
        CustomerGroup groupToBeSeatNext = findGroupWithSize(freeSeats);
        if (groupToBeSeatNext != null) {
            locate(groupToBeSeatNext);
        }
    }

    private CustomerGroup findGroupWithSize(int size) {
        if (size < 2) { // we assume that people are coming in groups
            return null;
        }
        LinkedList<CustomerGroup> waitingGroupsWithSameSizeAsFreeSits = waitingQueue.get(size);
        if (waitingGroupsWithSameSizeAsFreeSits.isEmpty()) {
            return findGroupWithSize(size - 1);
        } else {
            return waitingGroupsWithSameSizeAsFreeSits.getFirst();

        }
    }

    private void removeTableFromOldTablesGroup(Table table, CustomerGroup group) {
        if (table.getFreeChairs() - group.getSize() != 0) {
            tablesGropedByNumberOfFreeSeats.get(table.getFreeChairs() - group.getSize()).removeFirst();
        }
    }

    private Table findTableWithFeeSeats(int size) {
        LinkedList<Table> tables = tablesGropedByNumberOfFreeSeats.get(size);
        if (tables == null || tables.isEmpty()) {
            if (size == MAXIMUM_FREE_SEATS) {
                return null;
            }

            int nextSize = size + 1;
            LinkedList<CustomerGroup> waitingGroupsWithTheSameSize = waitingQueue.get(nextSize);
            if (waitingGroupsWithTheSameSize == null || waitingGroupsWithTheSameSize.isEmpty()) {
                return findTableWithFeeSeats(nextSize);
            }
            return null;
        } else {
            return tables.removeFirst();
        }
    }

    private void putTableInProperGroupBasedOnFreeSeatsNumber(Table table) {
        if (table.isFullyOccupied()) {
            return;
        }
        LinkedList<Table> groupedTables = tablesGropedByNumberOfFreeSeats.get(table.getFreeChairs());
        if (groupedTables == null) {
            groupedTables = new LinkedList<>();
            groupedTables.add(table);
            tablesGropedByNumberOfFreeSeats.put(table.getFreeChairs(), groupedTables);
        } else {
            groupedTables.add(table);
        }
    }

}
