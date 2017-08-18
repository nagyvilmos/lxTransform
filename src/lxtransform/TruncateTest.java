/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * TruncateTest.java (lxTransform)
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
 * Test truncate transformations
 *
 * @author  william
 * @since   2017-05
 */
@TestAnnotation(
        arguments = "dataSetTypes",
        setUp = "setUpClass",
        tearDown = "tearDownClass")
public class TruncateTest
        extends CommonTest
{

    /**
     * Test truncate transformation
     *
     * @param   arg
     *          the type of data set being tested
     * @return  the result of the test
     * @throws  IOException
     *          when an IO exception occurs
     */
    @TestAnnotation()
    public TestResult truncate(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).truncate(15);
        return TestResult.result(15,transform.size());
    }

    /**
     * Test truncate transformation of the tail
     *
     * @param   arg
     *          the type of data set being tested
     * @return  the result of the test
     * @throws  IOException
     *          when an IO exception occurs
     */
    @TestAnnotation()
    public TestResult truncateEnd(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).truncate(-7);
        return TestResult.result(7,transform.size());
    }

    /**
     * Test truncate transformation for more than available
     *
     * @param   arg
     *          the type of data set being tested
     * @return  the result of the test
     * @throws  IOException
     *          when an IO exception occurs
     */
    @TestAnnotation()
    public TestResult truncateOver(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).truncate(150);
        return TestResult.result(CommonTest.DATA_SIZE,transform.size());
    }

    /**
     * Test truncate transformation taking top then tail.
     *
     * @param   arg
     *          the type of data set being tested
     * @return  the result of the test
     * @throws  IOException
     *          when an IO exception occurs
     */
    @TestAnnotation()
    public TestResult sliceThirdQuartile(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data)
                .truncate(75)
                .truncate(-25);
        return TestResult.result(25,transform.size());
    }
}
