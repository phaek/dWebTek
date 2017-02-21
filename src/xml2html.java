import javax.faces.bean.ManagedBean;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;


@FacesComponent("xml2html")
public class xml2html extends UIComponentBase {


    public xml2html(){}

    @Override
    public String getFamily() { return "xml2html";}

    @Override
    public void encodeBegin(FacesContext arg) throws IOException{ super.encodeBegin(arg); }

    @Override
    public void encodeEnd(FacesContext context) throws IOException{
        ResponseWriter out = context.getResponseWriter();

        out.startElement("div", null);
        out.write(replacer((String) getAttributes().get("value")));
        out.endElement("div");
    }

    public String presenter(String in) {

        String out = in.replace("<w:document xmlns:w=\"http://www.cs.au.dk/dWebTek/2014\">", "");
        out = out.replace("</w:document>", "");
        out = out.replace("<w:document xmlns:w=\"http://www.cs.au.dk/dWebTek/2014\" />", "");
        out = out.replaceAll("w:", "");

        return out.trim();
    }

    private String replacer(String in) {
        String out = in.replaceAll("w:document", "div");
        out = out.replaceAll("w:italics", "i");
        out = out.replaceAll("w:bold", "b");
        out = out.replaceAll("w:item", "li");
        out = out.replaceAll("w:list", "ul");

        return out;
    }
}