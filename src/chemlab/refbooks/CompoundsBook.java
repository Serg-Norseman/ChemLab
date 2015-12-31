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
import java.awt.Color;
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
    private static final int VERSION = 2;
    
    private static final String FILENAME = "/compounds.xml";
    private static final String BACKNAME = "/compounds.bak";
    
    private final ArrayList<CompoundRecord> fList;
    private final HashMap<String, CompoundRecord> fMap;
    
    private int fCurrentVer;
    
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
        
        this.fCurrentVer = 0;
        
        RefBook.loadFile(AuxUtils.getAppPath() + FILENAME, "compounds", "compound", new IElementLoader<CompoundRecord>()
        {
            @Override
            public void processRoot(Element root)
            {
                try {
                    String verAttr = root.getAttribute("version");
                    fCurrentVer = Integer.parseInt(verAttr);
                } catch (NumberFormatException ex) {
                    fCurrentVer = 0;
                }
            }
            
            @Override
            public int load(Element el) throws ParseException
            {
                CompoundRecord compRec = new CompoundRecord();
                compRec.Formula = el.getAttribute("formula");
                
                if (fCurrentVer == 1) {
                    Element physEl = getElement(el, "physical");
                    if (physEl != null) {
                        compRec.setMolecularMass(AuxUtils.parseFloat(physEl.getAttribute("mass"), 0));
                        String st = physEl.getAttribute("state");
                        double density = AuxUtils.parseFloat(physEl.getAttribute("density"), 0);

                        SubstanceState state = (StringHelper.isNullOrEmpty(st) || st.equals("null")) ? SubstanceState.Solid : SubstanceState.valueOf(SubstanceState.class, st);
                        PhysicalState ps = compRec.getPhysicalState(state, true);
                        if (ps != null) {
                            ps.State = state;
                            ps.Density = density;
                        }
                    }
                } else {
                    compRec.setMolecularMass(AuxUtils.parseFloat(el.getAttribute("mass"), 0));
                    
                    Element pssEl = getElement(el, "physicals");
                    if (pssEl != null) {
                        NodeList nl = pssEl.getChildNodes();
                        for (int i = 0; i < nl.getLength(); i++) {
                            Node n = nl.item(i);
                            if (n instanceof Element) {
                                Element psEl = (Element) n;
                                
                                String st = psEl.getAttribute("state");
                                double density = AuxUtils.parseFloat(psEl.getAttribute("density"), 0);
                                
                                SubstanceState state = (StringHelper.isNullOrEmpty(st) || st.equals("null")) ? SubstanceState.Solid : SubstanceState.valueOf(SubstanceState.class, st);
                                PhysicalState ps = compRec.getPhysicalState(state, true);
                                if (ps != null) {
                                    ps.State = state;
                                    ps.Density = density;

                                    ps.HeatFormation = AuxUtils.parseFloat(psEl.getAttribute("HeatFormation"), 0);
                                    ps.GibbsFreeEnergy = AuxUtils.parseFloat(psEl.getAttribute("GibbsFreeEnergy"), 0);
                                    ps.StdEntropy = AuxUtils.parseFloat(psEl.getAttribute("StdEntropy"), 0);
                                    ps.MolarHeatCapacity = AuxUtils.parseFloat(psEl.getAttribute("SpecificHeat"), 0);
                                    
                                    ps.Color = getHexColor(psEl.getAttribute("Color"));
                                }
                            }
                        }
                    }
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
                
                Element radicalsEl = getElement(el, "radicals");
                if (radicalsEl != null) {
                    NodeList nl = radicalsEl.getChildNodes();
                    for (int i = 0; i < nl.getLength(); i++) {
                        Node n = nl.item(i);
                        if (n instanceof Element) {
                            Element radEl = (Element) n;
                            String formula = radEl.getAttribute("formula");
                            int charge = Integer.parseInt(radEl.getAttribute("charge"));
                            compRec.Radicals.add(new RadicalRecord(formula, charge));
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

    private static void backupFile(String oldName, String newName)
    {
        File oldFile = new File(oldName);
        if (!oldFile.exists()) {
            return;
        }
        
        File newFile = new File(newName);
        if (newFile.exists()) {
            newFile.delete();
        }

        boolean success = oldFile.renameTo(newFile);
        if (!success) {
            // File was not successfully renamed
        }
    }

    private static Color getHexColor(String str)
    {
        if (StringHelper.isNullOrEmpty(str)) {
            return Color.black;
        }
        
        return Color.decode(str);
    }
    
    private static String getColorHex(Color color)
    {
        if (color == null) {
            return "#000000";
        }
        
        String hex = "#"+Integer.toHexString(color.getRGB()).substring(2);
        return hex;
    }
    
    public void saveXML()
    {
        try {
            String fileName = AuxUtils.getAppPath() + FILENAME;
            String backupName = AuxUtils.getAppPath() + BACKNAME;
            
            // backup old file
            backupFile(fileName, backupName);
            
            // saving
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("compounds");
            doc.appendChild(rootElement);

            writeAttr(doc, rootElement, "version", String.valueOf(VERSION));
            
            for (CompoundRecord compRec : this.fList) {
                Element compElement = doc.createElement("compound");
                rootElement.appendChild(compElement);

                writeAttr(doc, compElement, "formula", compRec.Formula);
                writeAttr(doc, compElement, "mass", String.valueOf(compRec.getMolecularMass()));

                //

                Element pssElement = doc.createElement("physicals");
                compElement.appendChild(pssElement);
                for (SubstanceState state : SubstanceState.values()) {
                    if (state != SubstanceState.Ion) {
                        PhysicalState ps = compRec.getPhysicalState(state, false);
                        if (ps != null) {
                            Element psEl = doc.createElement("physical");
                            pssElement.appendChild(psEl);
                            
                            writeAttr(doc, psEl, "state", String.valueOf(ps.State));
                            writeAttr(doc, psEl, "density", String.valueOf(ps.Density));

                            writeAttr(doc, psEl, "HeatFormation", String.valueOf(ps.HeatFormation));
                            writeAttr(doc, psEl, "GibbsFreeEnergy", String.valueOf(ps.GibbsFreeEnergy));
                            writeAttr(doc, psEl, "StdEntropy", String.valueOf(ps.StdEntropy));
                            writeAttr(doc, psEl, "SpecificHeat", String.valueOf(ps.MolarHeatCapacity));
                            
                            writeAttr(doc, psEl, "Color", getColorHex(ps.Color));
                        }
                    }
                }

                //

                Element namesElement = doc.createElement("names");
                compElement.appendChild(namesElement);
                for (Map.Entry<String, String> entry : compRec.Names.entrySet()) {
                    Element nameEl = doc.createElement("name");
                    namesElement.appendChild(nameEl);
                    
                    writeAttr(doc, nameEl, "lang", entry.getKey());
                    nameEl.setTextContent(entry.getValue());
                }

                //

                Element radicalsElement = doc.createElement("radicals");
                compElement.appendChild(radicalsElement);
                for (RadicalRecord rad : compRec.Radicals) {
                    Element radEl = doc.createElement("radical");
                    radicalsElement.appendChild(radEl);

                    writeAttr(doc, radEl, "formula", rad.Formula);
                    writeAttr(doc, radEl, "charge", String.valueOf(rad.Charge));
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));

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
