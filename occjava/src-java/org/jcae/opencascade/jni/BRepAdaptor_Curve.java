/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.5
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.jcae.opencascade.jni;

public class BRepAdaptor_Curve extends Adaptor3d_Curve {
  private long swigCPtr;

  protected BRepAdaptor_Curve(long cPtr, boolean cMemoryOwn) {
    super(OccJavaJNI.BRepAdaptor_Curve_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(BRepAdaptor_Curve obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        OccJavaJNI.delete_BRepAdaptor_Curve(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public BRepAdaptor_Curve() {
    this(OccJavaJNI.new_BRepAdaptor_Curve__SWIG_0(), true);
  }

  public BRepAdaptor_Curve(TopoDS_Edge E) {
    this(OccJavaJNI.new_BRepAdaptor_Curve__SWIG_1(TopoDS_Edge.getCPtr(E), E), true);
  }

  public BRepAdaptor_Curve(TopoDS_Edge E, TopoDS_Face F) {
    this(OccJavaJNI.new_BRepAdaptor_Curve__SWIG_2(TopoDS_Edge.getCPtr(E), E, TopoDS_Face.getCPtr(F), F), true);
  }

  public void initialize(TopoDS_Edge E) {
    OccJavaJNI.BRepAdaptor_Curve_initialize__SWIG_0(swigCPtr, this, TopoDS_Edge.getCPtr(E), E);
  }

  public void initialize(TopoDS_Edge E, TopoDS_Face F) {
    OccJavaJNI.BRepAdaptor_Curve_initialize__SWIG_1(swigCPtr, this, TopoDS_Edge.getCPtr(E), E, TopoDS_Face.getCPtr(F), F);
  }

  public GP_Trsf trsf() {
    return new GP_Trsf(OccJavaJNI.BRepAdaptor_Curve_trsf(swigCPtr, this), false);
  }

  public boolean is3DCurve() {
	return OccJavaJNI.BRepAdaptor_Curve_is3DCurve(swigCPtr, this);
}

  public boolean isCurveOnSurface() {
	return OccJavaJNI.BRepAdaptor_Curve_isCurveOnSurface(swigCPtr, this);
}

  public GeomAdaptor_Curve curve() {
    return new GeomAdaptor_Curve(OccJavaJNI.BRepAdaptor_Curve_curve(swigCPtr, this), false);
  }

  public SWIGTYPE_p_Adaptor3d_CurveOnSurface curveOnSurface() {
    return new SWIGTYPE_p_Adaptor3d_CurveOnSurface(OccJavaJNI.BRepAdaptor_Curve_curveOnSurface(swigCPtr, this), false);
  }

  public TopoDS_Edge edge() {
    return new TopoDS_Edge(OccJavaJNI.BRepAdaptor_Curve_edge(swigCPtr, this), false);
  }

  public double tolerance() {
    return OccJavaJNI.BRepAdaptor_Curve_tolerance(swigCPtr, this);
  }

  public double firstParameter() {
    return OccJavaJNI.BRepAdaptor_Curve_firstParameter(swigCPtr, this);
  }

  public double lastParameter() {
    return OccJavaJNI.BRepAdaptor_Curve_lastParameter(swigCPtr, this);
  }

  public boolean isClosed() {
	return OccJavaJNI.BRepAdaptor_Curve_isClosed(swigCPtr, this);
}

  public boolean isPeriodic() {
	return OccJavaJNI.BRepAdaptor_Curve_isPeriodic(swigCPtr, this);
}

  public double period() {
    return OccJavaJNI.BRepAdaptor_Curve_period(swigCPtr, this);
  }

  public double[] value(double U) {
	return OccJavaJNI.BRepAdaptor_Curve_value(swigCPtr, this, U);
}

  public SWIGTYPE_p_GeomAbs_CurveType getType() {
    return new SWIGTYPE_p_GeomAbs_CurveType(OccJavaJNI.BRepAdaptor_Curve_getType(swigCPtr, this), true);
  }

}
