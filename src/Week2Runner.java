import org.jdom2.JDOMException;

import java.io.IOException;

/**
 * Created by mortenkrogh-jespersen on 25/01/2017.
 */
public class Week2Runner {

    public static void main(String[] args) throws IOException, JDOMException {

        if (args.length < 5) {
            System.out.println("Did not pass in enough arguments... " +
                    "The arguments should be: itemID, itemName, itemPrice, itemURL and itemDescription...");
            return;
        }

        CloudService cloud = new CloudService();
        OperationResult<String> result = cloud.modifyItem(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]), args[3], args[4]);


        System.out.println("\nResult: " + result.isSuccess());

        if (!result.isSuccess()) {
            System.out.println(result.getMessage());
        }

        if (result.getResult() != null) {
            System.out.println(result.getResult());
        }
    }

}
