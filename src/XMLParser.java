import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
    public static List<Map<String, String>> parseXML(String filePath, String tableName) {
        List<Map<String, String>> data = new ArrayList<>();
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // FIXED: Use dynamic tag name instead of hardcoded "item"
            String recordTag = getRecordTag(tableName);
            NodeList nodeList = doc.getElementsByTagName(recordTag);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Map<String, String> row = new HashMap<>();
                    // IMPROVED: Loop over all child elements to capture fields dynamically
                    NodeList children = element.getChildNodes();
                    for (int j = 0; j < children.getLength(); j++) {
                        Node child = children.item(j);
                        if (child.getNodeType() == Node.ELEMENT_NODE) {
                            Element fieldElement = (Element) child;
                            String tagName = fieldElement.getTagName();
                            String value = (fieldElement.getTextContent() != null) ? fieldElement.getTextContent().trim() : "";
                            row.put(tagName, value);
                        }
                    }
                    data.add(row);
                }
            }
            // DEBUG: Print parsed count (remove after testing)
            System.out.println("Parsed " + data.size() + " records from " + filePath + " for table " + tableName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("XML parsing failed for " + filePath + ": " + e.getMessage());
        }
        return data;
    }

    // NEW: Dynamic record tag based on table
    private static String getRecordTag(String tableName) {
        switch (tableName) {
            case "products":
                return "row";
            case "accounts":
                return "account";
            default:
                throw new IllegalArgumentException("Unknown table: " + tableName);
        }
    }

    private static String[] getFields(String tableName) {
        switch (tableName) {
            case "products":
                return new String[]{"product", "series", "sales_price"};
            case "accounts":
                return new String[]{"account", "sector", "year_established", "revenue", "employees", "office_location", "subsidiary_of"};
            default:
                throw new IllegalArgumentException("Unknown table: " + tableName);
        }
    }
}