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
package XMLReaders;

import CustomExceptions.XMLFileNotFoundException;
import CustomExceptions.XMLUnexpectedNodeException;
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
public class ItemCatalogReader {
    
    // Reads the XML file and returns a hashmap of item IDs and prices
    public static HashMap<String, Integer> load() {
        try {
            String fileName = "src\\XMLReaders\\ItemCatalog.xml";
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            File xml = new File(fileName);
            if (!xml.exists()) {
                throw new XMLFileNotFoundException("**** XML File '" + fileName + "' cannot be found");
            }
            
            Document doc = db.parse(xml);
            doc.getDocumentElement().normalize();
            
            NodeList itemEntries = doc.getDocumentElement().getChildNodes();
            
            HashMap<String, Integer> items = new HashMap<>();
            
            for (int i = 0; i < itemEntries.getLength(); i++) {
                if (itemEntries.item(i).getNodeType() == Node.TEXT_NODE) {
                    continue;
                }
                
                String entryName = itemEntries.item(i).getNodeName();
                if (!entryName.equals("Item")) {
                    throw new XMLUnexpectedNodeException("Unexpected node found: " + entryName);
                }
                
                // Get named nodes
                Element elem = (Element) itemEntries.item(i);
                String itemName = elem.getElementsByTagName("ID").item(0).getTextContent();
                String itemPrice = elem.getElementsByTagName("Price").item(0).getTextContent();
                
                items.put(itemName, Integer.parseInt(itemPrice));
                
                //System.out.println("Item Name: " + itemName + " Item Price: $" + itemPrice);
            }
            return items;
        } catch (XMLFileNotFoundException | XMLUnexpectedNodeException | ParserConfigurationException | SAXException | IOException | DOMException e) {
            e.printStackTrace();
            // TODO returning null seems like a bad idea. Not sure what to do...
            return null;
        }
    }
}
