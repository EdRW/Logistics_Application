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
package ordermanager;

import java.util.ArrayList;
import orderinterface.Order;
import orderinterface.OrderDTO;
import xmlreaders.OrderReader;
/**
 *
 * @author Camille Rose and Edmund Wright
 */
public class OrderManager {
    
    private static OrderManager instance = new OrderManager();
    
    private final ArrayList<Order> orderList;
    
    private OrderManager() {
        orderList = OrderReader.load();
    }
    
    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
    return instance;
    }
    
    public OrderDTO getOrderDTO(int orderIndex) throws Exception {
        if (orderIndex >= orderList.size()) {
            // TODO create a custom exception for this in package custom exceptions
            throw new Exception("Order Index out of range.");
        }
        else {
            return orderList.get(orderIndex).getOrderDTO();
        }
    }
    
    public void printReport() {
        int orderNum = 1;
        
        for (Order order : orderList) {
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("Order #" + orderNum);
            order.printReport();
            orderNum++;
        }
    }
}
