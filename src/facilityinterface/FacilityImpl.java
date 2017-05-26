/*
 * The MIT License
 *
 * Copyright 2017 Edmund Wright and Camille Rose.
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
package facilityinterface;

import inventory.Inventory;
import java.util.HashMap;
import schedule.Schedule;
import schedule.ScheduleImplFactory;

/**
 *
 * @author Edmund Wright and Camille Rose
 */
public class FacilityImpl implements Facility{
    private final String facilityName;
    private final int facilityRate;
    private final int facilityCost;
    private final HashMap<String, Integer> facilityNeighbors;
    private  Inventory facilityInventory;
    private  Schedule facilitySchedule;
    
    FacilityImpl(String name, int rate, int cost, HashMap<String, Integer> neighbors){
        // TODO add parameter error checking
        facilityName = name;
        facilityRate = rate;
        facilityCost = cost;
        facilityNeighbors = neighbors;
        facilitySchedule = ScheduleImplFactory.build(rate);
    }
     
    @Override
    public void loadInventory(HashMap<String, Integer> inventory){
        if (facilityInventory == null) {
            facilityInventory = new Inventory(inventory);
        }
        else{
            //TODO throw an exception
        }
    }
    
    @Override
    public boolean hasItem (String itemName) {
        return facilityInventory.hasItem(itemName);
    }
    
    @Override
    public int itemQuanity (String itemName) {
        return facilityInventory.requestQuantity(itemName);
    }
    
    @Override
    public int updateSchedule (int orderDay, int qty) {
        return facilitySchedule.reserve(orderDay, qty);
    }
    
    @Override
    public int processingEndDate(int orderDay, int qty){
        return facilitySchedule.processingEndDate(orderDay, qty);
    }
    
    @Override
    public void printReport(){
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println(facilityName);
        System.out.println("-------");
        System.out.println("Rate per Day: " + facilityRate + "\nCost per Day: $"+ facilityCost + "\n");
        
        System.out.println("Direct Links:");
        for (String name : facilityNeighbors.keySet() ){
            System.out.printf(name + " (%.1fd); ", facilityNeighbors.get(name)/450.0f);
        }
        System.out.println("\n");
        
        facilityInventory.printReport();
        facilitySchedule.printReport();
        System.out.println("----------------------------------------------------------------------------------");
    }

    @Override
    public HashMap<String, Integer> getNeighbors() {
        return new HashMap<>(facilityNeighbors);
    }
}
