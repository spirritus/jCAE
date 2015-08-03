/*
 * Project Info:  http://jcae.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 *
 * (C) Copyright 2014, by Airbus Group SAS
 */


package org.jcae.mesh.amibe.projection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jcae.mesh.amibe.algos3d.TriMultPoly;
import org.jcae.mesh.amibe.ds.AbstractHalfEdge;
import org.jcae.mesh.amibe.ds.Mesh;
import org.jcae.mesh.amibe.ds.Triangle;
import org.jcae.mesh.amibe.ds.Vertex;
import org.jcae.mesh.amibe.traits.MeshTraitsBuilder;
import org.jcae.mesh.xmldata.Amibe2VTK;
import org.jcae.mesh.xmldata.MeshReader;
import org.jcae.mesh.xmldata.MeshWriter;

/**
 * Insert an edge into a triangulation.
 * Triangles under the edge are removed, then the created hole is remeshed
 * taking the inserted edge into account.
 * @author Jerome Robert
 */
public class EdgeProjectorNG {
	private final Mesh mesh;
	private final TriangleKdTree kdTree;
	private final double tolerance;
	private final EdgeTrianglesLocator triangleFinder;
    private final TriMultPoly triMultPoly = new TriMultPoly();
	private final HoleFiller holeFiller = new HoleFiller();

	public EdgeProjectorNG(Mesh mesh, TriangleKdTree kdTree, double tolerance) {
		this.mesh = mesh;
		this.kdTree = kdTree;
		this.tolerance = tolerance;
		triangleFinder = new EdgeTrianglesLocator(kdTree) {
			@Override
			protected boolean isValidTriangle(Triangle t) {
				return isProjectionAllowed(t);
			}
		};
	}

	/** Ensure that removing triangles would not remove not removable edges */
	private boolean checkInternalEdges(Collection<Triangle> triangles) {
		boolean toReturn = true;
		Iterator<Triangle> it = triangles.iterator();
		mainLoop: while(it.hasNext()) {
			Triangle t = it.next();
			AbstractHalfEdge e = t.getAbstractHalfEdge();
			for(int i = 0; i < 3; i++) {
				boolean isInternalEdge = triangles.contains(e.sym().getTri());
				if(isInternalEdge && !isEdgeRemovable(e)) {
					it.remove();
					toReturn = false;
					continue mainLoop;
				}
				e = e.next();
			}
		}
		return toReturn;
	}

	public AbstractHalfEdge project(Vertex v1, Vertex v2, int group)
	{
		assert v1 != null;
		assert v2 != null;
		triangleFinder.locate(v1, v2, group, tolerance);
		ArrayList<AbstractHalfEdge> border = new ArrayList<AbstractHalfEdge>();
		for(Triangle t:triangleFinder.getResult())
		{
			AbstractHalfEdge e = t.getAbstractHalfEdge();
			for(int i = 0; i < 3; i++)
			{
				if(!triangleFinder.getResult().contains(e.sym().getTri()))
					border.add(e);
				e = e.next();
			}
		}
		if(!checkInternalEdges(triangleFinder.getResult()))
			return null;
		if(border.isEmpty())
			return null;
		holeFiller.triangulate(mesh, border,
			Collections.singleton(Arrays.asList(v1, v2)));
		for(Triangle t:triangleFinder.getResult())
		{
			mesh.remove(t);
			kdTree.remove(t);
		}
		for(Triangle t:holeFiller.getNewTriangles())
		{
			mesh.add(t);
			kdTree.addTriangle(t);
		}
		assert mesh.isValid();
		return holeFiller.getEdge(v1, v2);
	}

	private boolean checkVertexLinks() {
		for(Triangle t: mesh.getTriangles())
		{
			for(int i = 0; i < 3; i++) {
				Vertex v = t.getV(i);
				if(v.getLink() == null && v != mesh.outerVertex) {
					System.err.println("invalid vertex: "+v);
					return false;
				}
			}
		}
		return true;
	}

	public void projectTriMultPoly(Vertex v1, Vertex v2, int group) throws IOException
	{
		//TODO make it work with delauney
		triMultPoly.setDelauneyTetra(false);
		triangleFinder.locate(v1, v2, group, tolerance);
		ArrayList<AbstractHalfEdge> border = new ArrayList<AbstractHalfEdge>();
		for(Triangle t:triangleFinder.getResult())
		{
			AbstractHalfEdge e = t.getAbstractHalfEdge();
			for(int i = 0; i < 3; i++)
			{
				if(!triangleFinder.getResult().contains(e.sym().getTri()))
					border.add(e);
				e = e.next();
			}
		}
		Vertex[] vToAdd = new Vertex[3];
		vToAdd[0] = v2;
		vToAdd[1] = v1;
		// Add a 3rd point so that the polyline is not degenerated
		vToAdd[2] = mesh.createVertex(
			v1.getX() - 1E-9 * (v1.getX() - v2.getX()),
			v1.getY(), v1.getZ());
		vToAdd[2] = mesh.createVertex(v1.getX(), v2.getY(), v1.getZ());
		triMultPoly.triangulate(mesh, border,
			Collections.singleton(Arrays.asList(vToAdd)));
		AbstractHalfEdge e = triMultPoly.getEdge(v1, vToAdd[2]);
		for(Triangle t:triMultPoly.getNewTriangles())
			mesh.add(t);
		for(Triangle t:triangleFinder.getResult())
			mesh.remove(t);
		assert mesh.isValid();
		mesh.edgeCollapse(e, v1);
	}

	/**
	 * Return true is the projection can be done on this triangle.
	 * This methods aims at being redefine in subclasses.
	 * The default implementation return true.
	 */
	protected boolean isProjectionAllowed(Triangle triangle)
	{
		return true;
	}

	protected boolean isEdgeRemovable(AbstractHalfEdge edge) {
		return true;
	}

	public static void test()
	{
		Mesh m = new Mesh(MeshTraitsBuilder.getDefault3D());
		Vertex v1 = m.createVertex(0,0,0);
		Vertex v2 = m.createVertex(1,0,0);
		Vertex v3 = m.createVertex(0,1,0);
		Vertex v4 = m.createVertex(0.1,0.1,0);
		Vertex v5 = m.createVertex(0.2,0.2,0);
		m.add(m.createTriangle(v1, v2, v3));
		m.buildAdjacency();
		TriangleKdTree mkdTree = new TriangleKdTree(m);
		EdgeProjectorNG projector = new EdgeProjectorNG(m, mkdTree, 0.1);
		projector.project(v4, v5, -1);
		assert m.isValid();
	}

	public static void main(final String[] args) {
		try {
			test();
			String file = "/tmp/A350-1000_FULL-Config_Masked_Shape3422.amibe";
			Mesh m = new Mesh(MeshTraitsBuilder.getDefault3D());
			MeshReader.readObject3D(m, file);
			Vertex v1 = new Vertex(null, 5969.1334634938175, 602.1781046704872, -2639.907227074926);
			Vertex v2 = new Vertex(null, 5974.089704834468, 576.5380328193008, -2675.8479767020112);
			TriangleKdTree mkdTree = new TriangleKdTree(m);
			EdgeProjectorNG projector = new EdgeProjectorNG(m, mkdTree, 0.1);
			projector.project(v1, v2, -1);
			String tmpFile = "/tmp/debug.amibe";
			MeshWriter.writeObject3D(m, tmpFile, null);
			new Amibe2VTK(tmpFile).write("/tmp/debug.vtp");
		} catch (Exception ex) {
			Logger.getLogger(EdgeProjectorNG.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
