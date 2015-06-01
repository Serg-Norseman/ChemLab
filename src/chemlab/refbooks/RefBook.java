/*
 *  "ChemLab", Desktop helper application for chemists.
 *  Copyright (C) 1996-1998, 2015 by Serg V. Zhdanovskih (aka Alchemist, aka Norseman).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package chemlab.refbooks;

import bslib.common.Logger;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public abstract class RefBook<T>
{
    public abstract T get(int index);
    public abstract int size();
    public abstract Iterable<T> getList();
    
    public static Element getElement(Element parentElement, String tagName)
    {
        NodeList nl = parentElement.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            String nodeName = node.getNodeName();
            if (node instanceof Element && nodeName.equalsIgnoreCase(tagName)) {
                return ((Element) node);
            }
        }
        return null;
    }
    
    public static String readElement(Element parentElement, String tagName)
    {
        NodeList nl = parentElement.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            String nodeName = node.getNodeName();
            if (node instanceof Element && nodeName.equalsIgnoreCase(tagName)) {
                String value = ((Element) node).getTextContent();
                return value;
            }
        }
        return "";
    }

    protected static void loadDataStream(InputStream stream, String rootName, String elementName, IElementLoader<?> elementLoader)
    {
        if (elementLoader == null) return;
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(stream);

            Element root = doc.getDocumentElement();
            if (!root.getTagName().equals(rootName)) {
                throw new Exception("Invalid root");
            }

            elementLoader.processRoot(root);
            
            NodeList nl = root.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);
                if (n instanceof Element) {
                    Element el = (Element) n;
                    if (el.getTagName().equals(elementName)) {
                        try {
                            elementLoader.load(el);
                        } catch (ParseException ex) {
                            Logger.write("RefBook.loadDataStream.parse(): " + ex.getMessage());
                        } catch (Exception ex) {
                            Logger.write("RefBook.loadDataStream.elementloader(): " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SAXParseException ex) {
            Logger.write("RefBook.loadDataStream.sax(): " + ex.getMessage());
        } catch (Exception ex) {
            Logger.write("RefBook.loadDataStream(): " + ex.getMessage());
        }
    }

    protected static void loadFile(String fileName, String rootName, String elementName, IElementLoader<?> elementloader)
    {
        if (elementloader == null) return;
        
        try {
            InputStream is = new FileInputStream(fileName);
            loadDataStream(is, rootName, elementName, elementloader);
        } catch (Exception ex) {
            Logger.write("RefBook.loadDataFile(): " + ex.getMessage());
        }
    }

    // FIXME
    public static void loadResource(String resName, String rootName, String elementName, IElementLoader<?> elementloader)
    {
        if (elementloader == null) return;
        
        try {
            InputStream is = RefBook.class.getResourceAsStream(resName);
            loadDataStream(is, rootName, elementName, elementloader);
        } catch (Exception ex) {
            Logger.write("RefBook.loadDataFile(): " + ex.getMessage());
        }
    }
}
