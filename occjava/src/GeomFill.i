%{
#include <GeomFill_Pipe.hxx>
%}

class GeomFill_Pipe  {
	%rename(perform) Perform;
	%rename(surface) Surface;
	%rename(errorOnSurf) ErrorOnSurf;
	public:
	GeomFill_Pipe(const Handle_Geom_Curve& Path,const Standard_Real Radius);
	void Perform(const Standard_Boolean WithParameters = Standard_False,const Standard_Boolean myPolynomial = Standard_False) ;
	void Perform(const Standard_Real Tol,const Standard_Boolean Polynomial,const GeomAbs_Shape Conti = GeomAbs_C1,const Standard_Integer MaxDegree = 11,const Standard_Integer NbMaxSegment = 30) ;
	const Handle_Geom_Surface& Surface() const;
	Standard_Real ErrorOnSurf() const;
};