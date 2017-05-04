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
package xmlreaders;

import customexceptions.XMLFileNotFoundException;
import customexceptions.XMLUnexpectedNodeException;
import facilityinterface.Facility;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Edmund Wright and Camille Rose
 */
public class FacilityInventoryReader {

    public static void load(HashMap<String, Facility> facilityNetwork){
        try {



            String fileName = "src\\XMLReaders\\FacilityInventory.xml";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            File xml = new File(fileName);
            if (!xml.exists()) {
                throw new XMLFileNotFoundException("**** XML File '" + fileName + "' cannot be found");
            }

            Document doc = db.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList facilityEntries = doc.getDocumentElement().getChildNodes();

            for (int i = 0; i < facilityEntries.getLength(); i++) {
                if (facilityEntries.item(i).getNodeType() == Node.TEXT_NODE) {
                    continue;
                }

                String entryName = facilityEntries.item(i).getNodeName();
                if (!entryName.equals("Facility")) {
                    throw new XMLUnexpectedNodeException("Unexpected node found: " + entryName);
                }

                // Get named nodes
                Element elem = (Element) facilityEntries.item(i);
                String facilityName = elem.getElementsByTagName("Name").item(0).getTextContent();

                HashMap<String, Integer> inventory = new HashMap<>();

                // Get all noded named "Item" - there can be 0 or more
                NodeList itemList = elem.getElementsByTagName("Item");

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
                    String itemID = elem.getElementsByTagName("ID").item(0).getTextContent();
                    String itemQuant = elem.getElementsByTagName("Quantity").item(0).getTextContent();

                    inventory.put(itemID, Integer.parseInt(itemQuant));
                }
                facilityNetwork.get(facilityName).loadInventory(inventory);
            }    
        } catch (XMLFileNotFoundException | XMLUnexpectedNodeException | ParserConfigurationException | SAXException | IOException | DOMException e) {
            e.printStackTrace();
        }
    } 
}
