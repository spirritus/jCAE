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
 * (C) Copyright 2012, by EADS France
 */
package org.jcae.mesh.amibe.algos3d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.jcae.mesh.amibe.ds.AbstractHalfEdge;
import org.jcae.mesh.amibe.ds.Mesh;
import org.jcae.mesh.amibe.ds.Vertex;
import org.jcae.mesh.amibe.metrics.MetricSupport.AnalyticMetricInterface;
import org.jcae.mesh.amibe.projection.MeshLiaison;

/**
 *
 * @author Jerome Robert
 */
public class RemeshSkeleton {
	private final Mesh mesh;
	private final double angle;
	/** tolerance for point insertion */
	private final double tolerance;
	private final AnalyticMetricInterface metric;
	private final MeshLiaison liaison;
	public RemeshSkeleton(MeshLiaison liaison, double angle, double tolerance,
		final double size) {
		this(liaison, angle, tolerance, new AnalyticMetricInterface() {

			public double getTargetSize(double x, double y, double z, int groupId) {
				return size;
			}

			public double getTargetSizeTopo(Mesh mesh, Vertex v) {
				return size;
			}
		});
	}

	public RemeshSkeleton(MeshLiaison liaison, double angle, double tolerance,
		AnalyticMetricInterface metric) {
		this.liaison = liaison;
		this.mesh = liaison.getMesh();
		this.angle = angle;
		this.tolerance = tolerance * tolerance;
		this.metric = metric;
	}

	private AbstractHalfEdge getEdge(List<Vertex> polyline, int segId)
	{
		return getEdge(polyline.get(segId), polyline.get(segId+1));
	}

	private AbstractHalfEdge getEdge(Vertex v1, Vertex v2)
	{
		Iterator<AbstractHalfEdge> it = v1.getNeighbourIteratorAbstractHalfEdge();
		while(it.hasNext())
		{
			AbstractHalfEdge e = it.next();
			if(e.destination() == v2)
			{
				if(e.hasAttributes(AbstractHalfEdge.OUTER))
					return e.sym();
				else
					return e;
			}
		}
		//TODO in some cases v1.getNeighbourIteratorAbstractHalfEdge does not
		//return all HE. Check why. Star with vertex with more than one outer
		//triangle (more than one group/group frontier).
		it = v2.getNeighbourIteratorAbstractHalfEdge();
		while(it.hasNext())
		{
			AbstractHalfEdge e = it.next();
			if(e.destination() == v1)
			{
				if(e.hasAttributes(AbstractHalfEdge.OUTER))
					return e.sym();
				else
					return e;
			}
		}
		throw new NoSuchElementException(v1+" "+v2);
	}

	public void compute()
	{
		Skeleton skeleton = new Skeleton(mesh, angle);
		main: for(List<Vertex> polyline: skeleton.getPolylinesVertices())
		{
			RemeshPolyline rp = new RemeshPolyline(mesh, polyline, metric);
			rp.setBuildBackgroundLink(true);
			List<Vertex> toInsert = rp.compute();
			List<Integer> bgLink = rp.getBackgroundLink();
			int k = 0;
			final HashSet<Vertex> toKeep = new HashSet<Vertex>(toInsert);
			ArrayList<AbstractHalfEdge> edgeIndex = new ArrayList<AbstractHalfEdge>(polyline.size()-1);
			for(int i = 0; i < polyline.size() - 1; i++)
				edgeIndex.add(getEdge(polyline, i));

			for(Vertex v:toInsert)
			{
				int segId = bgLink.get(k++);
				AbstractHalfEdge toSplit = edgeIndex.get(segId);
				if(v.sqrDistance3D(toSplit.origin()) < tolerance)
				{
					toKeep.remove(v);
					toKeep.add(toSplit.origin());
				}
				else if(v.sqrDistance3D(toSplit.destination()) < tolerance)
				{
					toKeep.remove(v);
					toKeep.add(toSplit.destination());
				}
				else
				{
					Vertex oldDestination = toSplit.destination();
					mesh.vertexSplit(toSplit, v);
					liaison.addVertex(v, toSplit.getTri());
					//TODO this will be slow as as toSplit.getTri() may be far
					//from the wanted triangle so we will loop on all triangles
					liaison.move(v, v.getUV(), true);
					Iterator<AbstractHalfEdge> it = v.getNeighbourIteratorAbstractHalfEdge();
					while(it.hasNext())
					{
						AbstractHalfEdge newEdge = it.next();
						if(newEdge.destination() == oldDestination)
						{
							edgeIndex.set(segId, newEdge);
							break;
						}
					}
				}
			}
			HashSet<Vertex> toCollapse = new HashSet<Vertex>(polyline);
			toCollapse.removeAll(toKeep);
			collapse(toKeep, toCollapse);
		}
	}

	private void collapse(final Set<Vertex> toKeep, Set<Vertex> toCollapse)
	{
		boolean changed = true;
		while(changed)
		{
			//check(toCollapse);
			changed = false;
			Iterator<Vertex> itToCollapse = toCollapse.iterator();
			while(itToCollapse.hasNext())
			{
				Vertex v = itToCollapse.next();
				Iterator<AbstractHalfEdge> it = v.getNeighbourIteratorAbstractHalfEdge();
				while(it.hasNext())
				{
					AbstractHalfEdge edge = it.next();
					Vertex dest = edge.destination();
					if(toKeep.contains(dest) && mesh.canCollapseEdge(edge, dest))
					{
						mesh.edgeCollapse(edge, dest);
						liaison.removeVertex(v);
						changed = true;
						itToCollapse.remove();
						break;
					}
				}
			}
		}
	}
}