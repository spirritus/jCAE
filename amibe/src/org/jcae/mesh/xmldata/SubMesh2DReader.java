/* jCAE stand for Java Computer Aided Engineering. Features are : Small CAD
   modeler, Finit element mesher, Plugin architecture.
 
	Copyright (C) 2003 Jerome Robert <jeromerobert@users.sourceforge.net>
 
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
	Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jcae.mesh.xmldata;

import org.jcae.mesh.mesher.ds.*;
import org.jcae.mesh.cad.*;
import java.io.*;
import java.util.Iterator;
import org.w3c.dom.*;
import org.w3c.dom.traversal.NodeIterator;
import org.apache.xpath.CachedXPathAPI;
import org.apache.log4j.Logger;


public class SubMesh2DReader extends JCAEXMLData
{
	private static Logger logger=Logger.getLogger(SubMesh2DReader.class);
	
	/**
	 * Reads a SubMesh2D instance from a file.
	 * @param file The name of the XML file
	 */
	public static SubMesh2D readObject(String xmlDir, String xmlFile, CADFace F)
	{
		SubMesh2D submesh = null;
		Document document;
		try
		{
			document = XMLHelper.parseXML(new File(xmlDir, xmlFile));
		}
		catch(FileNotFoundException ex)
		{
			return submesh;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		int i;
		CachedXPathAPI xpath = new CachedXPathAPI();
		try
		{
			String nodesFile = xpath.selectSingleNode(document,
				"/jcae/mesh/submesh/nodes/file/@location").getNodeValue();
			DataInputStream nodesIn=new DataInputStream(new BufferedInputStream(new FileInputStream(xmlDir+File.separator+nodesFile)));
			String refFile = xpath.selectSingleNode(document,
				"/jcae/mesh/submesh/nodes/references/file/@location").getNodeValue();
			DataInputStream refsIn=new DataInputStream(new BufferedInputStream(new FileInputStream(xmlDir+File.separator+refFile)));
			String trianglesFile = xpath.selectSingleNode(document,
				"/jcae/mesh/submesh/triangles/file/@location").getNodeValue();
			DataInputStream trianglesIn=new DataInputStream(new BufferedInputStream(new FileInputStream(xmlDir+File.separator+trianglesFile)));
			
			Node submeshElement = xpath.selectSingleNode(document,
				"/jcae/mesh/submesh");
			Node submeshNodes = xpath.selectSingleNode(submeshElement, "nodes");
			
			submesh = new SubMesh2D(F);
			int numberOfReferences = Integer.parseInt(
				xpath.selectSingleNode(submeshNodes, "references/number/text()").getNodeValue());
			int [] refs = new int[numberOfReferences];
			logger.debug("Reading "+numberOfReferences+" references");
			for (i=0; i < numberOfReferences; i++)
				refs[i] = refsIn.readInt();
			
			int numberOfNodes = Integer.parseInt(
				xpath.selectSingleNode(submeshNodes, "number/text()").getNodeValue());
			logger.debug("Reading "+numberOfNodes+" nodes");
			MNode2D [] nodelist = new MNode2D[numberOfNodes];
			for (i=0; i < numberOfNodes; i++)
			{
				double u = nodesIn.readDouble();
				double v = nodesIn.readDouble();
				nodelist[i] = new MNode2D(u, v);
				if (i < numberOfNodes - numberOfReferences)
					nodelist[i].setLabel(-1);
				else
					nodelist[i].setLabel(refs[i+numberOfReferences-numberOfNodes]);
			}
			
			Node submeshFaces = xpath.selectSingleNode(submeshElement, "triangles");
			int numberOfFaces = Integer.parseInt(
					xpath.selectSingleNode(submeshFaces, "number/text()").getNodeValue());
			logger.debug("Reading "+numberOfFaces+" faces");
			for (i=0; i < numberOfFaces; i++)
			{
				MNode2D pt1 = nodelist[trianglesIn.readInt()];
				MNode2D pt2 = nodelist[trianglesIn.readInt()];
				MNode2D pt3 = nodelist[trianglesIn.readInt()];
				submesh.addTriangle(pt1, pt2, pt3);
			}
			logger.debug("End reading");
			nodesIn.close();
			trianglesIn.close();
			refsIn.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		return submesh;
	}
	
}

