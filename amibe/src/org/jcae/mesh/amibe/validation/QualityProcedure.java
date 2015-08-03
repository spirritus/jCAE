/* jCAE stand for Java Computer Aided Engineering. Features are : Small CAD
   modeler, Finite element mesher, Plugin architecture.

    Copyright (C) 2005, by EADS CRC

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

package org.jcae.mesh.amibe.validation;

import org.jcae.mesh.amibe.traits.MeshTraitsBuilder;
import gnu.trove.list.array.TFloatArrayList;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.jcae.mesh.amibe.ds.Triangle;

/**
 * Abstract class to compute quality criteria.
 * All functions implementing quality criteria have to inherit from
 * this class.  These functions can compute values either on elements
 * or nodes, and can work either on 2D or 3D meshes.
 *
 * For these reasons, <code>quality</code> method's argument is an
 * <code>Object</code>, and caller is responsible for passing the
 * right argument.
 */
public abstract class QualityProcedure
{
	public static final int FACE = 1;
	public static final int NODE = 2;
	public static final int EDGE = 3;
	
	// By default, values are computed by faces.
	int type = FACE;
	String [] usageStr;
	private TFloatArrayList data;
	
	private static final Class<QualityProcedure> [] subClasses = new Class[]{
		// AbsoluteDeflection2D.class,  Disabled for now
		Area.class,
		DihedralAngle.class,
		MaxAngleFace.class,
		MaxLengthFace.class,
		MinAngleFace.class,
		MinLengthFace.class,
		NodeConnectivity.class,
		null
	};

	public static String [] getListSubClasses()
	{
		String [] ret = new String[2*subClasses.length-2];
		int idx = 0;
		try
		{
			for (Class<QualityProcedure> c: subClasses)
			{
				if (c == null)
					break;
				Constructor<QualityProcedure> cons = c.getConstructor();
				QualityProcedure qproc = cons.newInstance();
				String [] str = qproc.usageStr;
				ret[2*idx] = str[0];
				ret[2*idx+1] = str[1];
				idx++;
			}
		}
		catch (NoSuchMethodException ex)
		{
		}
		catch (IllegalAccessException ex)
		{
		}
		catch (InstantiationException ex)
		{
		}
		catch (InvocationTargetException ex)
		{
		}
		return ret;
	}

	protected abstract void setValidationFeatures();
	QualityProcedure()
	{
		setValidationFeatures();
	}

	/**
	 * Return element type.
	 *
	 * @return element type
	 */
	public final int getType()
	{
		return type;
	}
	
	/**
	 * Return the quality factor for a given object.
	 *
	 * @param o  entity at which quality is computed
	 * Returns quality factor.
	 */
	public abstract float quality(Object o);
	
	/**
	 * Returns default scale factor.
	 */
	final float getScaleFactor()
	{
		return 1.0f;
	}
	
	/**
	 * Returns <code>MeshTraitsBuilder</code> instance needed by this class.
	 */
	public MeshTraitsBuilder getMeshTraitsBuilder()
	{
		return new MeshTraitsBuilder();
	}
	
	/**
	 * Finish quality computations.
	 * By default, this method does nothing and can be overriden
	 * when post-processing is needed.
	 */
	public void finish()
	{
	}
	
	/**
	 * Make output array visible by the {@link #finish} method.
	 * @see QualityFloat#setQualityProcedure
	 *
	 * @param d  array containing output values
	 */
	public final void bindResult(TFloatArrayList d)
	{
		data = d;
	}

	/**
	 * Return the highest or lowest quality triangles in the list
	 * @param qualityProcedure The quality procedure to use
	 * @param input The list of Triangle to test
	 * @param nb The number of triangles to return. If the number is positive
	 * the lowest quality triangles are return else the highest quality triangles
	 * are return
	 * @return The triangles with the lowest or highest quality
	 */
	public static List<Triangle> worstTriangles(QualityProcedure qualityProcedure,
		final List<Triangle> input, final int nb, final boolean reverse)
	{
		Integer[] indices = new Integer[input.size()];
		for (int i = 0; i < indices.length; i++) {
			indices[i] = i;
		}
		final float[] qualities = new float[indices.length];
		int k = 0;
		for (Triangle t : input) {
			qualities[k++] = qualityProcedure.quality(t);
		}
		Arrays.sort(indices, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				if(reverse)
					return Float.compare(qualities[o2], qualities[o1]);
				else
					return Float.compare(qualities[o1], qualities[o2]);
			}
		});
		Triangle[] toReturn;
		if (nb > 0) {
			toReturn = new Triangle[nb];
			for (int i = 0; i < nb; i++)
				toReturn[i] = input.get(indices[i]);
		} else {
			toReturn = new Triangle[-nb];
			for (int i = 0; i < -nb; i++)
				toReturn[i] = input.get(indices[indices.length - i - 1]);
		}
		return Arrays.asList(toReturn);
	}
}
