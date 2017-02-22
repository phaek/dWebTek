import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;


public class Controller {

    private static final Namespace NS = Namespace.getNamespace("http://www.cs.au.dk/dWebTek/2014");
    private static final String key = "BA2F22BE812D783D22B8EA5E";
    private static final String baseURL = "http://webtek.cs.au.dk/cloud/";
    private CloudService service;


    /**
     * Create items based on POST request to the cloud
     */
    OperationResult<Integer> createItem(String itemName) {

        service = new CloudService();
        OperationResult<Integer> retVal;
        String info = "";
        int returnShopID = 0;

        Element createItem = new Element("createItem", NS);
        createItem.addContent(new Element("itemName", NS).setText(itemName));
        createItem.addContent(new Element("shopKey", NS).setText(key));
        Document doc = new Document(createItem);

        if(!service.validate(doc).isSuccess()) {
            return new OperationResult<>(false, service.validate(doc).getMessage(), 0);
        }

        try {
            returnShopID = Integer.parseInt(new SAXBuilder().build(new StringReader(new CloudService().postit(baseURL+"createItem", doc))).getRootElement().getValue());
        } catch (JDOMException | IOException e) {
            info = e.toString();
            e.printStackTrace();
        }
        
        retVal = new OperationResult<>(true, info, returnShopID);

        return retVal;
    }



    public String adjustItemStock(Item item) throws IOException {
        String result = null;
        
        Element root = new Element("adjustItemStock", NS);
        root.addContent(new Element("shopKey", NS).setText(key));
        root.addContent(new Element("itemID", NS).setText(String.valueOf(item.getItemID())));
        root.addContent(new Element("adjustment", NS).setText(String.valueOf(item.getItemStock()+1)));
        Document doc = new Document(root);

        if (new CloudService().validate(doc).isSuccess()) {
            result = new CloudService().postit(baseURL+"/adjustItemStock", doc);
        }
        return result;
    }


    /**
     * Modify items based on POST request to the cloud
     */
    OperationResult<String> modifyItem(int itemID, String itemName, int itemPrice, String itemURL, String itemDescription) throws IOException, JDOMException {

        service = new CloudService();
        OperationResult<Element> itemDescRes = service.convertItemDescription(itemDescription);
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

            service.setNamespace(root);
            document = new Document(root);

        } catch (Exception e) {
            info = e.toString();
        }

        if (service.validate(document).isSuccess())
            new CloudService().postit(baseURL + "modifyItem", document);


        return new OperationResult<>(service.validate(document).isSuccess(), info, new XMLOutputter().outputString(document));
    }


    /**
     * Delete item ID
     * @param id ItemID
     */
    OperationResult<String> deleteItem(int id) {
        String info = "";

        service = new CloudService();
        Element deleteItem = new Element("deleteItem", NS);
        deleteItem.addContent(new Element("shopKey", NS).setText(key));
        deleteItem.addContent(new Element("itemID", NS).setText(""+ id));

        Document doc = new Document(deleteItem);

        try {
            new CloudService().postit(baseURL + "deleteItem", doc);
            return new OperationResult<>(true, info, "Success");
        }
        catch(Exception e) {
            info = "deleteItem fejlede :(\n" + e.toString();
        }

        return new OperationResult<>(service.validate(doc).isSuccess(), info, "Fail");
    }
}
