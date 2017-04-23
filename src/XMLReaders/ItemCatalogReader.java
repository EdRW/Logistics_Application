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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Edmund Wright and Camille Rose
 */
public class ItemCatalogReader {
    /*
     * TODO This is a temporary method that should be reworked
     * to load XML and returns hashmap
     */
    public static void loadPrint(){
        try {
            String fileName = "src\\XMLReaders\\ItemCatalog.xml";
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            File xml = new File(fileName);
            if (!xml.exists()) {
                System.err.println("**** XML File '" + fileName + "' cannot be found");
                System.exit(-1);
            }
            
            Document doc = db.parse(xml);
            doc.getDocumentElement().normalize();
            
            NodeList itemEntries = doc.getDocumentElement().getChildNodes();
            
            for (int i = 0; i < itemEntries.getLength(); i++) {
                if (itemEntries.item(i).getNodeType() == Node.TEXT_NODE) {
                    continue;
                }
                
                String entryName = itemEntries.item(i).getNodeName();
                if (!entryName.equals("Item")) {
                    System.err.println("Unexpected node found: " + entryName);
                    return;
                }
                
                // Get named nodes
                Element elem = (Element) itemEntries.item(i);
                String itemName = elem.getElementsByTagName("ID").item(0).getTextContent();
                String itemPrice = elem.getElementsByTagName("Price").item(0).getTextContent();
                
                /*
                 * TODO This where we should start filling the ItemCatelog object.
                 * Right now it just prints out each item for troubleshooting.
                 */
                System.out.println("Item Name: " + itemName + " Item Price: $" + itemPrice);
            }
            
        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            e.printStackTrace();
        }
    }
}
