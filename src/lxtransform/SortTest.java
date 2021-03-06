/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * SortTest.java (lxTransform)
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: May 2017
 *==============================================================================
 */
package lxtransform;

import java.io.IOException;
import lexa.core.data.*;
import lexa.core.transform.Transform;
import lexa.test.TestAnnotation;
import lexa.test.TestResult;

/**
 * Test sort transformations
 *
 * @author  william
 * @since   2017-05
 */
@TestAnnotation(
        arguments = "dataSetTypes",
        setUp = "setUpClass",
        tearDown = "tearDownClass")
public class SortTest
        extends CommonTest {

    /**
     * Test sort transformation
     *
     * @param   arg
     *          the type of data set being tested
     * @return  the result of the test
     * @throws  IOException
     *          when an IO exception occurs
     */
    @TestAnnotation()
    public TestResult sort(Object arg) throws IOException
    {
        Transform transform = new Transform(this.data).sort("rating", true);
        DataSet result = transform.getDataSet();
        Double previous = 0.0;
        boolean ordered = true;
        for (DataItem item : result)
        {
            Double next = item.getDataSet().getDouble("rating");
            if (next < previous)
            {
                ordered = false;
                break;
            }
            previous = next;
        }
        return TestResult.all(
                TestResult.result(CommonTest.DATA_SIZE, result.size()),
                TestResult.result(true, ordered, "Data not sorted")
        );
    }

    /**
     * Test sort transformation with an optional field
     *
     * @param   arg
     *          the type of data set being tested
     * @return  the result of the test
     * @throws  IOException
     *          when an IO exception occurs
     */
    @TestAnnotation()
    public TestResult sortOptional(Object arg) throws IOException
    {
        String[] fields = {"optional", "rating"};
        boolean[] ascending = {true, true};
         Transform transform =
                new Transform(this.data)
                .sort(fields, ascending);
        return TestResult.result(CommonTest.DATA_SIZE,
                transform.getDataSet().size());
    }

    /**
     * Test sort transformation with ascending and descending keys
     *
     * @param   arg
     *          the type of data set being tested
     * @return  the result of the test
     * @throws  IOException
     *          when an IO exception occurs
     */
    @TestAnnotation()
    public TestResult sortNameAgeDesc(Object arg) throws IOException
    {
        String[] fields = {"name", "forename", "age"};
        boolean[] ascending = {true, true, false};
         Transform transform =
                new Transform(this.data)
                .sort(fields, ascending);
        return TestResult.result(CommonTest.DATA_SIZE,
                transform.getDataSet().size());
    }

    /**
     * Test sort transformation multiple times with different inputs
     *
     * @param   arg
     *          the type of data set being tested
     * @return  the result of the test
     * @throws  IOException
     *          when an IO exception occurs
     */
    @TestAnnotation()
    public TestResult multiSort(Object arg) throws IOException
    {
        TestResult result = TestResult.result(true);
        for (int l = 10; l < 101; l+=10)
        {
            Transform transform =
                    new Transform(this.data)
                            .truncate(l)
                            .sort("rating", (l % 20 == 0))
                            .truncate(-1); // take the last one
            result = TestResult.all(result,
                    TestResult.result(1,
                        transform.getDataSet().size())
            );
        }
        return result;
    }
}
