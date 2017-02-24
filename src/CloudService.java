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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


class CloudService {

    private static final Namespace NS = Namespace.getNamespace("http://www.cs.au.dk/dWebTek/2014");
    private static final String baseURL = "http://webtek.cs.au.dk/cloud/";
    private ArrayList<Item> prodList = new ArrayList<>();
    private ArrayList<Customer> customerList = new ArrayList<>();

    /**
     * Creates a GET request for an entire list of products from 'shopID' and returns the prodList
     */
    ArrayList<Item> listItems() throws IOException {
        URL reqURL = new URL(baseURL + "listItems?shopID=" + 354);
        Document doc = null;
        prodList.clear();

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


        assert doc != null;
        for (Element e : doc.getRootElement().getChildren()) {
            prodList.add(new Item(Integer.parseInt(e.getDescendants(new ElementFilter("itemID")).next().getValue()),
                    e.getDescendants(new ElementFilter("itemName")).next().getValue(),
                    e.getDescendants(new ElementFilter("itemURL")).next().getValue(),
                    Integer.parseInt(e.getDescendants(new ElementFilter("itemPrice")).next().getValue()),
                    Integer.parseInt(e.getDescendants(new ElementFilter("itemStock")).next().getValue()),
                    new XMLOutputter().outputElementContentString(e.getChild("itemDescription", NS))));
        }
        return prodList;
    }


    public ArrayList<Customer> listCustomers() throws IOException {
        URL reqURL = new URL(baseURL + "listCustomers?shopID=" + 354);
        Document doc = null;
        customerList.clear();

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


        assert doc != null;
        for (Element e : doc.getRootElement().getChildren()) {
            customerList.add(new Customer(e.getDescendants(new ElementFilter("customerID")).next().getValue(), e.getDescendants(new ElementFilter("customerName")).next().getValue()));
        }
        return customerList;
    }


    /**
     * Converts itemDescription into a more intelligible composition that supports HTML formatting
     */
    public OperationResult<Element> convertItemDescription(String content) {

        String modifiedContent = "<document>" + content + "</document>";
        SAXBuilder builder = new SAXBuilder();
        StringReader reader;
        Element retVal = null;
        String info = null;


        try {
            reader = new StringReader(modifiedContent);
            retVal = builder.build(reader).getRootElement().clone();
        } catch (JDOMException | IOException e) {
            info = e.getMessage();
        }


        return new OperationResult<>(true, info, retVal);
    }


    /**
     * Sets namespace for all children, courtesy of Morten
     */
    public void setNamespace(Element child) {
        child.setNamespace(NS);

        if(child.getChildren().isEmpty())
            return;
        else {
            for(Element e:child.getChildren())
                setNamespace(e);
        }
    }

    /**
     * 'POST' requests via HTTP
     */
    public String postit(String reqURL, Document doc) throws IOException {
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

        return response.toString();
    }

    /**
     * Validates generated XML documents against cloud.xsd
     */
    public OperationResult<Object> validate(Document doc) {

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