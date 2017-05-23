/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * GroupTest.java
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: may 2017
 *==============================================================================
 */
package lxtransform;

import java.io.IOException;
import lexa.core.data.ArrayDataArray;
import lexa.core.data.ArrayDataSet;
import lexa.core.data.DataSet;
import lexa.core.transform.Transform;
import lexa.test.TestAnnotation;
import lexa.test.TestResult;

/**
 * Test grouping functions
 * @author william
 * @since 2017-05
 */
@TestAnnotation(
        arguments = "dataSetTypes",
        setUp = "setUpClass",
        tearDown = "tearDownClass")
public class GroupTest
        extends CommonTest
{
    @TestAnnotation()
    public TestResult countAll(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).group();
        return TestResult.all(
                TestResult.result(1, transform.getDataSet().size()),
                TestResult.result(CommonTest.DATA_SIZE,
                    transform
                            .item(0).getDataSet()
                            .getInteger("count"))
        );
    }

    @TestAnnotation()
    public TestResult countGender(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).group(
            new ArrayDataSet()
                .put("key",
                    new ArrayDataArray()
                        .add("sex")));
        DataSet ds = transform.getDataSet();
        return TestResult.all(
                TestResult.result(2, ds.size()),
                TestResult.result(50,
                    ds.get(0).getDataSet().getInteger("count")),
                TestResult.result(50,
                    transform
                            .item(1).getDataSet()
                            .getInteger("count"))
        );
    }

    @TestAnnotation()
    public TestResult countOptional(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).group(
                new ArrayDataSet()
                    .put("grouping", new ArrayDataSet()
                        .put("count",
                            new ArrayDataSet()
                                .put("count", "optional"))));
        return TestResult.all(
                TestResult.result(1, transform.getDataSet().size()),
                TestResult.result(28,
                    transform
                            .item(0).getDataSet()
                            .getInteger("count"))
        );    }
    @TestAnnotation()
    public TestResult sumInteger(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).group(
                new ArrayDataSet()
                    .put("grouping", new ArrayDataSet()
                        .put("sum",
                            new ArrayDataSet()
                                .put("total ages", "age"))));
        return TestResult.all(
                TestResult.result(1, transform.getDataSet().size()),
                TestResult.result(3402,
                    transform
                            .item(0).getDataSet()
                            .getInteger("total ages"))
        );
    }
    @TestAnnotation()
    public TestResult sumDouble(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).group(
                new ArrayDataSet()
                    .put("grouping", new ArrayDataSet()
                        .put("sum",
                            new ArrayDataSet()
                                .put("total rating", "rating"))));
        return TestResult.all(
                TestResult.result(1, transform.getDataSet().size()),
                TestResult.result(48.398145,48.398146,
                    transform
                            .item(0).getDataSet()
                            .getDouble("total rating"))
        );
    }
}

