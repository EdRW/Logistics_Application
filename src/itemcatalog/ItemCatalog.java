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
package itemcatalog;

import xmlreaders.ItemCatalogReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 *
 * @author Edmund Wright and Camille Rose
 */
public class ItemCatalog {
    // This class uses singleton design pattern
    private static ItemCatalog instance = new ItemCatalog();
    // holds list item IDs and prices
    private final HashMap<String, Integer> items;
    
    private ItemCatalog(){
        /*
         * add the Item Catalog reader in here to build
         * ItemCatalog instance object.
         * Or maybe add nothing here. Not sure.
         */
        items = ItemCatalogReader.load();
    }
    
    public static ItemCatalog getInstance() {
        if(instance == null) {
            instance = new ItemCatalog();
        }
        return instance;
    }
    
    public boolean itemExists(String ID) {
        return items.containsKey(ID);
    }
    
    public float getPrice(String ID) {
        return items.get(ID);
    }
    
    public void printReport() {
        System.out.println("---------------------\nItem Catalog Content:\n---------------------");
        Map<String, Integer> sortedItems = new TreeMap<>(items);
        for (String ID : sortedItems.keySet()) {
            //System.out.println(ID + " : " + Float.toString(items.get(ID)));
            System.out.printf("%-10s: $%d\n", ID, sortedItems.get(ID));
        }
        System.out.println("\n");
    }
}
