/**
 * Generated by Agitar build: JUnitFactory Version 2.1.2.000677 (Build date: Dec 21, 2007) [2.1.2.000677]
 * JDK Version: 1.5.0_11
 *
 * Generated on Jan 5, 2008 7:25:24 AM
 * Time to generate: 00:10.858 seconds
 *
 *
 *
 */

package org.gavaghan.geodesy.internal;

import org.gavaghan.geodesy.Angle;

import junit.framework.TestCase;


public class AngleAgitarTest extends TestCase {
    
    public Class<Angle> getTargetClass()  {
        return Angle.class;
    }
    
//    public void testConstructor() throws Throwable {
//        callPrivateMethod("org.gavaghan.geodesy.Angle", "<init>", new Class[] {}, null, new Object[] {});
//        assertTrue("Test call resulted in expected outcome", true);
//    }
    
    public void testToDegrees() throws Throwable {
        double result = Angle.toDegrees(100.0);
        assertEquals("result", 5729.5779513082325, result, 1.0E-6);
    }
    
    public void testToDegrees1() throws Throwable {
        double result = Angle.toDegrees(0.0);
        assertEquals("result", 0.0, result, 1.0E-6);
    }
    
    public void testToRadians() throws Throwable {
        double result = Angle.toRadians(100.0);
        assertEquals("result", 1.7453292519943295, result, 1.0E-6);
    }
    
    public void testToRadians1() throws Throwable {
        double result = Angle.toRadians(0.0);
        assertEquals("result", 0.0, result, 1.0E-6);
    }
}
