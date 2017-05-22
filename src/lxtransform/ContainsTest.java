/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * ContainsTest.java
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: May 2017
 *==============================================================================
 */
package lxtransform;

import java.io.IOException;
import lexa.core.transform.Transform;
import lexa.test.TestAnnotation;
import lexa.test.TestResult;

/**
 *
 * @author william
 */

// the set up methods are in the super class, but we need annotation here:
@TestAnnotation(
        arguments = "dataSetTypes",
        setUp = "setUpClass",
        tearDown = "tearDownClass")
public class ContainsTest
        extends CommonTest
{
    @TestAnnotation()
    public TestResult contains(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).contains("sex", "m");
        return TestResult.result(47, transform.getDataSet().size());
    }

    @TestAnnotation()
    public TestResult containsNone(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).contains("sex", "x");
        return TestResult.result(0, transform.getDataSet().size());
    }

}
