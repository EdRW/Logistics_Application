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
package shortestpathprocessor;

import facilitymanager.FacilityManager;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
/**
 *
 * @author Camille Rose and Edmund Wright
 */
public class ShortestPathProcessor {
    private static ShortestPathProcessor instance;
    
    // this is used in mapPairs to map the pairs whoo
    private HashMap<String,Integer> pairs;
    // seen is used to fill up mapPairs
    private HashSet<String> seen;
    private ArrayList<String> lowPath;
            
            
    private ShortestPathProcessor () {
        pairs = new HashMap<>();
        seen = new HashSet<>();
        lowPath = new ArrayList<>();
        //System.out.println("This is a test of the ShortestPathProcessor");
    }
    
    public static ShortestPathProcessor getInstance() {
        if(instance == null) {
            instance = new ShortestPathProcessor();
        }
        return instance;
    }
    
    public ArrayList<String> findBestPath(String start, String end) {
        // TODO parameters are start,end
        //System.out.println("mapPairs begins");
        mapPairs(start);
        //System.out.println("mapPairs ends");
        
        ArrayList<String> pathList = new ArrayList<>();
        // add start to ArrayList
        pathList.add(start);
        //System.out.println("findPaths begins");
        lowPath.clear();
        findPaths(start, end, pathList);
        //System.out.println("findPaths ends");
        //System.out.println("lowPath = " + lowPath);
        return lowPath;
        
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
            String newPair =  ShortestPathHelper.concatenateStringHelper(start,neighborName);
            pairs.put(newPair,neighbors.get(neighborName));
            if (seen.contains(neighborName) == false) {
                mapPairs(neighborName);
            }
        }
        // start + name of neighbor , distance   
    }
    
    private void findPaths(String start, String end, ArrayList<String> pathList) {
        // pass start, end, and pathList
        // there is a special case for when start equals end
        if (start.equals(end)) {
            if (lowPath.isEmpty()) {
                lowPath = pathList;
            }
            else {
                // sum the length of miles of "pathList"
                // get distance from pairs
                // if sum < length of lowPath  
                int pathListDistance = sumPath(pathList);
                int lowPathDistance = sumPath(lowPath);
                if (pathListDistance < lowPathDistance) {
                    lowPath = pathList;
                }
            }
        } 
        else {
            HashSet<String> fromHere = new HashSet<>();
            // for each pairing in "pairs" HashSet
            for (String pair : pairs.keySet()) {
                 // does the first node in the pair equal start?
                    // if yes, add the pair to the fromHere HashSet
                if (ShortestPathHelper.getFirstNodeHelper(pair).equals(start)) {
                    fromHere.add(pair);
                }
                // for each pairing in from Here HashSet
            }

            for (String pairing : fromHere) {
                // does pathList contain the second node in the pair?
                if (!pathList.contains(ShortestPathHelper.getSecondNodeHelper(pairing))) {
                    // if no, ArrayList<String> = a copy of pathList
                    ArrayList<String> newPath = new ArrayList<>(pathList);
                    newPath.add(ShortestPathHelper.getSecondNodeHelper(pairing));
                     // add second node in current pair to newPath
                     findPaths(ShortestPathHelper.getSecondNodeHelper(pairing),end, newPath);


                }
            }


// recursive call findPaths on second node in pair, end, newPath
        }
        // fill fromHere with pairs containing the starting facility
        // recursive call
    }
    
    private int sumPath(ArrayList<String> pathList) {
        //System.out.println(pathList);
        int sum = 0;
        for (int i=0; i < (pathList.size()-1); i++) {
            int distance = getPairDistance(pathList.get(i), pathList.get(i+1));
            sum += distance;
        }
        return sum;
    }
    
    private int getPairDistance(String cityOne, String cityTwo) {
        String pairKey = ShortestPathHelper.concatenateStringHelper(cityOne,cityTwo);
        //System.out.println(pairKey);
        //System.out.println(pairs.get(pairKey));
        //System.out.println(pairs);
        return pairs.get(pairKey);
    }
    
    public void shortestPathTests() {
        HashMap<String, String> testCities = new HashMap<>();
        testCities.put("a",ShortestPathHelper.concatenateStringHelper("Santa Fe, NM", "Chicago, IL"));
        testCities.put("b",ShortestPathHelper.concatenateStringHelper("Atlanta, GA", "St. Louis, MO"));
        testCities.put("c",ShortestPathHelper.concatenateStringHelper("Seattle, WA", "Nashville, TN"));
        testCities.put("d",ShortestPathHelper.concatenateStringHelper("New York City, NY", "Phoenix, AZ"));
        testCities.put("e",ShortestPathHelper.concatenateStringHelper("Fargo, ND", "Austin, TX"));
        testCities.put("f",ShortestPathHelper.concatenateStringHelper("Denver, CO", "Miami, FL"));
        testCities.put("g",ShortestPathHelper.concatenateStringHelper("Austin, TX", "Norfolk, VA"));
        testCities.put("h",ShortestPathHelper.concatenateStringHelper("Miami, FL", "Seattle, WA"));
        testCities.put("i",ShortestPathHelper.concatenateStringHelper("Los Angeles, CA", "Chicago, IL"));
        testCities.put("j",ShortestPathHelper.concatenateStringHelper("Detroit, MI", "Nashville, TN"));
        
        System.out.println("Shortest Path Tests:");
        
        for (String key : testCities.keySet()){
            String start = ShortestPathHelper.getFirstNodeHelper(testCities.get(key));
            String end = ShortestPathHelper.getSecondNodeHelper(testCities.get(key));
            ArrayList<String> bestPath = findBestPath(start,end);
            int totalDistance = sumPath(bestPath);
            System.out.printf("%s) %s to %s:\n", key, start, end);
            for (int i = 0; i < (bestPath.size()-1); i++) {
                System.out.printf("%s  ", bestPath.get(i));   
            }
            System.out.printf("%s = %d mi\n", end, totalDistance);
            System.out.printf("%d mi / (8 hours per day * 50 mph) = %.2f days\n\n", totalDistance, (totalDistance/400.00f));
        }

        
        
    }
    
}
