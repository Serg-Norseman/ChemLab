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

import bslib.common.AuxUtils;
import bslib.common.Logger;
import bslib.common.StringHelper;
import chemlab.core.chemical.SubstanceState;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public final class CompoundsBook extends RefBook
{
    private static final String FILENAME = "/compounds.xml";
    
    private final ArrayList<CompoundRecord> fList;
    private final HashMap<String, CompoundRecord> fMap;
    
    public CompoundsBook()
    {
        this.fList = new ArrayList<>();
        this.fMap = new HashMap<>();
    }

    @Override
    public final CompoundRecord get(int index)
    {
        if (index < 0 || index >= this.fList.size()) {
            return null;
        }
        return this.fList.get(index);
    }

    @Override
    public final int size()
    {
        return this.fList.size();
    }
    
    @Override
    public final Iterable<CompoundRecord> getList()
    {
        return this.fList;
    }
    
    public void loadXML()
    {
        this.fList.clear();
        this.fMap.clear();
        
        RefBook.loadFile(AuxUtils.getAppPath() + FILENAME, "compounds", "compound", new IElementLoader<CompoundRecord>()
        {
            @Override
            public int load(Element el) throws ParseException
            {
                CompoundRecord compRec = new CompoundRecord();
                compRec.Formula = el.getAttribute("formula");
                
                Element physEl = getElement(el, "physical");
                if (physEl != null) {
                    compRec.Charge = AuxUtils.ParseInt(physEl.getAttribute("charge"), 0);
                    compRec.Density = AuxUtils.ParseFloat(physEl.getAttribute("density"), 0);
                    compRec.MolecularMass = AuxUtils.ParseFloat(physEl.getAttribute("mass"), 0);

                    String state = physEl.getAttribute("state");
                    compRec.State = (StringHelper.isNullOrEmpty(state) || state.equals("null")) ? null : SubstanceState.valueOf(SubstanceState.class, state);
                }
                
                Element namesEl = getElement(el, "names");
                if (namesEl != null) {
                    NodeList nl = namesEl.getChildNodes();
                    for (int i = 0; i < nl.getLength(); i++) {
                        Node n = nl.item(i);
                        if (n instanceof Element) {
                            Element nameEl = (Element) n;
                            String lang = nameEl.getAttribute("lang");
                            String value = nameEl.getTextContent();
                            compRec.Names.put(lang, value);
                        }
                    }
                }
                
                fList.add(compRec);
                fMap.put(compRec.Formula, compRec);

                return 0;
            }
        });
    }

    private void writeAttr(Document doc, Element element, String attrName, String attrVal)
    {
        Attr formulaAttr = doc.createAttribute(attrName);
        formulaAttr.setValue(attrVal);
        element.setAttributeNode(formulaAttr);
    }

    public void saveXML()
    {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("compounds");
            doc.appendChild(rootElement);

            for (CompoundRecord compRec : this.fList) {
                Element compElement = doc.createElement("compound");
                rootElement.appendChild(compElement);

                writeAttr(doc, compElement, "formula", compRec.Formula);

                Element physEl = doc.createElement("physical");
                compElement.appendChild(physEl);

                writeAttr(doc, physEl, "charge", String.valueOf(compRec.Charge));
                writeAttr(doc, physEl, "density", String.valueOf(compRec.Density));
                writeAttr(doc, physEl, "mass", String.valueOf(compRec.MolecularMass));
                writeAttr(doc, physEl, "state", String.valueOf(compRec.State));

                //

                Element namesElement = doc.createElement("names");
                compElement.appendChild(namesElement);
                for (Map.Entry<String, String> entry : compRec.Names.entrySet()) {
                    Element nameEl = doc.createElement("name");
                    namesElement.appendChild(nameEl);
                    
                    writeAttr(doc, nameEl, "lang", entry.getKey());
                    nameEl.setTextContent(entry.getValue());
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(AuxUtils.getAppPath() + FILENAME));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);
        } catch (ParserConfigurationException ex) {
            Logger.write("CompoundsBook.saveXML(): " + ex.getMessage());
        } catch (TransformerException ex) {
            Logger.write("CompoundsBook.saveXML(): " + ex.getMessage());
        }
    }

    public final CompoundRecord add(String formula)
    {
        CompoundRecord compound = new CompoundRecord();
        compound.Formula = formula;
        
        this.fList.add(compound);
        this.fMap.put(formula, compound);
        
        return compound;
    }

    public CompoundRecord checkCompound(String formula)
    {
        CompoundRecord compRec = this.fMap.get(formula);
        if (compRec == null) {
            compRec = this.add(formula);
        }
        return compRec;
    }
}
