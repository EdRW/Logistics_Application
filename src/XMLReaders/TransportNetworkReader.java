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

import facilityinterface.Facility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
public class TransportNetworkReader {
    
     /*
     * TODO This is a temporary method that should be reworked
     * to load XML and return a list of facility objects
     */
    public static void loadPrint(){
        try {
            String fileName = "src\\XMLReaders\\TransportNetwork.xml";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            File xml = new File(fileName);
            if (!xml.exists()) {
                System.err.println("**** XML File '" + fileName + "' cannot be found");
                System.exit(-1);
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
                    System.err.println("Unexpected node found: " + entryName);
                    return;
                }
                
                // Get named nodes
                Element elem = (Element) facilityEntries.item(i);
                String facilityName = elem.getElementsByTagName("Name").item(0).getTextContent();
                String facilityRate = elem.getElementsByTagName("Rate").item(0).getTextContent();
                String facilityCost = elem.getElementsByTagName("Cost").item(0).getTextContent();
                
                // This line is just here so we have something to print while troubleshooting
                ArrayList<String> neighborDescriptions = new ArrayList<>();
                // Get all noded named "Neighbor" - there can be 0 or more
                NodeList neighborList = elem.getElementsByTagName("Neighbor");
                
                for (int j = 0; j < neighborList.getLength(); j++) {
                    if(neighborList.item(j).getNodeType() == Node.TEXT_NODE) {
                        continue;
                    }
                    
                    entryName = neighborList.item(j).getNodeName();
                    if (!entryName.equals("Neighbor")) {
                        System.err.println("Unexpected node found: " + entryName);
                        return;
                    }
                    
                    // Get named nodes
                    elem = (Element) neighborList.item(j);
                    String neighborName = elem.getElementsByTagName("Name").item(0).getTextContent();
                    String neighborDist = elem.getElementsByTagName("Distance").item(0).getTextContent();
                    
                    neighborDescriptions.add("(Name: "+ neighborName + ", Distance: " + neighborDist + ")");
                }
                System.out.println("Facility: " + facilityName + "\nRate: " + facilityRate + "\nCost: " + facilityCost + "\nNeighbors: " + neighborDescriptions + "\n");
            }
            

        }catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            e.printStackTrace();
        }
    }
    
}
