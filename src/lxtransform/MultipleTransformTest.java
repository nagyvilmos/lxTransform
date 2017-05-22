/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * MultipleTransformTest.java
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

@TestAnnotation(
        arguments = "dataSetTypes",
        setUp = "setUpClass",
        tearDown = "tearDownClass")
public class MultipleTransformTest
        extends CommonTest {

    @TestAnnotation()
    public TestResult multiple(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data)
                .truncate(-50)
                .contains("age", 30)
                .sort("name", false)
                .truncate(5);
        // different data order means different results, just we must have a result
        return TestResult.notNull(transform.getDataSet());
    }

}
