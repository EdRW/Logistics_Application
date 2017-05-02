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
package schedule;

import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author Edmund Wright and Camille Rose
 */
public class ScheduleImpl implements Schedule{
    
    private final HashMap<Integer, Integer> schedule;
    private final int rate;
    
    
    public ScheduleImpl(int rateIn) {
        rate = rateIn;
        schedule = new HashMap<>();
        schedule.put(0, rate);
    }
    
    public void printReport() {
        // from day 0 up the highest number day present in the schedule and then prints 20 days beyond it.      
        int highestDay = Collections.max(schedule.keySet());
        
        
        System.out.println("Schedule:");
        System.out.printf("%-20s","Day:");
        for (int day = 0; day <= (highestDay + 20); day++) {
            System.out.printf("%-3s", Integer.toString(day));
        }
        
        System.out.println();
        
        System.out.printf("%-20s","Available:");
        for (int day = 0; day <= (highestDay + 20); day++) {
            System.out.printf("%-3s", Integer.toString(daysCapacity(day)));
        }
        
        System.out.println();
    }
    
    @Override
    public int daysCapacity(int day) {
        // If the day doesn't exist in the hashmap, then that day is wide open for production
        return (schedule.containsKey(day))? schedule.get(day) : rate;
    }
    
    @Override
   public int earliestAvailability() {
        // Returns the earlier day that production can begin
        int day = 0;
        Integer capacity = schedule.get(day);
        while ((capacity == 0) &&  (capacity != null)) {
            day++;
            capacity = schedule.get(day);
        }
        return day;
    }

    @Override
    public int expectedProductionNumDays (int Qty) {
        int end = earliestAvailability();
        int days = 0;
        while (Qty > 0) {
            int capacity = daysCapacity(end);
            if (capacity != 0){
                Qty -= (Qty >= capacity)? capacity :  Qty;
                days++;
            }
            end++;
        }
        return days;
    }
    
    @Override
    public int expectedProductionEndDate (int Qty) {
        int end = earliestAvailability();
        while (Qty > 0) {
            int capacity = daysCapacity(end);
            if (capacity != 0){
                Qty -= (Qty >= capacity)? capacity :  Qty;
            }
            end++;
        }
        return end;
    }
    
    @Override
    public int reserveSchedule (int Qty) {
        //call earliest available day
        // call expectedProductionNumDays
        int end = earliestAvailability();
        while (Qty > 0) {
            int capacity = daysCapacity(end);
            if (capacity != 0){
                if (Qty >= capacity) {
                    Qty -= capacity;
                    schedule.put(end, 0);
                }
                else {
                    Qty -= Qty;
                    schedule.put(end, capacity - Qty);
                }
            }
            end++;
        }
        return end;
    }
}
