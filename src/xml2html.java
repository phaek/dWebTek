import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;


@FacesComponent("xml2html")
public class xml2html extends UIComponentBase {


    public xml2html(){}

    @Override
    public String getFamily() { return "xml2html";}

    @Override
    public void encodeBegin(FacesContext arg) throws IOException{ super.encodeBegin(arg); }

    @Override
    public void encodeEnd(FacesContext context) throws IOException{

        String value = (String) getAttributes().get("value");

        try {
            Document old = new SAXBuilder().build(new ByteArrayInputStream(value.getBytes()));
            Element newRoot = (Element) reverseConversion(old.getRootElement());
            Document newDoc = new Document(newRoot);
            new XMLOutputter().output(newDoc, context.getResponseWriter());
        } catch (JDOMException ignored) {}

    }

    private Content reverseConversion(Content old) {

        if(old instanceof Text) return new Text(((Text)old).getText());

        Element currEle = (Element) old;
        Element htmlElement = null;

        if(currEle.getName().equals("italics") ){
            htmlElement = new Element("i");
        } else if (currEle.getName().equals("bold") ){
            htmlElement = new Element("b");
        } else if (currEle.getName().equals("list") ){
            htmlElement = new Element("ul");
        } else if (currEle.getName().equals("item") ){
            htmlElement = new Element("li");
        }else if (currEle.getName().equals("document") ){
            htmlElement = new Element("div");
        }

        for (Content e : currEle.getContent()){
            assert htmlElement != null;
            htmlElement.addContent(reverseConversion(e));

        }
        return htmlElement;
    }
}