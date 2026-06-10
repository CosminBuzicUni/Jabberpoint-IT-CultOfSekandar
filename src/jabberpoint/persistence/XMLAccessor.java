package jabberpoint.persistence;

import jabberpoint.model.CompositeSlideItem;
import jabberpoint.model.Presentation;
import jabberpoint.model.Slide;
import jabberpoint.model.SlideItem;
import jabberpoint.model.SlideItemKind;

import java.util.Vector;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/** XMLAccessor, reads and writes XML files
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class XMLAccessor extends Accessor {

    /** Default API to use. */
    protected static final String DEFAULT_API_TO_USE = "dom";

    /** namen van xml tags of attributen */
    protected static final String SHOWTITLE = "showtitle";
    protected static final String SLIDETITLE = "title";
    protected static final String SLIDE = "slide";
    protected static final String ITEM = "item";
    protected static final String LEVEL = "level";
    protected static final String KIND = "kind";
    protected static final String TEXT = "text";
    protected static final String IMAGE = "image";

    /** tekst van messages */
    protected static final String PCE = "Parser Configuration Exception";
    protected static final String UNKNOWNTYPE = "Unknown Element type";
    protected static final String NFE = "Number Format Exception";


    private String getTitle(Element element, String tagName) {
    	NodeList titles = element.getElementsByTagName(tagName);
    	return titles.item(0).getTextContent();
    }

	public void loadFile(Presentation presentation, String filename) throws IOException {
		int slideNumber, itemNumber;
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse(new File(filename)); // Create a JDOM document
			Element doc = document.getDocumentElement();
			presentation.setTitle(getTitle(doc, SHOWTITLE));

			NodeList slides = doc.getElementsByTagName(SLIDE);
			for (slideNumber = 0; slideNumber < slides.getLength(); slideNumber++) {
				Element xmlSlide = (Element) slides.item(slideNumber);
				Slide slide = new Slide();
				slide.setTitle(getTitle(xmlSlide, SLIDETITLE));
				presentation.append(slide);

				NodeList slideNodes = xmlSlide.getChildNodes();
				for (itemNumber = 0; itemNumber < slideNodes.getLength(); itemNumber++) {
					Node node = slideNodes.item(itemNumber);
					if (node.getNodeType() == Node.ELEMENT_NODE && ((Element) node).getTagName().equals(ITEM)) {
						loadSlideItem(slide, (Element) node);
					}
				}
			}
		}
		catch (IOException iox) {
			System.err.println(iox.toString());
		}
		catch (SAXException sax) {
			System.err.println(sax.getMessage());
		}
		catch (ParserConfigurationException pcx) {
			System.err.println(PCE);
		}
	}

	protected void loadSlideItem(Slide slide, Element item) {
		SlideItem slideItem = parseSlideItem(item);
		if (slideItem != null) {
			slide.append(slideItem);
		}
	}

	private SlideItem parseSlideItem(Element item) {
		int level = 1;
		NamedNodeMap attributes = item.getAttributes();
		String leveltext = attributes.getNamedItem(LEVEL).getTextContent();
		if (leveltext != null) {
			try {
				level = Integer.parseInt(leveltext);
			} catch (NumberFormatException x) {
				System.err.println(NFE);
			}
		}
		String kindText = attributes.getNamedItem(KIND).getTextContent();
		SlideItemKind kind = null;
		try {
			kind = SlideItemKind.valueOf(kindText.toUpperCase());
		} catch (IllegalArgumentException ignored) {
		}

		if (kind == SlideItemKind.COMPOSITE) {
			CompositeSlideItem composite = new CompositeSlideItem(level);
			NodeList children = item.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child.getNodeType() == Node.ELEMENT_NODE
						&& ((Element) child).getTagName().equals(ITEM)) {
					SlideItem childItem = parseSlideItem((Element) child);
					if (childItem != null) {
						composite.addChild(childItem);
					}
				}
			}
			return composite;
		}

		SlideItem slideItem = SlideItem.createSlideItem(kind, level, item.getTextContent());
		if (slideItem == null) {
			System.err.println(UNKNOWNTYPE + ": " + kindText);
		}
		return slideItem;
	}

	public void saveFile(Presentation presentation, String filename) throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter(filename));
		out.println("<?xml version=\"1.0\"?>");
		out.println("<!DOCTYPE presentation SYSTEM \"jabberpoint.dtd\">");
		out.println("<presentation>");
		out.print("<showtitle>");
		out.print(presentation.getTitle());
		out.println("</showtitle>");
		for (int slideNumber=0; slideNumber<presentation.getSize(); slideNumber++) {
			Slide slide = presentation.getSlide(slideNumber);
			out.println("<slide>");
			out.println("<title>" + slide.getTitle() + "</title>");
			Vector<SlideItem> slideItems = slide.getSlideItems();
			for (int itemNumber = 0; itemNumber < slideItems.size(); itemNumber++) {
				writeSlideItem(out, slideItems.elementAt(itemNumber));
			}
			out.println("</slide>");
		}
		out.println("</presentation>");
		out.close();
	}

	private void writeSlideItem(PrintWriter out, SlideItem item) {
		out.print("<item kind=\"" + item.getKind() + "\" level=\"" + item.getLevel() + "\">");
		if (item instanceof CompositeSlideItem) {
			out.println();
			for (SlideItem child : ((CompositeSlideItem) item).getChildren()) {
				out.print("\t");
				writeSlideItem(out, child);
			}
		} else {
			out.print(item.getSerializationText());
		}
		out.println("</item>");
	}
}
