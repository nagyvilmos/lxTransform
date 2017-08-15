/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * GroupTest.java (lxTransform)
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: May 2017
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
        DataSet ds = transform.getDataSet();
        return TestResult.all(
                TestResult.notNull(ds),
                TestResult.result(1, ds.size()),
                TestResult.result(CommonTest.DATA_SIZE,
                    ds.getDataSet("all")
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
                TestResult.notNull(ds),
                TestResult.result(2, ds.size()),
                TestResult.result(47,
                    ds.getDataSet("m").getInteger("count")),
                TestResult.result(53,
                    ds.getDataSet("f").getInteger("count"))
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
        DataSet ds = transform.getDataSet();
        return TestResult.all(
                TestResult.notNull(ds),
                TestResult.result(1, ds.size()),
                TestResult.result(28,
                    ds.getDataSet("all")
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
        DataSet ds = transform.getDataSet();
        return TestResult.all(
                TestResult.notNull(ds),
                TestResult.result(1, ds.size()),
                TestResult.result(3402,
                    ds.getDataSet("all")
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
        DataSet ds = transform.getDataSet();
        return TestResult.all(
                TestResult.notNull(ds),
                TestResult.result(1, ds.size()),
                TestResult.result(48.3981456,48.3981457,
                    ds.getDataSet("all")
                            .getDouble("total rating"))
        );
    }
}

