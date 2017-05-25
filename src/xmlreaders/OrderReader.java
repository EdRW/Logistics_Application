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
package xmlreaders;

import customexceptions.XMLFileNotFoundException;
import customexceptions.XMLUnexpectedNodeException;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import orderinterface.Order;
import java.util.ArrayList;
import java.util.HashMap;
import orderinterface.OrderImplFactory;

/**
 *
 * @author Camille Rose and Edmund Wright
 */
public class OrderReader {
    
   // public static Order[] load(){
    public static ArrayList<Order> load() {
        // for Order[] this is still to be created (order impl class)
        //int orderNum = 1;
        ArrayList<Order> orders = new ArrayList<>();
        
        try {

            String fileName = "src\\XMLReaders\\Orders.xml";
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            File xml = new File(fileName);
            if (!xml.exists()) {
                throw new XMLFileNotFoundException("**** XML File '" + fileName + "' cannot be found");
            }
            
            Document doc = db.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList orderEntries = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < orderEntries.getLength(); i++) {
                if (orderEntries.item(i).getNodeType() == Node.TEXT_NODE) {
                    continue;
                }
                String entryName = orderEntries.item(i).getNodeName();
                if (!entryName.equals("Order")) {
                    throw new XMLUnexpectedNodeException("Unexpected node found: " + entryName);
                }
                // Get named nodes
                Element elem = (Element) orderEntries.item(i);
                String orderID = elem.getElementsByTagName("ID").item(0).getTextContent();
                String orderTime = elem.getElementsByTagName("Time").item(0).getTextContent();
                String orderDestination = elem.getElementsByTagName("Destination").item(0).getTextContent();
                
//                ArrayList<String> fullOrderInfo = new ArrayList<>();

                // Get all noded named "Item" - there can be 0 or more
                NodeList itemList = elem.getElementsByTagName("Item");
                
                HashMap<String, Integer> orderItems = new HashMap<>();
                
                for (int j = 0; j < itemList.getLength(); j++) {
                    if(itemList.item(j).getNodeType() == Node.TEXT_NODE) {
                        continue;
                    }

                    entryName = itemList.item(j).getNodeName();
                    if (!entryName.equals("Item")) {
                        throw new XMLUnexpectedNodeException("Unexpected node found: " + entryName);
                    }

                    // Get named nodes
                    elem = (Element) itemList.item(j);
                    String itemName = elem.getElementsByTagName("Name").item(0).getTextContent();
                    String itemQuantity = elem.getElementsByTagName("QTY").item(0).getTextContent();

                    
                    orderItems.put(itemName, Integer.parseInt(itemQuantity));
//                    fullOrderInfo.add("Item ID: " + itemName + ", Quantity: " + itemQuantity);
                }
                  orders.add(OrderImplFactory.build(orderID, orderTime, orderDestination, orderItems));
                 //   System.out.println("Order #" + orderNum);
                 //   orderNum = orderNum + 1;
//                    System.out.println("Order ID: " + orderID);
//                    System.out.println("Order Time: " + orderTime + "\nDestination: " + orderDestination);
//                    System.out.println("List of Order Items:\n" + fullOrderInfo);

            }    
        } catch (XMLFileNotFoundException | XMLUnexpectedNodeException | ParserConfigurationException | SAXException | IOException | DOMException e) {
            e.printStackTrace();
        }
        return orders;
    }
}