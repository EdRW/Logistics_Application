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
package logisticsapplication;

import facilitymanager.FacilityManager;
import itemcatalog.ItemCatalog;
import shortestpathprocessor.ShortestPathProcessor;
//import xmlreaders.FacilityInventoryReader;
//import xmlreaders.TransportNetworkReader;

/**
 *
 * @author Edmund Wright and Camille Rose
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Gets an instance of the Item Catalog singleton
        ItemCatalog ic = ItemCatalog.getInstance();
        
        // Tells the Item Catalog to print a report about every item it has
        ic.printReport();        
        
        // Gets an instance of the Facility Manager singleton
        FacilityManager fm = FacilityManager.getInstance();
        
        /*
         * Tells the Facility Manager to print a report about every facility
         * in the network.
         */
        fm.printReport();
        
        ShortestPathProcessor.getInstance();
    }
    
}
