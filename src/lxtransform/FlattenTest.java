/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * FlattenTest.java
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: May 2017
 *==============================================================================
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lxtransform;

import java.io.IOException;
import lexa.core.data.DataSet;
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
public class FlattenTest
        extends CommonTest
{
    private final static int FLATTEND_SIZE = 260;
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
    @TestAnnotation()
    public TestResult flatten(Object arg)
    {
        Transform transform = new Transform(this.data).flatten("tags");
        return TestResult.result(FlattenTest.FLATTEND_SIZE, transform.getDataSet().size());
    }

    @TestAnnotation()
    public TestResult flattenNonArray(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).flatten("sex");
        return TestResult.result(CommonTest.DATA_SIZE, transform.getDataSet().size());
    }

    @TestAnnotation()
    public TestResult flattenNoField(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).flatten("foobar");
        return TestResult.result(CommonTest.DATA_SIZE, transform.getDataSet().size());
    }
}
