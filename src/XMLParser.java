import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

/**
 * Created by astrg on 15.02.2017.
 */
public class XMLParser {

    String xml;

    public void setXml(String xml) {
        this.xml = xml;
    }

    public NodeList parse(){
        NodeList items = null;
        try{

            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = newDocumentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
            Node root = doc.getDocumentElement();
            items = root.getChildNodes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

}
