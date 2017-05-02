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
package inventory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Edmund Wright and Camille Rose
 */
public class Inventory {
    
    private final HashMap<String, Integer> inventory;
    
    public Inventory(HashMap<String, Integer> items){
         inventory = items;
    }
    
    public int requestQuantity(String item) {
        return inventory.get(item);
    }
    
    public Boolean hasItem(String item) {
        return (inventory.get(item) > 0);
    }
    
    public int removeItems(String item, Integer requestedQty){
        Integer currQty = inventory.get(item);
        if (requestedQty > currQty) {
            //TODO throw error
        }
        currQty -= requestedQty;
        inventory.put(item, currQty);
        return currQty;
    }
    
    public void printReport() {
        System.out.println("Active Inventory:");
        System.out.printf("\t%-10s %s\n", "Item ID", "Quanity");
        
        ArrayList<String> depleted = new ArrayList<>();
        for (String item : inventory.keySet()) {
            Integer currQty = inventory.get(item);
            System.out.printf("\t%-10s %d\n", item, currQty);
            if (currQty <= 0) {
                depleted.add(item);
            } 
        }
        System.out.println("\nDepleted (Used-Up) Inventory: " + ((depleted.isEmpty())? "None" : depleted)); 
        System.out.println();
    }
}
