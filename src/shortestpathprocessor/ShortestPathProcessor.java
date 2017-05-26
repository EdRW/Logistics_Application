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

import customexceptions.CityNotFoundException;
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
    
    private final HashMap<String,Integer> pairs;
    private final HashSet<String> seen;
    private ArrayList<String> lowPath;
            
            
    private ShortestPathProcessor () {
        pairs = new HashMap<>();
        seen = new HashSet<>();
        lowPath = new ArrayList<>();
    }
    
    public static ShortestPathProcessor getInstance() {
        if(instance == null) {
            instance = new ShortestPathProcessor();
        }
        return instance;
    }
    
    public int bestPathTravelTime (String start, String end) throws CityNotFoundException {
        //TODO make this round up in days
        return Math.ceil(sumPath(findBestPath(start, end))/400.0d)
    }
    
    private ArrayList<String> findBestPath(String start, String end) throws CityNotFoundException {
        if (!FacilityManager.getInstance().facilityExists(start)) {
            throw new CityNotFoundException("Invalid city name: " + start);
        }
        if (!FacilityManager.getInstance().facilityExists(end)) {
            throw new CityNotFoundException("Invalid city name: " + end);
        }

        mapPairs(start);
        
        ArrayList<String> pathList = new ArrayList<>();

        pathList.add(start);

        lowPath.clear();
        findPaths(start, end, pathList);

        return lowPath;
        
    }
    
    private void mapPairs(String start) {
        seen.add(start);
        
        FacilityManager facilityManager = FacilityManager.getInstance();
        
        HashMap<String, Integer> neighbors = facilityManager.getNeighbors(start);
        
        for (String neighborName : neighbors.keySet()) {
            String newPair =  concatenateStringHelper(start,neighborName);
            pairs.put(newPair,neighbors.get(neighborName));
            if (seen.contains(neighborName) == false) {
                mapPairs(neighborName);
            }
        }

    }
    
    private void findPaths(String start, String end, ArrayList<String> pathList) {
        if (start.equals(end)) {
            if (lowPath.isEmpty()) {
                lowPath = pathList;
            }
            else { 
                int pathListDistance = sumPath(pathList);
                int lowPathDistance = sumPath(lowPath);
                if (pathListDistance < lowPathDistance) {
                    lowPath = pathList;
                }
            }
        } 
        else {
            HashSet<String> fromHere = new HashSet<>();

            for (String pair : pairs.keySet()) {
                if (getFirstNodeHelper(pair).equals(start)) {
                    fromHere.add(pair);
                }
            }

            for (String pairing : fromHere) {
                if (!pathList.contains(getSecondNodeHelper(pairing))) {
                    ArrayList<String> newPath = new ArrayList<>(pathList);
                    newPath.add(getSecondNodeHelper(pairing));
                     findPaths(getSecondNodeHelper(pairing),end, newPath);
                }
            }
        }
    }
    
    private int sumPath(ArrayList<String> pathList) {
        int sum = 0;
        for (int i=0; i < (pathList.size()-1); i++) {
            int distance = getPairDistance(pathList.get(i), pathList.get(i+1));
            sum += distance;
        }
        return sum;
    }
    
    private int getPairDistance(String cityOne, String cityTwo) {
        String pairKey = concatenateStringHelper(cityOne,cityTwo);
        
        return pairs.get(pairKey);
    }
    
    private String concatenateStringHelper(String start, String neighborName) {
        return start.concat(";".concat(neighborName));
    }
    
    private String getFirstNodeHelper(String combinedName) {
        String[] splitStringArrayList = combinedName.split(";");
        return splitStringArrayList[0];
    }
    
    private String getSecondNodeHelper(String combinedName) {
        String[] splitStringArrayList = combinedName.split(";");
        return splitStringArrayList[1];
    }
    
    public void shortestPathTests() {
        HashMap<String, String> testCities = new HashMap<>();
        testCities.put("a",concatenateStringHelper("Santa Fe, NM", "Chicago, IL"));
        testCities.put("b",concatenateStringHelper("Atlanta, GA", "St. Louis, MO"));
        testCities.put("c",concatenateStringHelper("Seattle, WA", "Nashville, TN"));
        testCities.put("d",concatenateStringHelper("New York City, NY", "Phoenix, AZ"));
        testCities.put("e",concatenateStringHelper("Fargo, ND", "Austin, TX"));
        testCities.put("f",concatenateStringHelper("Denver, CO", "Miami, FL"));
        testCities.put("g",concatenateStringHelper("Austin, TX", "Norfolk, VA"));
        testCities.put("h",concatenateStringHelper("Miami, FL", "Seattle, WA"));
        testCities.put("i",concatenateStringHelper("Los Angeles, CA", "Chicago, IL"));
        testCities.put("j",concatenateStringHelper("Detroit, MI", "Nashville, TN"));
        
        System.out.println("Shortest Path Tests:");
        
        for (String key : testCities.keySet()){
            String start = getFirstNodeHelper(testCities.get(key));
            String end = getSecondNodeHelper(testCities.get(key));
            try {
                ArrayList<String> bestPath = findBestPath(start,end);

                int totalDistance = sumPath(bestPath);
                System.out.printf("%s) %s to %s:\n", key, start, end);
                for (int i = 0; i < (bestPath.size()-1); i++) {
                    System.out.printf("%s  ", bestPath.get(i));   
                }
                System.out.printf("%s = %d mi\n", end, totalDistance);
                System.out.printf("%d mi / (8 hours per day * 50 mph) = %.2f days\n\n", totalDistance, (totalDistance/400.00f));
            } catch (CityNotFoundException e) {
                e.printStackTrace();
            }
        }     
    }
}
