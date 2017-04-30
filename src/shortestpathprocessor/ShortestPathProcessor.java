/*
 * The MIT License
 *
 * Copyright 2017 Camille.
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
package shortestpathprocessor;

import facilitymanager.FacilityManager;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
/**
 *
 * @author Camille
 */
public class ShortestPathProcessor {
    public static ShortestPathProcessor instance = new ShortestPathProcessor();
    
    // this is used in mapPairs to map the pairs whoo
    HashMap<String,Integer> pairs = new HashMap<>();
    // seen is used to fill up mapPairs
    HashSet seen = new HashSet();
    ArrayList lowPath = new ArrayList();
            
            
    private ShortestPathProcessor () {
        
        
        System.out.println("This is a test of the ShortestPathProcessor");
    }
    
    public static ShortestPathProcessor getInstance() {
        if(instance == null) {
            instance = new ShortestPathProcessor();
        }
        return instance;
    }
    
    public void findBestPath(String start, String end) {
        // TODO parameters are start,end
        mapPairs(start);
        
        //ArrayList pathList = new ArrayList();
        // add start to ArrayList
    }
    
    private void mapPairs(String start) {
        // takes the start node (a string with a name of city)
        // add "start" to seen HashSet
        // pairs add getNeighbors(start) with each neighbor and distance
        // use concatenation of start with string of hashmap for getNeighbors by alphabetical order
        // getNeighbors returns a hashmap of String,Integer (cityName and distance)
        // for loop
        seen.add(start);
        FacilityManager facilityManager = FacilityManager.getInstance();
        HashMap<String, Integer> neighbors = facilityManager.getNeighbors(start);
        for (String neighborName : neighbors.keySet()) {
            // decide which string comes first-
            String newPair = (start.compareTo(neighborName) == -1) ? start.concat(neighborName) : neighborName.concat(start);
            pairs.put(newPair,neighbors.get(neighborName));
            if (seen.contains(neighborName) == false) {
                mapPairs(neighborName);
        }
        }
        // start + name of neighbor , distance
        
    }
    
    private void findPaths(String start, String end, ArrayList pathList) {
        // pass start, end, and pathList
        // there is a special case for when start equals end
        // HashSet fromHere = new HashSet();
        // fill fromHere with pairs containing the starting facility
        // recursive call
    }
}
