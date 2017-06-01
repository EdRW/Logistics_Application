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
package facilitymanager;

import customexceptions.CityNotFoundException;
import customexceptions.InvalidScheduleDayException;
import customexceptions.ItemNotFoundException;
import customexceptions.ItemQuantityException;
import facilityinterface.Facility;
import facilityinterface.FacilityDTO;
import itemcatalog.ItemCatalog;
import java.util.HashMap;
import xmlreaders.FacilityInventoryReader;
import xmlreaders.TransportNetworkReader;

/**
 *
 * @author Edmund Wright and Camille Rose
 */

public class FacilityManager {
    // This class uses singleton design pattern
    private static FacilityManager instance = new FacilityManager();
    
    private final HashMap<String, Facility> facilityNetwork;
    
    private FacilityManager() {
        facilityNetwork = TransportNetworkReader.load();
        FacilityInventoryReader.load(facilityNetwork);  
    }
    
    public static FacilityManager getInstance() {
        if(instance == null) {
            instance = new FacilityManager();
        }
        return instance;
    }
    
    public boolean facilityExists(String facilityName) {
        return facilityNetwork.containsKey(facilityName);
    }
    
    public HashMap<String, Integer> getNeighbors(String facilityName) throws CityNotFoundException {
        if (!facilityExists(facilityName)){
            throw new CityNotFoundException(facilityName + " is not a valid Facility name");
        }
        return facilityNetwork.get(facilityName).getNeighbors();
    }
    
    public FacilityDTO getFacilityDTO(String facilityName) throws CityNotFoundException {
        if (!facilityExists(facilityName)){
            throw new CityNotFoundException(facilityName + " is not a valid Facility name");
        }
        return facilityNetwork.get(facilityName).getFacilityDTO();
    }
    
    public HashMap<String, Integer> facilitiesWithItem(String itemName) throws ItemNotFoundException {
        if (!ItemCatalog.getInstance().itemExists(itemName)){
            throw new ItemNotFoundException(itemName + "does not exist in Item Catalog");
        }
        HashMap<String, Integer> facilitiesList = new HashMap<>();
        
        for (String facilityName : facilityNetwork.keySet()) {
            Facility facility = facilityNetwork.get(facilityName);
            if (facility.hasItem(itemName) && facility.itemQuanity(itemName) > 0) {
                facilitiesList.put(facilityName, facility.itemQuanity(itemName));
            }
        }
        
        return facilitiesList;
    }
    
    public void reduceInventory(String facilityName, String itemName, int itemQty) throws CityNotFoundException, ItemQuantityException, ItemNotFoundException {
        if (itemQty < 0) {
            throw new ItemQuantityException("The requested quantity: " + itemQty + " is less than zero and cannot be handled.");
        }
        if (!ItemCatalog.getInstance().itemExists(itemName)){
            throw new ItemNotFoundException(itemName + "does not exist in Item Catalog");
        }
        if (!facilityExists(facilityName)){
            throw new CityNotFoundException(facilityName + " is not a valid Facility name");
        }
        facilityNetwork.get(facilityName).reduceInventory(itemName, itemQty);
    }
    
    public int updateSchedule(String facilityName, int orderDay, int qty) throws InvalidScheduleDayException, ItemQuantityException, CityNotFoundException {
        if (qty < 0) {
            throw new ItemQuantityException("The requested quantity: " + qty + " is less than zero and cannot be handled.");
        }
        if (orderDay < 1) {
            throw new InvalidScheduleDayException("Day " + orderDay + " is not a valid schedule day." );
        }
        if (!facilityExists(facilityName)){
            throw new CityNotFoundException(facilityName + " is not a valid Facility name");
        }
        return facilityNetwork.get(facilityName).updateSchedule(orderDay, qty);
    }
    
    public int processingEndDate(String facilityName, int orderDay, int qty)throws InvalidScheduleDayException, ItemQuantityException, CityNotFoundException {
        if (qty < 0) {
            throw new ItemQuantityException("The requested quantity: " + qty + " is less than zero and cannot be handled.");
        }
        if (orderDay < 1) {
            throw new InvalidScheduleDayException("Day " + orderDay + " is not a valid schedule day." );
        }
        if (!facilityExists(facilityName)){
            throw new CityNotFoundException(facilityName + " is not a valid Facility name");
        }
        return facilityNetwork.get(facilityName).processingEndDate(orderDay, qty);
    }
    
    public double processingNumDays(String facilityName, int orderDay, int qty) throws InvalidScheduleDayException, ItemQuantityException, CityNotFoundException {
        if (qty < 0) {
            throw new ItemQuantityException("The requested quantity: " + qty + " is less than zero and cannot be handled.");
        }
        if (orderDay < 1) {
            throw new InvalidScheduleDayException("Day " + orderDay + " is not a valid schedule day." );
        }
        if (!facilityExists(facilityName)){
            throw new CityNotFoundException(facilityName + " is not a valid Facility name");
        }
        return facilityNetwork.get(facilityName).processingNumDays(orderDay, qty);
    }
    
    public void printFacilityReport(String facilityName) throws CityNotFoundException {
        if (!facilityExists(facilityName)){
            throw new CityNotFoundException(facilityName + " is not a valid Facility name");
        }
        facilityNetwork.get(facilityName).printReport();
    }
    public void printReport() {
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("FACILITY STATUS REPORT");
        
        // loop through the hashmap and print each facility's report        
        for (String facilityName : facilityNetwork.keySet() ){
            facilityNetwork.get(facilityName).printReport();
        }
    }
}
