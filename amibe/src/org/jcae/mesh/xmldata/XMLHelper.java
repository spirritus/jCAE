/* jCAE stand for Java Computer Aided Engineering. Features are : Small CAD
   modeler, Finite element mesher, Plugin architecture.
 
    Copyright (C) 2003,2004,2005, by EADS CRC
    Copyright (C) 2008, by EADS France
 
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.
 
    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.
 
    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA */

package org.jcae.mesh.xmldata;

import java.io.IOException;
import java.io.File;
import java.io.StringReader;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** Some methods to help using DOM */
public class XMLHelper
{	
	/** Read an XML file with DTD validation. Use ClassPathEntityResolver to solve URI.
	 * Return a normalized Document
	 */	
	public static Document parseXML(File file)
		throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder=factory.newDocumentBuilder();
		builder.setEntityResolver(new ClassPathEntityResolver());
		Document document=builder.parse(file);
		document.normalize();
		return document;
	}
	
	/** Parse a valid xml string and return the Element representing this string. */	
	public static Element parseXMLString(Document document, String string)
		throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder=factory.newDocumentBuilder();
		Document subDoc=builder.parse(new InputSource(new StringReader(string)));
		Element e=subDoc.getDocumentElement();		
		return (Element)document.importNode(e, true);
	}
	
	private static void removeEmptyLines(Document doc) {
		try {
			XPath xp = XPathFactory.newInstance().newXPath();
			NodeList nl = (NodeList) xp.evaluate(
				"//text()[normalize-space(.)='']",
				doc, XPathConstants.NODESET);

			for (int i = 0; i < nl.getLength(); ++i) {
				Node node = nl.item(i);
				node.getParentNode().removeChild(node);
			}
		} catch (XPathExpressionException ex) {
			Logger.getLogger(XMLHelper.class.getName()).log(Level.SEVERE, null,
				ex);
		}
	}

	/** Write a DOM to a file. */
	public static void writeXML(Document document, File file)
		throws IOException
	{
		// save the DOM to file
		StreamResult result = new StreamResult(new BufferedOutputStream(new FileOutputStream(file)));
		TransformerFactory transFactory = TransformerFactory.newInstance();
		try
		{
			document.normalizeDocument();
			removeEmptyLines(document);
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(new DOMSource(document), result);
		}
		catch (TransformerConfigurationException ex)
		{
			throw new IOException(ex.getMessage());
		}
		catch (TransformerException ex)
		{
			throw new IOException(ex.getMessage());
		}
		result.getOutputStream().close();
	}

	private static String listToString(ArrayList<String> pathSpec)
	{
		if (pathSpec.isEmpty())
			return "";
		StringBuilder ret = new StringBuilder();
		ret.append(pathSpec.get(0));
		for (int i = 1, n = pathSpec.size(); i < n; i++)
			ret.append(File.separator).append(pathSpec.get(i));
		return ret.toString();
	}

	/** Removes useless path components.  */
	private static String canonicalize(String path)
	{
		String pattern=File.separator;
		if(pattern.equals("\\"))
			pattern="\\\\";
		
		String [] splitted = path.split(pattern);
		ArrayList<String> pathSpec = new ArrayList<String>(splitted.length);
		for (int i = 0; i < splitted.length; i++)
			pathSpec.add(splitted[i]);
		// Warning: these steps must be performed in this exact order!
		// Step 1: Remove empty paths
		for (ListIterator<String> it = pathSpec.listIterator(); it.hasNext(); )
		{
			String c = it.next();
			if (c.length() == 0 && it.previousIndex() > 0)
				it.remove();
		}
		// Step 2: Remove all occurrences of "."
		for (ListIterator<String> it = pathSpec.listIterator(); it.hasNext(); )
		{
			String c = it.next();
			if (c.equals("."))
				it.remove();
		}
		// Step 3: Remove all occurrences of "foo/.."
		for (ListIterator<String> it = pathSpec.listIterator(); it.hasNext(); )
		{
			String c = it.next();
			if (c.equals("..") && it.previousIndex() > 0)
			{
				if (it.previousIndex() == 1 && pathSpec.get(0).length() == 0)
				{
					// "/.." is replaced by "/"
					it.remove();
				}
				else if (!pathSpec.get(it.previousIndex() - 1).equals(".."))
				{
					it.remove();
					it.previous();
					it.remove();
				}
			}
		}
		return listToString(pathSpec);
	}
	
	/** Removes useless path components.  */
	public static String canonicalize(String dir, String file)
	{
		if (dir != null && dir.length() > 0)
		{
			if (file.startsWith(dir+File.separator))
				file = file.substring(dir.length()+1);
		}
		return canonicalize(file);
	}
}
