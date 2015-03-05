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
 * (C) Copyright 2011, by EADS France
 */
 
 /*
   EXCEPTION HANDLING DOES ONLY WORK (for ...) IF YOU COMPILE WITH /EHa instead of /EHsc with MSVC
 */
 
%{
#include <Standard_ErrorHandler.hxx>
#include <BRepOffsetAPI_NormalProjection.hxx>
#include <BRepOffsetAPI_MakePipe.hxx>
#include <BRepOffsetAPI_MakePipeShell.hxx>
#include <BRepOffsetAPI_ThruSections.hxx>
%}

class BRepOffsetAPI_NormalProjection: public BRepBuilderAPI_MakeShape
{
	public:
	%rename(init) Init;
	%rename(add) Add;
	%rename(setParams) SetParams;
	%rename(setMaxDistance) SetMaxDistance;
	%rename(setLimit) SetLimit;
	%rename(compute3d) Compute3d;
	%rename(build) Build;
	%rename(isDone) IsDone;
	%rename(projection) Projection;
	%rename(couple) Couple;
	%rename(generated) Generated;
	%rename(ancestor) Ancestor;
	%rename(buildWire) BuildWire;
	BRepOffsetAPI_NormalProjection();
	BRepOffsetAPI_NormalProjection(const TopoDS_Shape& S);
	void Init(const TopoDS_Shape& S) ;
	void Add(const TopoDS_Shape& ToProj) ;
	void SetParams(const Standard_Real Tol3D,const Standard_Real Tol2D,const GeomAbs_Shape InternalContinuity,const Standard_Integer MaxDegree,const Standard_Integer MaxSeg) ;
	void SetMaxDistance(const Standard_Real MaxDist) ;
	void SetLimit(const Standard_Boolean FaceBoundaries = Standard_True) ;
	void Compute3d(const Standard_Boolean With3d = Standard_True) ;
	virtual  void Build() ;
	Standard_Boolean IsDone() const;
	const TopoDS_Shape& Projection() const;
	const TopoDS_Shape& Couple(const TopoDS_Edge& E) const;
	virtual const TopTools_ListOfShape& Generated(const TopoDS_Shape& S) ;
	const TopoDS_Shape& Ancestor(const TopoDS_Edge& E) const;
	Standard_Boolean BuildWire(TopTools_ListOfShape& Liste) const;
};

%exception BRepOffsetAPI_MakePipe
{
	try	{
	    OCC_CATCH_SIGNALS
		$action
	}
	catch(Standard_Failure) {
		cout << "build EXCEPTION IN OCE" << endl;
		// return $null;
	}
	catch (...) {
		cout << "... EXCEPTION IN OCE" << endl;
		// return $null;
	}
}

class BRepOffsetAPI_MakePipe  : public BRepPrimAPI_MakeSweep {
    public:
	%rename(build) Build;
	%rename(firstShape) FirstShape;
	%rename(lastShape) LastShape;
	%rename(generated) Generated;
	%rename(pipe) Pipe;
	BRepOffsetAPI_MakePipe(const TopoDS_Wire& Spine,const TopoDS_Shape& Profile);
	BRepOffsetAPI_MakePipe(const TopoDS_Wire& Spine,const TopoDS_Shape& Profile,const GeomFill_Trihedron aMode,const Standard_Boolean ForceApproxC1 = Standard_False);
	const BRepFill_Pipe& Pipe() const;
	virtual  void Build() ;
	TopoDS_Shape FirstShape() ;
	TopoDS_Shape LastShape() ;
	TopoDS_Shape Generated(const TopoDS_Shape& SSpine,const TopoDS_Shape& SProfile) ;
};

%exception Build
{
	try	{
	    OCC_CATCH_SIGNALS
		$action
	}
	catch(Standard_Failure) {
		cout << "build EXCEPTION IN OCE" << endl;
		/*return $null;*/
	}
	catch (...) {
		cout << "... EXCEPTION IN OCE" << endl;
	}
}

%exception Add
{
	try	{
		OCC_CATCH_SIGNALS
		$action
	}
	catch(Standard_Failure) {
		cout << "add EXCEPTION IN OCE" << endl;
		/*return $null;*/
	}
	catch (...) {
		cout << "... EXCEPTION IN OCE" << endl;
	}
}

class BRepOffsetAPI_MakePipeShell  : public BRepPrimAPI_MakeSweep {
	public:
	%rename(setModeAx2) SetMode(const gp_Ax2& Axe);
	%rename(setModeBiNormal) SetMode(const gp_Dir& BiNormal);
	%rename(setDiscreteMode) SetDiscreteMode;
	%rename(add1) Add(const TopoDS_Shape& Profile,const Standard_Boolean WithContact = Standard_False,const Standard_Boolean WithCorrection = Standard_False) ;
	%rename(add2) Add(const TopoDS_Shape& Profile,const TopoDS_Vertex& Location,const Standard_Boolean WithContact = Standard_False,const Standard_Boolean WithCorrection = Standard_False) ;
	/*%rename(setLaw) SetLaw;*/
	/*%rename(delete) Delete;*/
	%rename(isReady) IsReady;
	%rename(getStatus) GetStatus;
	%rename(setTolerance) SetTolerance;
	%rename(setForceApproxC1) SetForceApproxC1;
	%rename(setTransitionMode) SetTransitionMode;
	%rename(simulate) Simulate;
	%rename(build) Build;
	%rename(makeSolid) MakeSolid;
	%rename(firstShape) FirstShape;
	%rename(lastShape) LastShape;
	%rename(generated) Generated;
	BRepOffsetAPI_MakePipeShell(const TopoDS_Wire& Spine);
	void SetMode(const Standard_Boolean IsFrenet = Standard_False) ;
	void SetDiscreteMode() ;
	void SetMode(const gp_Ax2& Axe) ;
	void SetMode(const gp_Dir& BiNormal) ;
	Standard_Boolean SetMode(const TopoDS_Shape& SpineSupport) ;
	void SetMode(const TopoDS_Wire& AuxiliarySpine,const Standard_Boolean CurvilinearEquivalence,const BRepFill_TypeOfContact KeepContact = BRepFill_NoContact) ;
	void Add(const TopoDS_Shape& Profile,const Standard_Boolean WithContact = Standard_False,const Standard_Boolean WithCorrection = Standard_False) ;
	void Add(const TopoDS_Shape& Profile,const TopoDS_Vertex& Location,const Standard_Boolean WithContact = Standard_False,const Standard_Boolean WithCorrection = Standard_False) ;
	
	/*void SetLaw(const TopoDS_Shape& Profile,const Handle_Law_Function& L,const Standard_Boolean WithContact = Standard_False,const Standard_Boolean WithCorrection = Standard_False) ;
	void SetLaw(const TopoDS_Shape& Profile,const Handle_Law_Function& L,const TopoDS_Vertex& Location,const Standard_Boolean WithContact = Standard_False,const Standard_Boolean WithCorrection = Standard_False) ;*/
	
	void Delete(const TopoDS_Shape& Profile) ;
	Standard_Boolean IsReady() const;
	BRepBuilderAPI_PipeError GetStatus() const;
	void SetTolerance(const Standard_Real Tol3d = 1.0e-4,const Standard_Real BoundTol = 1.0e-4,const Standard_Real TolAngular = 1.0e-2) ;
	void SetForceApproxC1(const Standard_Boolean ForceApproxC1) ;
	void SetTransitionMode(const BRepBuilderAPI_TransitionMode Mode = BRepBuilderAPI_Transformed) ;
	void Simulate(const Standard_Integer NumberOfSection,TopTools_ListOfShape& Result) ;
	virtual  void Build() ;
	Standard_Boolean MakeSolid() ;
	virtual  TopoDS_Shape FirstShape() ;
	virtual  TopoDS_Shape LastShape() ;
	virtual const TopTools_ListOfShape& Generated(const TopoDS_Shape& S) ;
};

class BRepOffsetAPI_ThruSections  : public BRepBuilderAPI_MakeShape
{
	%rename(addWire) AddWire;
	%rename(setParType) SetParType;
	public:
	BRepOffsetAPI_ThruSections(const Standard_Boolean isSolid = Standard_False,const Standard_Boolean ruled = Standard_False,const Standard_Real pres3d = 1.0e-06);
	void AddWire(const TopoDS_Wire& wire);
	void SetParType(const Approx_ParametrizationType ParType);
};
