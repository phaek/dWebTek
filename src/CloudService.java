import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

class CloudService {

    private static final Namespace NS = Namespace.getNamespace("http://www.cs.au.dk/dWebTek/2014");
    private static final String SHOP_KEY = "BA2F22BE812D783D22B8EA5E";


    OperationResult<String> modifyItem(int itemID, String itemName, int itemPrice, String itemURL, String itemDescription) throws IOException, JDOMException {

        // itemDescription is a string, possible with XML content. We have to transform it.
        OperationResult<Element> itemDescRes = convertItemDescription(itemDescription);
        String info = "";


        // HINT: You can get the XML as a string from a Document by XMLOutputter
        /*SAXBuilder builder = new SAXBuilder();
        Document document = null;
        try {
            document = builder.build(new File("mod1.xml"));
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }

        assert document != null;
        itemID = Integer.parseInt(document.getRootElement().getDescendants(new org.jdom2.filter.ElementFilter("itemID")).next().getValue());
        itemName = document.getRootElement().getDescendants(new org.jdom2.filter.ElementFilter("itemName")).next().getValue();
        itemPrice = Integer.parseInt(document.getRootElement().getDescendants(new org.jdom2.filter.ElementFilter("itemPrice")).next().getValue());
        itemURL = document.getRootElement().getDescendants(new org.jdom2.filter.ElementFilter("itemURL")).next().getValue();
        itemDescription = document.getRootElement().getDescendants(new org.jdom2.filter.ElementFilter("itemDescription")).next().getValue();


        System.out.println("ID:\t" + itemID + "\nName:\t" + itemName + "\nPrice:\t" + itemPrice + "\nURL:\t" + itemURL + "\nDesc:\t" + itemDescription + "\n");
        */

        //Document doc = SAXBuilder.build("www.example.com/fil.xml");


        Document document = null;

        try {

            Element root = new Element("modifyItem", "http://www.cs.au.dk/dWebTek/2014");

            document = new Document(root);

            root.addContent(new Element("shopKey").setText(SHOP_KEY));
            root.addContent(new Element("itemID").setText(""+itemID));
            root.addContent(new Element("itemName").setText(itemName));
            root.addContent(new Element("itemPrice").setText(""+itemPrice));
            root.addContent(new Element("itemURL").setText(itemURL));
            Element itemDesc = new Element("itemDescription");
            itemDesc.addContent(itemDescRes.getResult());
            root.addContent(itemDesc);

            setNamespace(root);

            new XMLOutputter(Format.getPrettyFormat()).output(document, System.out);


        } catch (Exception e) {
            info = e.toString();
        }

        return new OperationResult<>(validate(document).isSuccess(), info, "");
    }


    private OperationResult<Element> convertItemDescription(String content) throws JDOMException, IOException {
        return new OperationResult<>(true, "", new SAXBuilder().build(new StringReader("<document>" + content + "</document>")).getRootElement().clone());
    }

    /**
     * Sets the namespace on the element - what about the children??
     * @param child the xml-element to have set the namespace
     */
    private void setNamespace(Element child) {

        //Jeg tænker at der må være en snedigere måde end det her pis...

        for (Element c : child.getChildren()) {
            c.setNamespace(NS);
            for (Element c2 : c.getChildren()) {
                c2.setNamespace(NS);
                for (Element c3 : c2.getChildren()) {
                    c3.setNamespace(NS);
                    for (Element c4 : c3.getChildren()) {
                        c4.setNamespace(NS);
                    }
                }
            }
        }
    }

    private OperationResult<Object> validate(Document doc) {

        URL url = getClass().getClassLoader().getResource("cloud.xsd");
        XMLReaderJDOMFactory factory;

        try {
            factory = new XMLReaderXSDFactory(url);
        } catch (JDOMException e) {
            return OperationResult.Fail("Could not find schema");
        }

        String xml = new XMLOutputter().outputString(doc);
        SAXBuilder builder = new SAXBuilder(factory);
        try {
            builder.build(new StringReader(xml));
        } catch (JDOMException e) {
            return OperationResult.Fail("Xml is not valid: " + e.getMessage());
        } catch (IOException e) {
            return OperationResult.Fail("YIKES: " + e.getMessage());
        }

        return OperationResult.Success(true);
    }

}