/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.5
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.jcae.opencascade.jni;

public class GeomFill_Pipe {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected GeomFill_Pipe(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(GeomFill_Pipe obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        OccJavaJNI.delete_GeomFill_Pipe(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public GeomFill_Pipe(Geom_Curve Path, double Radius) {
    this(OccJavaJNI.new_GeomFill_Pipe(Geom_Curve.getCPtr(Path), Path, Radius), true);
  }

  public void perform(boolean WithParameters, boolean myPolynomial) {
    OccJavaJNI.GeomFill_Pipe_perform__SWIG_0(swigCPtr, this, WithParameters, myPolynomial);
  }

  public void perform(boolean WithParameters) {
    OccJavaJNI.GeomFill_Pipe_perform__SWIG_1(swigCPtr, this, WithParameters);
  }

  public void perform() {
    OccJavaJNI.GeomFill_Pipe_perform__SWIG_2(swigCPtr, this);
  }

  public void perform(double Tol, boolean Polynomial, GeomAbs_Shape Conti, int MaxDegree, int NbMaxSegment) {
    OccJavaJNI.GeomFill_Pipe_perform__SWIG_3(swigCPtr, this, Tol, Polynomial, Conti.swigValue(), MaxDegree, NbMaxSegment);
  }

  public void perform(double Tol, boolean Polynomial, GeomAbs_Shape Conti, int MaxDegree) {
    OccJavaJNI.GeomFill_Pipe_perform__SWIG_4(swigCPtr, this, Tol, Polynomial, Conti.swigValue(), MaxDegree);
  }

  public void perform(double Tol, boolean Polynomial, GeomAbs_Shape Conti) {
    OccJavaJNI.GeomFill_Pipe_perform__SWIG_5(swigCPtr, this, Tol, Polynomial, Conti.swigValue());
  }

  public void perform(double Tol, boolean Polynomial) {
    OccJavaJNI.GeomFill_Pipe_perform__SWIG_6(swigCPtr, this, Tol, Polynomial);
  }

  public Geom_Surface surface() {
    return new Geom_Surface(OccJavaJNI.GeomFill_Pipe_surface(swigCPtr, this), false);
  }

  public double errorOnSurf() {
    return OccJavaJNI.GeomFill_Pipe_errorOnSurf(swigCPtr, this);
  }

}