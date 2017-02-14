import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


class CloudService {

    private static final Namespace NS = Namespace.getNamespace("http://www.cs.au.dk/dWebTek/2014");
    private static final String key = "BA2F22BE812D783D22B8EA5E";
    private static final String baseURL = "http://webtek.cs.au.dk/cloud/";
    private final ArrayList<Item> prodList = new ArrayList<>();

    /**
     * Creates a GET request for an entire list of products from 'shopID' and returns... nothing. Yet.
     */
    void listItems() throws IOException {
        URL reqURL = new URL(baseURL + "listItems?shopID=" + 354);
        Document doc = null;
        HttpURLConnection connection = (HttpURLConnection) reqURL.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int respCode = connection.getResponseCode();
        String respMsg = connection.getResponseMessage();

        connection.disconnect(); //For at være flink ved server og lokale resurser


        if (respCode == 200) {
            try {
                doc = new SAXBuilder().build(new BufferedReader(new InputStreamReader(reqURL.openStream())));
            } catch (JDOMException | IOException e) {
                System.out.println("Svar fra " + reqURL + " er malformed! Prøv igen med korrekt kommando/ID\n(Teknisk fejlmeddelelse: " + e + ")");
            }
        } else {
            System.out.println("Serveren afviste vores forespørgesel :(\n(" + respCode + "): " + respMsg);
        }


        //For every <item> in <items>, create Item object and add it to ArrayList<Item> prodList
        assert doc != null;
        for (Element e : doc.getRootElement().getChildren()) {
            prodList.add(new Item(Integer.parseInt(e.getDescendants(new ElementFilter("itemID")).next().getValue()),
                    e.getDescendants(new ElementFilter("itemName")).next().getValue(),
                    e.getDescendants(new ElementFilter("itemURL")).next().getValue(),
                    Integer.parseInt(e.getDescendants(new ElementFilter("itemPrice")).next().getValue()),
                    Integer.parseInt(e.getDescendants(new ElementFilter("itemStock")).next().getValue()),
                    e.getDescendants(new ElementFilter("itemDescription")).next().getValue()));
        }


        /* Kun til afleveringsopgave; sikkert at slette efterfølgende */
        System.out.println("Ét produkt pr linje som opgavebeskrivelsen lyder på:");
        for (Item item : prodList) {
            System.out.println("ID=" + item.getItemID() + ", name=" + item.getItemName() + ", URL=" + "udeladt" + ", price=" + item.getItemPrice() + ", stock=" + item.getItemStock() + ", desc=udeladt");
        }
    }

    /**
     * Delete item ID
     * @param id ItemID
     */
    OperationResult<String> deleteItem(int id) {
        String info = "";

        Element deleteItem = new Element("deleteItem", NS);
        deleteItem.addContent(new Element("shopKey", NS).setText(key));
        deleteItem.addContent(new Element("itemID", NS).setText(""+ id));

        Document doc = new Document(deleteItem);

        try {
            if (validate(doc).isSuccess()) {
                postit(baseURL + "deleteItem", doc);
                return new OperationResult<>(true, info, "Success");
            }
        }
        catch(Exception e) {
            info = e.toString();
        }

        return new OperationResult<>(false, info, "Fail");
    }


    /**
     * Create items based on POST request to the cloud
     */
    OperationResult<Integer> createItem(String itemName) throws IOException, JDOMException {

        Element createItem = new Element("createItem", NS);
        createItem.addContent(new Element("itemName", NS).setText(itemName));
        createItem.addContent(new Element("shopKey", NS).setText(key));
        Document doc = new Document(createItem);

        if(!validate(doc).isSuccess())
            return new OperationResult<>(false, validate(doc).getMessage(), 0);

        return new OperationResult<>(true, "", Integer.parseInt(new SAXBuilder().build(new StringReader(postit(baseURL+"createItem", doc))).getRootElement().getValue()));
    }

    /**
     * Modify items based on POST request to the cloud
     */
    OperationResult<String> modifyItem(int itemID, String itemName, int itemPrice, String itemURL, String itemDescription) throws IOException, JDOMException {

        OperationResult<Element> itemDescRes = convertItemDescription(itemDescription);
        String info = "";
        Document document = null;

        try {
            Element root = new Element("modifyItem", NS);
            root.addContent(new Element("shopKey").setText(key));
            root.addContent(new Element("itemID").setText("" + itemID));
            root.addContent(new Element("itemName").setText(itemName));
            root.addContent(new Element("itemPrice").setText("" + itemPrice));
            root.addContent(new Element("itemURL").setText(itemURL));
            Element itemDesc = new Element("itemDescription");
            itemDesc.addContent(itemDescRes.getResult());
            root.addContent(itemDesc);

            setNamespace(root);
            document = new Document(root);

        } catch (Exception e) {
            info = e.toString();
        }

        if (validate(document).isSuccess())
            postit(baseURL + "modifyItem", document);

        return new OperationResult<>(validate(document).isSuccess(), info, new XMLOutputter().outputString(document));
    }

    /**
     * 'POST' requests via HTTP
     */
    private String postit(String reqURL, Document doc) throws IOException {
        HttpURLConnection httpCon = (HttpURLConnection) new URL(reqURL).openConnection();
        httpCon.setRequestMethod("POST");
        httpCon.setDoInput(true);
        httpCon.setDoOutput(true);
        httpCon.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");

        new XMLOutputter(Format.getPrettyFormat()).output(doc, httpCon.getOutputStream());

        BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("Server response: (" + httpCon.getResponseCode() + ") " + httpCon.getResponseMessage());


        return response.toString();
    }

    /**
     * Converts itemDescription into a more intelligible composition that supports HTML formatting
     */
    private OperationResult<Element> convertItemDescription(String content) throws JDOMException, IOException {
        return new OperationResult<>(true, "", new SAXBuilder().build(new StringReader("<document>" + content + "</document>")).getRootElement().clone());
    }


    /**
     * Sets namespace for all children, courtesy of Morten
     */
    private void setNamespace(Element child) {
        child.setNamespace(NS);

        if(child.getChildren().isEmpty())
            return;
        else {
            for(Element e:child.getChildren())
                setNamespace(e);
        }
    }

    /**
     * Validates generated XML documents against cloud.xsd
     */
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