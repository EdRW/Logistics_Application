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
package FacilityManager;

import java.util.ArrayList;

/**
 *
 * @author Edmund Wright and Camille Rose
 */
public class FacilityManager {
    // This class uses singleton design pattern
    private static FacilityManager instance = new FacilityManager();
    
    // TODO create a private hashmap that holds a String with facility name and reference to facility obj
    
    // TODO create a private hashmap that holds pairs of facility names and the distance between them
    
    private FacilityManager() {
        /*
         * add the Transport Network reader in here to build
         * Facility Manager instance object.
         * Or maybe add nothing here. Not sure.
         */
    }
    
    public static FacilityManager getInstance() {
        if(instance == null) {
            instance = new FacilityManager();
        }
        return instance;
    }
    
    public float getDistance(String facilityA, String facilityB) {
        // TODO this method does nothing. Put code here.
        return 0.0f;
    }
    
    public ArrayList<String> getNeighbors(String facility) {
         // TODO this method does nothing. Put code here.
        return new ArrayList<>();
    }
    
    public void printReport() {
        // TODO this method does nothing. Put code here.
        // loop through the hashmap and print each facility's report
    }
}
