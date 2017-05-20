/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * GroupTest.java
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: Month YEAR
 *------------------------------------------------------------------------------
 * Change Log
 * Date:        By: Description:
 * ----------   --- ------------------------------------------------------------
 * -            -   -
 *==============================================================================
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lxtransform;

import java.io.IOException;
import lexa.core.data.ArrayDataArray;
import lexa.core.data.ArrayDataSet;
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

    public TestResult keyGender(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).group(
            new ArrayDataSet()
                .put("key",
                    new ArrayDataArray()
                        .add("sex")));
        return TestResult.all(
                TestResult.result(2, transform.getDataSet().size()),
                TestResult.result(50,
                    transform
                            .item(0).getDataSet()
                            .getInteger("count")),
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
                TestResult.result(35,
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
                TestResult.result(4000,
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
                TestResult.result(12.5,
                    transform
                            .item(0).getDataSet()
                            .getDouble("total rating"))
        );
    }
}

