/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * MultipleTransformTest.java (lxTransform)
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
 * Test multiple transformations
 *
 * @author  william
 * @since   2017-05
 */
@TestAnnotation(
        arguments = "dataSetTypes",
        setUp = "setUpClass",
        tearDown = "tearDownClass")
public class MultipleTransformTest
        extends CommonTest {

    /**
     * Test multiple transformations
     *
     * @param   arg
     *          the type of data set being tested
     * @return  the result of the test
     * @throws  IOException
     *          when an IO exception occurs
     */
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
