/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.5
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.jcae.opencascade.jni;

public class BRepFill_Pipe {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected BRepFill_Pipe(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(BRepFill_Pipe obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        OccJavaJNI.delete_BRepFill_Pipe(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public BRepFill_Pipe() {
    this(OccJavaJNI.new_BRepFill_Pipe__SWIG_0(), true);
  }

  public BRepFill_Pipe(TopoDS_Wire Spine, TopoDS_Shape Profile, GeomFill_Trihedron aMode, boolean ForceApproxC1, boolean GeneratePartCase) {
    this(OccJavaJNI.new_BRepFill_Pipe__SWIG_1(TopoDS_Wire.getCPtr(Spine), Spine, TopoDS_Shape.getCPtr(Profile), Profile, aMode.swigValue(), ForceApproxC1, GeneratePartCase), true);
  }

  public BRepFill_Pipe(TopoDS_Wire Spine, TopoDS_Shape Profile, GeomFill_Trihedron aMode, boolean ForceApproxC1) {
    this(OccJavaJNI.new_BRepFill_Pipe__SWIG_2(TopoDS_Wire.getCPtr(Spine), Spine, TopoDS_Shape.getCPtr(Profile), Profile, aMode.swigValue(), ForceApproxC1), true);
  }

  public BRepFill_Pipe(TopoDS_Wire Spine, TopoDS_Shape Profile, GeomFill_Trihedron aMode) {
    this(OccJavaJNI.new_BRepFill_Pipe__SWIG_3(TopoDS_Wire.getCPtr(Spine), Spine, TopoDS_Shape.getCPtr(Profile), Profile, aMode.swigValue()), true);
  }

  public BRepFill_Pipe(TopoDS_Wire Spine, TopoDS_Shape Profile) {
    this(OccJavaJNI.new_BRepFill_Pipe__SWIG_4(TopoDS_Wire.getCPtr(Spine), Spine, TopoDS_Shape.getCPtr(Profile), Profile), true);
  }

  public void perform(TopoDS_Wire Spine, TopoDS_Shape Profile, boolean GeneratePartCase) {
    OccJavaJNI.BRepFill_Pipe_perform__SWIG_0(swigCPtr, this, TopoDS_Wire.getCPtr(Spine), Spine, TopoDS_Shape.getCPtr(Profile), Profile, GeneratePartCase);
  }

  public void perform(TopoDS_Wire Spine, TopoDS_Shape Profile) {
    OccJavaJNI.BRepFill_Pipe_perform__SWIG_1(swigCPtr, this, TopoDS_Wire.getCPtr(Spine), Spine, TopoDS_Shape.getCPtr(Profile), Profile);
  }

  public TopoDS_Shape spine() {
    long cPtr = OccJavaJNI.BRepFill_Pipe_spine(swigCPtr, this);
    return (TopoDS_Shape)TopoDS_Shape.create(cPtr);
}

  public TopoDS_Shape Profile() {
    long cPtr = OccJavaJNI.BRepFill_Pipe_Profile(swigCPtr, this);
    return (TopoDS_Shape)TopoDS_Shape.create(cPtr);
}

  public TopoDS_Shape shape() {
    long cPtr = OccJavaJNI.BRepFill_Pipe_shape(swigCPtr, this);
    return (TopoDS_Shape)TopoDS_Shape.create(cPtr);
}

  public TopoDS_Shape firstShape() {
    long cPtr = OccJavaJNI.BRepFill_Pipe_firstShape(swigCPtr, this);
    return (TopoDS_Shape)TopoDS_Shape.create(cPtr);
}

  public TopoDS_Shape lastShape() {
    long cPtr = OccJavaJNI.BRepFill_Pipe_lastShape(swigCPtr, this);
    return (TopoDS_Shape)TopoDS_Shape.create(cPtr);
}

  public TopoDS_Face face(TopoDS_Edge ESpine, TopoDS_Edge EProfile) {
    return new TopoDS_Face(OccJavaJNI.BRepFill_Pipe_face(swigCPtr, this, TopoDS_Edge.getCPtr(ESpine), ESpine, TopoDS_Edge.getCPtr(EProfile), EProfile), true);
  }

  public TopoDS_Edge edge(TopoDS_Edge ESpine, TopoDS_Vertex VProfile) {
    return new TopoDS_Edge(OccJavaJNI.BRepFill_Pipe_edge(swigCPtr, this, TopoDS_Edge.getCPtr(ESpine), ESpine, TopoDS_Vertex.getCPtr(VProfile), VProfile), true);
  }

  public TopoDS_Shape section(TopoDS_Vertex VSpine) {
    long cPtr = OccJavaJNI.BRepFill_Pipe_section(swigCPtr, this, TopoDS_Vertex.getCPtr(VSpine), VSpine);
    return (TopoDS_Shape)TopoDS_Shape.create(cPtr);
}

  public TopoDS_Wire pipeLine(double[] Point) {
    return new TopoDS_Wire(OccJavaJNI.BRepFill_Pipe_pipeLine(swigCPtr, this, Point), true);
  }

}
