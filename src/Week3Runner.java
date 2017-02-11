import org.jdom2.JDOMException;
import java.io.IOException;


public class Week3Runner {

    private static CloudService service = new CloudService();

    public static void main(String[] args) throws IOException, JDOMException {

        if (args.length == 0) {
            System.out.println("Not enough arguments...");
            return;
        }

        switch (args[0]) {
            case "createItem": createItem(args); break;
            case "modifyItem": modifyItem(args); break;
            case "listItems": listItems(); break;
            case "deleteItem": deleteItem(args); break;

            default:
                System.out.println("Command not valid");
        }

    }

    private static void createItem(String[] args) throws IOException, JDOMException {

        String itemName = args[1];
        OperationResult<Integer> result = service.createItem(itemName);
        if (result.isSuccess()) {
            System.out.printf("Result: " + result.getResult() + "\n");
        } else {
            System.out.println(result.getMessage());
        }
    }

    private static void modifyItem(String[] args) throws IOException, JDOMException {
        int itemID = Integer.parseInt(args[1]);
        String itemName = args[2];
        int itemPrice = Integer.parseInt(args[3]);
        String itemUrl = args[4];
        String itemDescriptionXml = args[5];
        OperationResult res = service.modifyItem(itemID, itemName, itemPrice, itemUrl, itemDescriptionXml);
        if (res.isSuccess()) {
            System.out.printf("Success\n");
        } else {
            System.out.println(res.getMessage());
        }
    }

    private static void listItems() throws IOException, JDOMException {
        service.listItems();
    }

    private static void deleteItem(String[] args) throws IOException {
        int itemID = Integer.parseInt(args[1]);
        OperationResult<String> result = service.deleteItem(itemID);
        if (result.isSuccess())
            System.out.println("Result: " + result.getResult() + "\n");
        else
            System.out.println(result.getMessage());
    }

}
