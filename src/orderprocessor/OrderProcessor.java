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
import customexceptions.ItemNotFoundException;
import facilitymanager.FacilityManager;
import itemcatalog.ItemCatalog;
import java.text.DecimalFormat;
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
    
    private void orderSolutionReport(int orderNum, OrderDTO order, HashMap<String, ArrayList<FacilityRecord>> completeOrderSolution) {
        //TODO add something that prints backorders and rejected items
        FacilityManager facilityManager = FacilityManager.getInstance();
        ItemCatalog itemCatalog = ItemCatalog.getInstance();
        DecimalFormat decimalFormat = new DecimalFormat ("$#,###.00");
        
        String indent1 = "  ";
        String indent2 = indent1 + "     ";
        
        System.out.println("Order #" + (orderNum + 1));
        System.out.printf(indent1 + "%-15s%s\n", "Order ID:", order.orderID);
        System.out.printf(indent1 + "%-15s%s\n", "Order Time:", order.orderDay);
        System.out.printf(indent1 + "%-15s%s\n", "Destination: ", order.orderDestination);
        System.out.println();
        
        System.out.println(indent1 + "List of Order Items:");
        int count = 1;
        for (String itemName : order.itemInfo.keySet()) {
            int qty = order.itemInfo.get(itemName);
            System.out.printf(indent2 + "%d)%9s%10s,\tQuantity: %-5s\n", count, "Item ID:", itemName, qty);
            count++;
        }
        System.out.println();
        
        float totalCost = 0;
        System.out.println(indent1 + "Processing Solution:\n");
        for (String itemName : completeOrderSolution.keySet()) {
            System.out.println(indent1 + itemName + ":");
            System.out.printf(indent2 + "   %-25s%-15s%-22s%s\n", "Facility", "Quantity", "Cost", "Arrival Day");
            count = 1;
            //System.out.println("Count: " + count);
            double itemCost = 0;
            double recordCost = 0;
            double totalRecordCost = 0;
            int totalRecordQty = 0;
            ArrayList<Integer> arrivalDayList = new ArrayList<>();
            
            if (itemCatalog.itemExists(itemName)) {
                itemCost = itemCatalog.getPrice(itemName);
            }
    
            for (FacilityRecord facilityRecord : completeOrderSolution.get(itemName)) {
                int facilityCost = 0;
                if (facilityManager.facilityExists(facilityRecord.facilityName)) {
                    facilityCost = facilityManager.getFacilityDTO(facilityRecord.facilityName).facilityCost;
                    recordCost = (itemCost * facilityRecord.quantityNeeded) + (facilityRecord.processingNumDays * facilityCost) + (facilityRecord.travelTime * 500);
                    arrivalDayList.add(facilityRecord.arrivalDay);
                    System.out.printf(indent2 + "%d) %-25s%-15d%11s\t\t%d\n", count, facilityRecord.facilityName, facilityRecord.quantityNeeded, decimalFormat.format(recordCost), facilityRecord.arrivalDay);
                }
                else {
                    recordCost = 0;
                    System.out.printf(indent2 + "%d) %-25s%-15d%11s\t\t%s\n", count, facilityRecord.facilityName, facilityRecord.quantityNeeded, "N/A", "N/A");
                }
                
                totalRecordQty += facilityRecord.quantityNeeded;
                totalRecordCost += recordCost;
                totalCost += recordCost;
                
                count++;
            }
            if (arrivalDayList.size() > 1) {
                System.out.printf(indent2 + "   %-25s%-15d%11s\t\t[%d - %d]\n", "TOTAL", totalRecordQty, decimalFormat.format(totalRecordCost), Collections.min(arrivalDayList), Collections.max(arrivalDayList));
            }
            else if (arrivalDayList.size() == 1){
                System.out.printf(indent2 + "   %-25s%-15d%11s\t\t[%d]\n", "TOTAL", totalRecordQty, decimalFormat.format(totalRecordCost), arrivalDayList.get(0));
            }
            else {
                System.out.printf(indent2 + "   %-25s%-15d%11s\t\t%s\n", "TOTAL", 0, "N/A", "N/A");
            }
            System.out.println();
        }
        System.out.println(indent2 + "Total Cost:\t" + decimalFormat.format(totalCost));
        System.out.println("----------------------------------------------------------------------------------\n");
        
    }
    
    private void backOrderReport(String itemName, int quantity, ArrayList<FacilityRecord> orderItemSolutionList) {
        //System.out.println("Back Ordered Item: " + itemName + " :: QTY: " + quantity);
        orderItemSolutionList.add(new FacilityRecord("*Back Ordered*", quantity, 0, 0, 0));
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
            backOrderReport(itemName, orderItemQuantityRemaining, orderItemSolutionList);
            return;
        }
        // TODO process the item
        //method call here for ordrProcess
        //ArrayList<FacilityRecord> facilityRecords = facilityRecordGenerator(facilitiesWithItem, order, orderItemQuantityRemaining);
        ArrayList<FacilityRecord> facilityRecords = new ArrayList<>();
        for (String currentFacility : facilitiesWithItem.keySet()) {
            int facilityQty = facilitiesWithItem.get(currentFacility);
            int quantityNeeded = Math.min(orderItemQuantityRemaining, facilityQty);
            //System.out.println(currentFacility + " " + order.orderDay+ " " + quantityNeeded);
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
                    //TODO throw exception item does not exist
                    //System.out.println(itemName + " Item was rejected.");
                    ArrayList<FacilityRecord> rejectedRecordList = new ArrayList<>();
                    FacilityRecord rejectedRecord = new FacilityRecord("*DOES NOT EXIST*", 0, 0, 0, 0);
                    rejectedRecordList.add(rejectedRecord);
                    completeOrderSolution.put(itemName, rejectedRecordList);
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
