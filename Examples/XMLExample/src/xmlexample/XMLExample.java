package xmlexample;

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

public class XMLExample {

  public static void main(String[] args) {

        try {
            String fileName = "XMLExampleDoc.xml";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            File xml = new File(fileName);
            if (!xml.exists()) {
                System.err.println("**** XML File '" + fileName + "' cannot be found");
                System.exit(-1);
            }

            Document doc = db.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList storeEntries = doc.getDocumentElement().getChildNodes();

            for (int i = 0; i < storeEntries.getLength(); i++) {
                if (storeEntries.item(i).getNodeType() == Node.TEXT_NODE) {
                    continue;
                }
                
                String entryName = storeEntries.item(i).getNodeName();
                if (!entryName.equals("Store")) {
                    System.err.println("Unexpected node found: " + entryName);
                    return;
                }
                
                // Get a node attribute
                NamedNodeMap aMap = storeEntries.item(i).getAttributes();
                String storeId = aMap.getNamedItem("Id").getNodeValue();

                // Get a named nodes
                Element elem = (Element) storeEntries.item(i);
                String storeName = elem.getElementsByTagName("Name").item(0).getTextContent();
                String storeAddress = elem.getElementsByTagName("Address").item(0).getTextContent();

                // Get all nodes named "Book" - there can be 0 or more
                ArrayList<String> bookDescriptions = new ArrayList<>();
                NodeList bookList = elem.getElementsByTagName("Book");
                for (int j = 0; j < bookList.getLength(); j++) {
                    if (bookList.item(j).getNodeType() == Node.TEXT_NODE) {
                        continue;
                    }

                    entryName = bookList.item(j).getNodeName();
                    if (!entryName.equals("Book")) {
                        System.err.println("Unexpected node found: " + entryName);
                        return;
                    }

                    // Get some named nodes
                    elem = (Element) bookList.item(j);
                    String bookTitle = elem.getElementsByTagName("Title").item(0).getTextContent();
                    String bookAuthor = elem.getElementsByTagName("Author").item(0).getTextContent();
                    String bookDate = elem.getElementsByTagName("Date").item(0).getTextContent();
                    String bookIsbn13 = elem.getElementsByTagName("ISBN13").item(0).getTextContent();
                    // Create a string summary of the book
                    bookDescriptions.add(bookTitle + ", by " + bookAuthor + ", " + bookDate + " [" + bookIsbn13 + "]");
                }

                // Here I would create a Store object using the data I just loaded from the XML
                System.out.println("Store: " + storeName + " [Store: #" + storeId + "], " + storeAddress + "\n" + bookDescriptions + "\n");
                
            }

        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            e.printStackTrace();
        }
    }
    
}
