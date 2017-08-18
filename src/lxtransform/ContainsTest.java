/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * ContainsTest.java (lxTransform)
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
 * Test contain transforms
 * @author  william
 * @since   2017-05
 */

// the set up methods are in the super class, but we need annotation here:
@TestAnnotation(
        arguments = "dataSetTypes",
        setUp = "setUpClass",
        tearDown = "tearDownClass")
public class ContainsTest
        extends CommonTest
{
    /**
     * Test the contains transform
     *
     * @param   arg
     *          the type of data set being tested
     * @return  the result of the test
     * @throws  IOException
     *          when an IO exception occurs
     */
    @TestAnnotation()
    public TestResult contains(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).contains("sex", "m");
        return TestResult.result(47, transform.getDataSet().size());
    }

    /**
     * Test the contains transform with no output
     *
     * @param   arg
     *          the type of data set being tested
     * @return  the result of the test
     * @throws  IOException
     *          when an IO exception occurs
     */
    @TestAnnotation()
    public TestResult containsNone(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).contains("sex", "x");
        return TestResult.result(0, transform.getDataSet().size());
    }
}
