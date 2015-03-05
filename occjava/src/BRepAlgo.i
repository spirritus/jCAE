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
 * (C) Copyright 2005, by EADS CRC
 */

%{#include <BRepAlgo.hxx>%}
%{#include <BRepAlgo_BooleanOperation.hxx>%}
%{#include <BRepAlgo_Cut.hxx>%}

class BRepAlgo
{
	%rename(isValid) IsValid;
	%rename(isTopologicallyValid) IsTopologicallyValid;
	public:	
	static Standard_Boolean IsValid(const TopoDS_Shape& S);
	static Standard_Boolean IsTopologicallyValid(const TopoDS_Shape& S);
};

class BRepAlgo_BooleanOperation: public BRepBuilderAPI_MakeShape
{
	%rename(performDS) PerformDS;
	%rename(perform) Perform;
	%rename(builder) Builder;
	%rename(shape1) Shape1;
	%rename(shape1) Shape1;
	%rename(modified) Modified;
	BRepAlgo_BooleanOperation()=0;
	public:
    virtual  void Delete() ;
	virtual ~BRepAlgo_BooleanOperation() {Delete();}
	void PerformDS() ;
	void Perform(const TopAbs_State St1,const TopAbs_State St2) ;
	Handle_TopOpeBRepBuild_HBuilder Builder() const;
	const TopoDS_Shape& Shape1() const;
	const TopoDS_Shape& Shape2() const;
	virtual const TopTools_ListOfShape& Modified(const TopoDS_Shape& S) ;
	virtual  Standard_Boolean IsDeleted(const TopoDS_Shape& S) ;
};

class BRepAlgo_Cut  : public BRepAlgo_BooleanOperation {
  public:
  BRepAlgo_Cut(const TopoDS_Shape& S1,const TopoDS_Shape& S2);
};
