/* jCAE stand for Java Computer Aided Engineering. Features are : Small CAD
   modeler, Finite element mesher, Plugin architecture.

    Copyright (C) 2006, by EADS CRC
    Copyright (C) 2007, by EADS France

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

package org.jcae.mesh.amibe.traits;

public class TriangleTraitsBuilder extends TraitsBuilder
{
	private static final int BITVIRTUALHALFEDGE = 13;
	private static final int BITHALFEDGE     = 14;

	public static final int VIRTUALHALFEDGE  = 1 << BITVIRTUALHALFEDGE;
	public static final int HALFEDGE         = 1 << BITHALFEDGE;

	/**
	 * Let {@link org.jcae.mesh.amibe.ds.ElementFactory#createTriangle} create
	 * {@link org.jcae.mesh.amibe.ds.TriangleVH} instances.
	 *
	 * @return  this instance
	 */
	public final TriangleTraitsBuilder addVirtualHalfEdge()
	{
		attributes |= VIRTUALHALFEDGE;
		attributes &= ~HALFEDGE;
		return this;
	}

	/**
	 * Let {@link org.jcae.mesh.amibe.ds.ElementFactory#createTriangle} create
	 * {@link org.jcae.mesh.amibe.ds.TriangleHE} instances.
	 *
	 * @return  this instance
	 */
	public final TriangleTraitsBuilder addHalfEdge()
	{
		attributes |= HALFEDGE;
		attributes &= ~VIRTUALHALFEDGE;
		return this;
	}

	/**
	 * Tells whether adjacency relations are created with {@link
	 * org.jcae.mesh.amibe.ds.HalfEdge} instances.
	 *
	 * @return <code>true</code> if {@link #addHalfEdge}
	 * was called, <code>false</code> otherwise.
	 */
	public final boolean hasHalfEdge()
	{
		return hasCapability(HALFEDGE);
	}

	/**
	 * Tells whether adjacency relations are created with {@link
	 * org.jcae.mesh.amibe.ds.VirtualHalfEdge} instances.
	 *
	 * @return <code>true</code> if {@link #addVirtualHalfEdge}
	 * was called, <code>false</code> otherwise.
	 */
	public final boolean hasVirtualHalfEdge()
	{
		return hasCapability(VIRTUALHALFEDGE);
	}

	// For performance reasons, adjacency relations are not stored in
	// traits, but directly in Triangle subclass.

}
