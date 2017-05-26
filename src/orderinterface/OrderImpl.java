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
package orderinterface;

import java.util.HashMap;
/**
 *
 * @author Camille Rose and Edmund Wright
 */
public class OrderImpl implements Order {
    private final String orderID;
    private final int orderDay;
    private final String orderDestination;
    private final HashMap<String, Integer> itemInfo;
    
    OrderImpl(String iD, int day, String destination, HashMap<String, Integer> items){
        orderID = iD;
        orderDay = day;
        orderDestination = destination;
        itemInfo = items;
    }
    
    @Override
    public OrderDTO getOrderDTO(){
        // TODO what do?
        return new OrderDTO(orderID, orderDay, orderDestination, new HashMap<>(itemInfo));
    }
    
    
    @Override
    public void printReport() {
        // TODO update to use print format instead of spaces
        System.out.println("     Order ID: " + orderID);
        System.out.println("     Order Time: " + orderDay + "\n     Destination: " + orderDestination + "\n");
        System.out.println("     List of Order Items:");
        int itemNum = 1;
        for (String item : itemInfo.keySet()) {
            System.out.println("     " + itemNum + ") Item ID:     " + item + ",     Quantity: " + itemInfo.get(item));
            itemNum++;
        }
        System.out.println("\n");
    }
}
