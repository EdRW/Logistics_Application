/*
 * The MIT License
 *
 * Copyright 2017 Camille Rose and Edmund Wright.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package orderprocessor;

import customexceptions.CityNotFoundException;
import facilitymanager.FacilityManager;
import itemcatalog.ItemCatalog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import orderinterface.OrderDTO;
import ordermanager.OrderManager;
import shortestpathprocessor.ShortestPathProcessor;

/**
 *
 * @author Camille Rose and Edmund Wright
 */
public class OrderProcessor {
    private static OrderProcessor instance;
    
    private OrderProcessor() {
        // TODO stuff
    }
    
    public static OrderProcessor getInstance() {
        if(instance == null) {
            instance = new OrderProcessor();
        }
        return instance;
    }
    
    private OrderDTO getOrder(int orderIndex){
        return OrderManager.getInstance().getOrderDTO(orderIndex);
    }
    
    private void processOrder(String itemName, int orderItemQuantityRemaining, OrderDTO order, ArrayList<FacilityRecord> orderItemSolutionList) {
        
        FacilityManager facilityManager = FacilityManager.getInstance();
        HashMap <String, Integer> facilitiesWithItem = facilityManager.facilitiesWithItem(itemName);                   
        // remove destination city from facilities with item list
        if (facilitiesWithItem.containsKey(order.orderDestination)) {
            facilitiesWithItem.remove(order.orderDestination);
        }

        if (facilitiesWithItem.isEmpty()) {
            //TODO Generate the back order report for remaineder of the quanity of items
            backOrderReport(itemName, orderItemQuantityRemaining);
            return;
        }
        // TODO process the item
        //method call here for ordrProcess
        //ArrayList<FacilityRecord> facilityRecords = facilityRecordGenerator(facilitiesWithItem, order, orderItemQuantityRemaining);
        ArrayList<FacilityRecord> facilityRecords = new ArrayList<>();
        for (String currentFacility : facilitiesWithItem.keySet()) {
            int facilityQty = facilitiesWithItem.get(currentFacility);
            int quantityNeeded = Math.min(orderItemQuantityRemaining, facilityQty);
            System.out.println(currentFacility + " " + order.orderDay+ " " + quantityNeeded);
            try {
                int travelTime = ShortestPathProcessor.getInstance().bestPathTravelTime(currentFacility, order.orderDestination);
                int processingEndDay = facilityManager.processingEndDate(currentFacility, order.orderDay, quantityNeeded);
                double processingNumDays = facilityManager.processingNumDays(currentFacility, order.orderDay, quantityNeeded);

                facilityRecords.add(new FacilityRecord(currentFacility, quantityNeeded, processingNumDays, processingEndDay, travelTime));


            } catch (CityNotFoundException e) {
                e.printStackTrace();

            }


        }
        try {
        //System.out.println("This is the item name: " + itemName + " and Cost: " + ItemCatalog.getInstance().getPrice(itemName));
        } catch (Exception e) {

        }
//                    System.out.println("Unsorted!!!~~~@@@@@@@@@@@@@@@@@@@@@@@@@@");
//                    for (FacilityRecord f : facilityRecords) {
//                        f.print();
//                    }
        //System.out.println("Sorting!!!!~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Collections.sort(facilityRecords);

//        for (FacilityRecord f : facilityRecords) {
//            f.print();
//        }
        for (FacilityRecord facilityRecord : facilityRecords){

            if (orderItemQuantityRemaining > 0){
                //System.out.println("Reducing Inventories, Updating Schedules..." + facilityRecord.facilityName);
                int facilityQty = facilitiesWithItem.get(facilityRecord.facilityName);
                
                facilityRecord.quantityNeeded = Math.min(orderItemQuantityRemaining, facilityQty);
                
                facilityManager.reduceInventory(facilityRecord.facilityName, itemName, facilityRecord.quantityNeeded);
                facilityManager.updateSchedule(facilityRecord.facilityName, order.orderDay, facilityRecord.quantityNeeded);
                
                //facilityManager.printFacilityReport(facilityRecord.facilityName);
                
                orderItemSolutionList.add(facilityRecord);
                
                //System.out.println(facilityRecord.quantityNeeded + " facilityRecord.quantityNeeded");
                //System.out.println(orderItemQuantityRemaining + " before update");
                
                orderItemQuantityRemaining -= facilityRecord.quantityNeeded;
                //facilityRecord.quantityNeeded = Math.min(facilityRecord.quantityNeeded,orderItemQuantityRemaining);
                
                //System.out.println(orderItemQuantityRemaining + " after update");
                //System.out.println(facilityRecord.quantityNeeded + " facilityRecord.quantityNeeded");
            }
            else {
                break;
            }
        }
        if (orderItemQuantityRemaining > 0){
            processOrder(itemName, orderItemQuantityRemaining, order, orderItemSolutionList);
        //TODO Generate the back order report for remainder of the quantity of items
        // figure out how to identify the desired facilities again. recursion?
        }
    }
    
    private void orderSolutionReport(int orderNum, OrderDTO order, HashMap<String, ArrayList<FacilityRecord>> completeOrderSolution) {
        System.out.println("Order #" + (orderNum + 1));
        System.out.println("\tOrder ID: " + order.orderID);
        System.out.println("\tOrder Time: " + order.orderDay);
        System.out.println("\tDestination: " + order.orderDestination);
        System.out.println("\tList of Order Items:\n\t\t" + order.itemInfo + "\n");
        
        System.out.println("\tProcessing Solution:\n");
        for (String itemName : completeOrderSolution.keySet()) {
            System.out.println("\t" + itemName + ":");
            
            for (FacilityRecord facilityRecord : completeOrderSolution.get(itemName)) {
                System.out.println("Facility Name: " + facilityRecord.facilityName);
                System.out.println("Item Qty: " + facilityRecord.quantityNeeded);
                System.out.println();
            }
        }
        
    }
    
    private void backOrderReport(String itemName, int quantity) {
        System.out.println("Back Ordered Item: " + itemName + " :: QTY: " + quantity);
    }
    
    public void startOrderProcessing() {
        int orderIndex = 0;
        OrderDTO order;
//        int i = 0;
        
       // System.out.println("orderIndex: " + orderIndex);
//        while (i < 1) {
            //order = getOrder(orderIndex);
        while ((order = getOrder(orderIndex)) != null) {
            // TODO remove print statements for troubleshooting
//            i++;
//            System.out.println(order.orderID);
//            System.out.println(order.orderDay);
//            System.out.println(order.orderDestination);
//            System.out.println(order.itemInfo + "\n");
            //Order hashmap<itemName, solution> here
            HashMap<String, ArrayList<FacilityRecord>> completeOrderSolution = new HashMap<>();
            // are the keys in the itemInfo also in the itemCatalog?
            for (String itemName : order.itemInfo.keySet()) {
                if (!ItemCatalog.getInstance().itemExists(itemName)) {
                    // item does not exist
                    System.out.println(itemName + " Item was rejected.");
                    continue;
                }   
                else {
                    // item does exist
                    ArrayList<FacilityRecord> orderItemSolutionList = new ArrayList<>();
                    //
                    processOrder(itemName, order.itemInfo.get(itemName), order, orderItemSolutionList);
                    
                    //order hashmap.add(orderItemSolutionList);
                    completeOrderSolution.put(itemName, orderItemSolutionList);
                
                }
                // TODO   generate logistics record for the Order Items with the facility the inventory was taken from, 
                /* the qtyNeeded from the facility record, calculate the total cost, and include the arrival day
                all totaled Ex. TOTAL 180 $1898 11
                */
            }
            //TODO Here is here we print the solution report
            //print solutionReport
            orderSolutionReport(orderIndex, order, completeOrderSolution);
            
            orderIndex++;
            //System.out.println("orderIndex: " + orderIndex);
        }
//        for (FacilityRecord facilityRecord : orderItemSolutionList) {
//            //facilityRecord.print();
//        }
    }
}
