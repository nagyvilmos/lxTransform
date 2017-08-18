/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * FlattenTest.java (lxTransform)
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: May 2017
 *==============================================================================
 */
package lxtransform;

import java.io.IOException;
import lexa.core.data.DataSet;
import lexa.core.transform.Transform;
import lexa.test.TestAnnotation;
import lexa.test.TestResult;

/**
 * Test flatten transforms
 * @author  william
 * @since   2017-05
 */

@TestAnnotation(
        arguments = "dataSetTypes",
        setUp = "setUpClass",
        tearDown = "tearDownClass")
public class FlattenTest
        extends CommonTest
{
    /** expected size of flattening */
    private final static int FLATTEND_SIZE = 260;

    /**
     * Test the flatten transform
     *
     * @param   arg
     *          the type of data set being tested
     * @return  the result of the test
     */
    @TestAnnotation()
    public TestResult simple(Object arg)
    {
        DataSet from = this.data.factory().getDataSet();
        from.put("x",from.factory().getDataSet()
                .put("y",
                    from.factory().getDataArray()
                            .add(1).add(2).add(3)
                )
        );
        DataSet expected = this.data.factory().getDataSet();
        expected
            .put("x_0", expected.factory().getDataSet()
                .put(expected.factory().getDataItem("y", 1)))
            .put("x_1", expected.factory().getDataSet()
                .put(expected.factory().getDataItem("y", 2)))
            .put("x_2", expected.factory().getDataSet()
                .put(expected.factory().getDataItem("y", 3)));
        Transform transform = new Transform(from).flatten("y");
        DataSet result = transform.getDataSet();
        return TestResult.result(expected, result);
    }

    /**
     * Test the flatten transform for the full test data
     *
     * @param   arg
     *          the type of data set being tested
     * @return  the result of the test
     */
    @TestAnnotation()
    public TestResult flatten(Object arg)
    {
        Transform transform = new Transform(this.data).flatten("tags");
        return TestResult.result(FlattenTest.FLATTEND_SIZE, transform.getDataSet().size());
    }

    /**
     * Test the flatten transform where the field is not an array
     *
     * @param   arg
     *          the type of data set being tested
     * @return  the result of the test
     * @throws  IOException
     *          when an IO exception occurs     */
    @TestAnnotation()
    public TestResult flattenNonArray(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).flatten("sex");
        return TestResult.result(CommonTest.DATA_SIZE, transform.getDataSet().size());
    }

    /**
     * Test the flatten transform where the field does not exist
     *
     * @param   arg
     *          the type of data set being tested
     * @return  the result of the test
     * @throws  IOException
     *          when an IO exception occurs     */
    @TestAnnotation()
    public TestResult flattenNoField(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).flatten("foobar");
        return TestResult.result(CommonTest.DATA_SIZE, transform.getDataSet().size());
    }
}
