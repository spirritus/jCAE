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
 * (C) Copyright 2005-2010, by EADS France
 */

package org.jcae.netbeans.mesh;

import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;

/**
 * @author Jerome Robert
 */
public class SmoothParameters extends PropertySheet
{
	private class MyProperty<T> extends PropertySupport.Reflection<T>
	{
		public MyProperty(Class<T> type, String property, String name)
			throws NoSuchMethodException
		{
			this(type, property, name, name);
		}
		public MyProperty(Class<T> type, String property, String name, String description)
			throws NoSuchMethodException
		{
			super(SmoothParameters.this, type, property);
			setName(name);
			setShortDescription(description);
		}
	}
	/** Creates a new instance of SmoothParameters */
	public SmoothParameters()
	{
		AbstractNode node = new AbstractNode(Children.LEAF)
		{
			@Override
			public PropertySet[] getPropertySets() {
				return new PropertySet[]{createPropertySet()};
			}
		};
		setNodes(new Node[]{node});
		setDescriptionAreaVisible(true);
		setPreferredSize(new Dimension(0, 200));
	}

	private Sheet.Set createPropertySet()
	{
		Sheet.Set r = new Sheet.Set();
		r.setName("Parameters");
		try {			
			r.put(new MyProperty<Double>(Double.TYPE, "elementSize", "Element size",
				"Target edge size"));
			r.put(new MyProperty<Integer>(Integer.TYPE, "iterationNumber",
				"Iteration number", ""));
			r.put(new MyProperty<Boolean>(Boolean.TYPE, "preserveGroups",
				"Preserve groups",
				"Edges adjacent to two different groups are handled like free edges."));
			r.put(new MyProperty<Double>(Double.TYPE, "coplanarity",
				"Coplanarity",
				"Dot product of face normals to detect feature edges. "+
				"A negative value disable feature edges detection."));
			r.put(new MyProperty<Double>(Double.TYPE, "tolerance",
				"Tolerance",
				"Process only nodes with quality lower than this value."));
		} catch (NoSuchMethodException ex) {
			Exceptions.printStackTrace(ex);
		}
		return r;
	}

	public boolean showDialog()
	{
        JOptionPane jp = new JOptionPane(this,
             JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		JDialog d = jp.createDialog("Smoothing parameters");
		d.setResizable(true);
		d.setVisible(true);
        return Integer.valueOf(JOptionPane.OK_OPTION).equals(jp.getValue());
	}

	private double elementSize=-1;
	private int iterationNumber=10;
	private boolean preserveGroups = true;
	private double coplanarity = 0.95, tolerance = 2.0;

	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}
	
	public double getCoplanarity() {
		return coplanarity;
	}

	public void setCoplanarity(double coplanarity) {
		this.coplanarity = coplanarity;
	}

	public double getElementSize()
	{
		return this.elementSize;
	}

	public void setElementSize(double elementSize)
	{
		this.elementSize = elementSize;
	}

	public int getIterationNumber()
	{
		return this.iterationNumber;
	}

	public void setIterationNumber(int iterationNumber)
	{
		this.iterationNumber = iterationNumber;
	}

	/**
	 * @return the preserveGroups
	 */ public boolean isPreserveGroups() {
		return preserveGroups;
	}

	/**
	 * @param preserveGroups the preserveGroups to set
	 */ public void setPreserveGroups(boolean preserveGroups) {
		this.preserveGroups = preserveGroups;
	}
}
