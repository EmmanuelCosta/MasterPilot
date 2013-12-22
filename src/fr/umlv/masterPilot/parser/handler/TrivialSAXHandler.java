package fr.umlv.masterPilot.parser.handler;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler trivial pour SAX
 * Ce handler se contente d'afficher les balises ouvrantes et fermantes.
 * @author O. Carton
 * @version 1.0
 */
class TrivialSAXHandler extends DefaultHandler {
   public void setDocumentLocator(Locator locator) {
       System.out.println("Location : " + 
			  "publicId=" + locator.getPublicId() + 
			  " systemId=" + locator.getSystemId());
   }
   public void startDocument() {
       System.out.println("Debut du document");
   }
   public void endDocument() {
       System.out.println("Fin du document");
   }
   public void startElement(String namespace, 
			    String localname,
			    String qualname,
			    Attributes  atts) {
       System.out.println("Balise ouvrante : " + 
			  "namespace=" + namespace + 
			  " localname=" + localname + 
			  " qualname=" + qualname);
   }
   public void endElement(String  namespace, 
			  String localname,
			  String qualname) {
       System.out.println("Balise fermante : " + 
			  "namespace=" + namespace + 
			  " localname=" + localname + 
			  " qualname=" + qualname);
   }
   public void characters(char[] ch, int start, int length) {
       System.out.print("Caractères : ");
       for(int i = start; i < start+length; i++)
	   System.out.print(ch[i]);
       System.out.println();
   }
   
   
}