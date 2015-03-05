%{
#include <BRepFill_Pipe.hxx>
%}

class BRepFill_Pipe  {
  %rename(perform) Perform;
  %rename(spine) Spine;
  %rename(shape) Shape;
  %rename(firstShape) FirstShape;
  %rename(lastShape) LastShape;
  %rename(face) Face;
  %rename(edge) Edge;
  %rename(section) Section;
  %rename(pipeLine) PipeLine;
  public:
  BRepFill_Pipe();
  BRepFill_Pipe(const TopoDS_Wire& Spine,const TopoDS_Shape& Profile,const GeomFill_Trihedron aMode = GeomFill_IsCorrectedFrenet,const Standard_Boolean ForceApproxC1 = Standard_False,const Standard_Boolean GeneratePartCase = Standard_False);
  void Perform(const TopoDS_Wire& Spine,const TopoDS_Shape& Profile,const Standard_Boolean GeneratePartCase = Standard_False) ;
  const TopoDS_Shape& Spine() const;
  const TopoDS_Shape& Profile() const;
  const TopoDS_Shape& Shape() const;
  const TopoDS_Shape& FirstShape() const;
  const TopoDS_Shape& LastShape() const;
  TopoDS_Face Face(const TopoDS_Edge& ESpine,const TopoDS_Edge& EProfile) ;
  TopoDS_Edge Edge(const TopoDS_Edge& ESpine,const TopoDS_Vertex& VProfile) ;
  TopoDS_Shape Section(const TopoDS_Vertex& VSpine) const;
  TopoDS_Wire PipeLine(const gp_Pnt& Point) const;
};

