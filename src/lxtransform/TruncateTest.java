/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * TruncateTest.java
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: May 2017
 *==============================================================================
 */
package lxtransform;

import java.io.IOException;
import lexa.core.data.transform.Transform;
import lexa.test.TestAnnotation;
import lexa.test.TestResult;

/**
 *
 * @author william
 */
@TestAnnotation(
        arguments = "dataSetTypes",
        setUp = "setUpClass",
        tearDown = "tearDownClass")
public class TruncateTest
        extends CommonTest
{
    @TestAnnotation()
    public TestResult truncate(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).truncate(15);
        return TestResult.result(15,transform.size());
    }
    @TestAnnotation()
    public TestResult truncateEnd(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).truncate(-7);
        return TestResult.result(7,transform.size());
    }
    @TestAnnotation()
    public TestResult truncateOver(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).truncate(150);
        return TestResult.result(CommonTest.DATA_SIZE,transform.size());
    }

    @TestAnnotation()
    public TestResult sliceThirdQuartile(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data)
                .truncate(75)
                .truncate(-25);
        return TestResult.result(25,transform.size());
    }
}
