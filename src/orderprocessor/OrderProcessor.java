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
import customexceptions.InvalidScheduleDayException;
import customexceptions.ItemNotFoundException;
import customexceptions.ItemQuantityException;
import customexceptions.OrderNotFoundException;
import facilitymanager.FacilityManager;
import itemcatalog.ItemCatalog;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    }
    
    public static OrderProcessor getInstance() {
        if(instance == null) {
            instance = new OrderProcessor();
        }
        return instance;
    }
    
    private OrderDTO getOrder(int orderIndex) throws OrderNotFoundException{
        return OrderManager.getInstance().getOrderDTO(orderIndex);
    }
    
    private void backOrderReport(String itemName, int quantity, ArrayList<FacilityRecord> orderItemSolutionList) {
        orderItemSolutionList.add(new FacilityRecord("*Back Ordered*", quantity, 0, 0, 0));
    }
    
    private void processOrder(String itemName, int orderItemQuantityRemaining, OrderDTO order, ArrayList<FacilityRecord> orderItemSolutionList) {
        try {
            FacilityManager facilityManager = FacilityManager.getInstance();
            HashMap <String, Integer> facilitiesWithItem = facilityManager.facilitiesWithItem(itemName);

            // remove destination city from facilities with item list
            if (facilitiesWithItem.containsKey(order.orderDestination)) {
                facilitiesWithItem.remove(order.orderDestination);
            }
            // No facilities have the item in stock
            if (facilitiesWithItem.isEmpty()) {
                backOrderReport(itemName, orderItemQuantityRemaining, orderItemSolutionList);
                return;
            }

            ArrayList<FacilityRecord> facilityRecords = new ArrayList<>();
            for (String currentFacility : facilitiesWithItem.keySet()) {
                int facilityQty = facilitiesWithItem.get(currentFacility);
                int quantityNeeded = Math.min(orderItemQuantityRemaining, facilityQty);         
                int travelTime = ShortestPathProcessor.getInstance().bestPathTravelTime(currentFacility, order.orderDestination);
                int processingEndDay = facilityManager.processingEndDate(currentFacility, order.orderDay, quantityNeeded);
                double processingNumDays = facilityManager.processingNumDays(currentFacility, order.orderDay, quantityNeeded);

                facilityRecords.add(new FacilityRecord(currentFacility, quantityNeeded, processingNumDays, processingEndDay, travelTime));
            }

            //sort by shortest travel time
            Collections.sort(facilityRecords);

            FacilityRecord facilityRecord = facilityRecords.get(0);

            int facilityQty = facilitiesWithItem.get(facilityRecord.facilityName);

            facilityRecord.quantityNeeded = Math.min(orderItemQuantityRemaining, facilityQty);
            facilityManager.reduceInventory(facilityRecord.facilityName, itemName, facilityRecord.quantityNeeded);
            facilityRecord.processingNumDays = facilityManager.processingNumDays(facilityRecord.facilityName, order.orderDay, facilityRecord.quantityNeeded);
            facilityRecord.processingEndDay = facilityManager.updateSchedule(facilityRecord.facilityName, order.orderDay, facilityRecord.quantityNeeded);
            facilityRecord.arrivalDay = facilityRecord.travelTime + facilityRecord.processingEndDay;

            orderItemSolutionList.add(facilityRecord);
            orderItemQuantityRemaining -= facilityRecord.quantityNeeded;

            if (orderItemQuantityRemaining > 0){
                processOrder(itemName, orderItemQuantityRemaining, order, orderItemSolutionList);
            }
            
        }catch (ItemNotFoundException | ItemQuantityException | CityNotFoundException | InvalidScheduleDayException e) {
           e.printStackTrace(); 
        }
    }
    
    public void startOrderProcessing() {
        int orderIndex = 0;
        OrderManager ordermanager = OrderManager.getInstance();
        try {
            while (orderIndex < ordermanager.totalNumOrders()) {
                OrderDTO order = ordermanager.getOrderDTO(orderIndex);
                LinkedHashMap<String, ArrayList<FacilityRecord>> completeOrderSolution = new LinkedHashMap<>();

                for (String itemName : order.itemInfo.keySet()) {
                    if (!ItemCatalog.getInstance().itemExists(itemName)) {
                        ArrayList<FacilityRecord> rejectedRecordList = new ArrayList<>();
                        FacilityRecord rejectedRecord = new FacilityRecord("*DOES NOT EXIST*", 0, 0, 0, 0);
                        rejectedRecordList.add(rejectedRecord);
                        completeOrderSolution.put(itemName, rejectedRecordList);
                        continue;
                    }   
                    else {
                        // item does exist
                        ArrayList<FacilityRecord> orderItemSolutionList = new ArrayList<>();

                        processOrder(itemName, order.itemInfo.get(itemName), order, orderItemSolutionList);

                        completeOrderSolution.put(itemName, orderItemSolutionList);
                    }
                }
                orderSolutionReport(orderIndex, order, completeOrderSolution);

                orderIndex++;
            }
        }catch (OrderNotFoundException e){
            e.printStackTrace();
        }
    }
    
    private void orderSolutionReport(int orderNum, OrderDTO order, LinkedHashMap<String, ArrayList<FacilityRecord>> completeOrderSolution) {
        FacilityManager facilityManager = FacilityManager.getInstance();
        ItemCatalog itemCatalog = ItemCatalog.getInstance();
        DecimalFormat decimalFormat = new DecimalFormat ("$#,###.00");
        
        String indent1 = "  ";
        String indent2 = indent1 + "     ";
        
        System.out.println("ORDER REPORT");
        System.out.println("----------------------------------------------------------------------------------");
        
        System.out.println("Order #" + (orderNum + 1));
        System.out.printf(indent1 + "%-15s%s\n", "Order ID:", order.orderID);
        System.out.printf(indent1 + "%-15s%s%d\n", "Order Time:", "Day ", order.orderDay);
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
            
            double itemCost = 0;
            double recordCost = 0;
            double totalRecordCost = 0;
            int totalRecordQty = 0;
            ArrayList<Integer> arrivalDayList = new ArrayList<>();
            
            try {
                itemCost = itemCatalog.getPrice(itemName);
            } catch (ItemNotFoundException e){
                //Nothing to do here
            }
    
            for (FacilityRecord facilityRecord : completeOrderSolution.get(itemName)) {
                int facilityCost = 0;
                if (facilityManager.facilityExists(facilityRecord.facilityName)) {
                    try {
                        facilityCost = facilityManager.getFacilityDTO(facilityRecord.facilityName).facilityCost;
                    }catch (CityNotFoundException e) {
                        //Nothing to do here
                    }
                    recordCost = (itemCost * facilityRecord.quantityNeeded) + (facilityRecord.processingNumDays * facilityCost) + (facilityRecord.travelTime * 500.00);
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
}
