/**
 * Generated by Agitar build: JUnitFactory Version 2.1.2.000677 (Build date: Dec 21, 2007) [2.1.2.000677]
 * JDK Version: 1.5.0_11
 *
 * Generated on Jan 5, 2008 7:26:28 AM
 * Time to generate: 00:11.989 seconds
 *
 *
 *
 */

package org.gavaghan.geodesy.internal;

import junit.framework.TestCase;

public class EllipsoidAgitarTest extends TestCase  {
    
    public Class<Ellipsoid> getTargetClass()  {
        return Ellipsoid.class;
    }
    
//    public void testConstructor() throws Throwable {
//        Ellipsoid ellipsoid = (Ellipsoid) callPrivateMethod("org.gavaghan.geodesy.Ellipsoid", "<init>", new Class[] {double.class, double.class, double.class, double.class}, null, new Object[] {new Double(100.0), new Double(1000.0), new Double(0.0), new Double(-1.0)});
//        assertEquals("ellipsoid.getSemiMajorAxis()", 100.0, ellipsoid.getSemiMajorAxis(), 1.0E-6);
//        assertEquals("ellipsoid.getFlattening()", 0.0, ellipsoid.getFlattening(), 1.0E-6);
//        assertEquals("ellipsoid.getSemiMinorAxis()", 1000.0, ellipsoid.getSemiMinorAxis(), 1.0E-6);
//        assertEquals("ellipsoid.getInverseFlattening()", -1.0, ellipsoid.getInverseFlattening(), 1.0E-6);
//    }
    
    public void testFromAAndF() throws Throwable {
        Ellipsoid result = Ellipsoid.fromAAndF(100.0, 1000.0);
        assertEquals("result.getFlattening()", 1000.0, result.getFlattening(), 1.0E-6);
    }
    
    public void testFromAAndInverseF() throws Throwable {
        Ellipsoid result = Ellipsoid.fromAAndInverseF(100.0, 1000.0);
        assertEquals("result.getFlattening()", 0.0010, result.getFlattening(), 1.0E-6);
    }
    
    public void testGetFlattening() throws Throwable {
        double result = EllipsoidTestHelper.createSphere().getFlattening();
        assertEquals("result", 0.0, result, 1.0E-6);
    }
    
    public void testGetFlattening1() throws Throwable {
        double result = EllipsoidTestHelper.createGRS80().getFlattening();
        assertEquals("result", 0.003352810681182319, result, 1.0E-6);
    }
    
//    public void testGetInverseFlattening() throws Throwable {
//        Ellipsoid ellipsoid = (Ellipsoid) callPrivateMethod("org.gavaghan.geodesy.Ellipsoid", "<init>", new Class[] {double.class, double.class, double.class, double.class}, null, new Object[] {new Double(100.0), new Double(1000.0), new Double(0.0), new Double(0.0)});
//        double result = ellipsoid.getInverseFlattening();
//        assertEquals("result", 0.0, result, 1.0E-6);
//    }
    
    public void testGetInverseFlattening1() throws Throwable {
        double result = EllipsoidTestHelper.createGRS80().getInverseFlattening();
        assertEquals("result", 298.257222101, result, 1.0E-6);
    }
    
    public void testGetSemiMajorAxis() throws Throwable {
        double result = EllipsoidTestHelper.createGRS80().getSemiMajorAxis();
        assertEquals("result", 6378137.0, result, 1.0E-6);
    }
    
//    public void testGetSemiMinorAxis() throws Throwable {
//        Ellipsoid ellipsoid = (Ellipsoid) callPrivateMethod("org.gavaghan.geodesy.Ellipsoid", "<init>", new Class[] {double.class, double.class, double.class, double.class}, null, new Object[] {new Double(100.0), new Double(0.0), new Double(1000.0), new Double(-1.0)});
//        double result = ellipsoid.getSemiMinorAxis();
//        assertEquals("result", 0.0, result, 1.0E-6);
//    }
    
    public void testGetSemiMinorAxis1() throws Throwable {
        double result = EllipsoidTestHelper.createGRS80().getSemiMinorAxis();
        assertEquals("result", 6356752.314140356, result, 1.0E-6);
    }
}
