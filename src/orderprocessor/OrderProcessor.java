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
    // TODO delete as this may not be necessary
//    private boolean checkItemsExist(OrderDTO order) {
//        boolean returnVal = true;
//        for (String item : order.itemInfo.keySet()) {
//            if (!ItemCatalog.getInstance().itemExists(item)) {
//                returnVal = false;
//                break;
//            }
//        }
//        return returnVal;
//    }
    
    public void startOrderProcessing() throws CityNotFoundException {
        int orderIndex = 0;
        OrderDTO order;
        while ((order = getOrder(orderIndex)) != null) {
            // TODO remove print statements for troubleshooting
            System.out.println(order.orderID);
            System.out.println(order.orderDay);
            System.out.println(order.orderDestination);
            System.out.println(order.itemInfo + "\n");
            
            // are the keys in the itemInfo also in the itemCatalog?
            for (String item : order.itemInfo.keySet()) {
                if (!ItemCatalog.getInstance().itemExists(item)) {
                    // item does not exist
                    System.out.println(item + " Item was rejected.");
                    continue;
                }   
                else {
                    // item does exist
                    HashMap <String, Integer> facilitiesWithItem = FacilityManager.getInstance().facilitiesWithItem(item);                   
                    
                    if (facilitiesWithItem.containsKey(order.orderDestination)) {
                        facilitiesWithItem.remove(order.orderDestination);
                    }
                    if (facilitiesWithItem.isEmpty()) {
                        //TODO Generate the back order report for remaineder of the quanity of items
                        continue;
                    }

                    // TODO process the item
                    int quantityRemaining = order.itemInfo.get(item);
                    for (String currentFacility : facilitiesWithItem.keySet()) {
                        int quantityNeeded = Math.min(quantityRemaining, facilitiesWithItem.get(currentFacility));
                        
                        int travelTime = ShortestPathProcessor.getInstance().bestPathTravelTime(currentFacility, order.orderDestination);
                        int processingEndDay = FacilityManager.getInstance().processingEndDate(currentFacility, order.orderDay, quantityNeeded);
                        
                        int arrivalDay = travelTime + processingEndDay;
                    }
                    
                }
            }
            orderIndex++;
        }
    }
}
