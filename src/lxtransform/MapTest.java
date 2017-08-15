/*==============================================================================
 * Lexa - Property of William Norman-Walker
 *------------------------------------------------------------------------------
 * MapTest.java (lxTransform)
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: August 2017
 *==============================================================================
 */

package lxtransform;

import java.io.*;
import lexa.core.data.*;
import lexa.core.data.io.*;
import lexa.core.expression.*;
import lexa.core.expression.map.*;
import lexa.core.transform.*;
import lexa.test.*;

/**
 *
 * @author  willaimnw
 * @since   2017-08
 */
@TestAnnotation(
        arguments = "dataSetTypes",
        setUp = "setUpClass",
        tearDown = "tearDownClass")
public class MapTest
        extends CommonTest
{

    @TestAnnotation()
    public TestResult simple(Object arg)
        throws ExpressionException,
            IOException
    {
        ExpressionMap map = new ExpressionMap(
                DataReader.parseString(
                        "fullname - [format \"%s %s\" forename name]"
                )
        );
        Transform transform = new Transform(this.data).map(map);
        DataSet result = transform.getDataSet();

        int invalidNames = 0;
        for (DataItem di : result)
        {
            DataSet ds = this.data.getDataSet(di.getKey());
            String fullname = ds.getString("forename") +
                    " " + ds.getString("name");
            DataSet res = di.getDataSet();
            if (!fullname.equals(res.getString("fullname")))
            {
                invalidNames++;
            }
        }
        return TestResult.all(
                TestResult.result(this.data.size(), result.size()),
                TestResult.result(0, invalidNames, Integer.toString(invalidNames) + " invalid name[s]")
        );
    }
}
