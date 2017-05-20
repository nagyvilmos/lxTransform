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
public class FlattenTest
        extends CommonTest
{
    @TestAnnotation()
    public TestResult flatten(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).flatten("tags");
        return TestResult.result(281, transform.getDataSet().size());
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
