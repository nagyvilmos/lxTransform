/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * CommonTest.java (lxTransform)
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: May 2017
 *==============================================================================
 */
package lxtransform;

import java.util.Random;
import lexa.core.data.ArrayDataArray;
import lexa.core.data.ArrayDataSet;
import lexa.core.data.DataArray;
import lexa.core.data.DataSet;
import lexa.core.data.HashDataSet;
import lexa.test.TestResult;

/**
 * Common steps for the transform test handlers
 * @author william
 * @since 2017-05
 */
public class CommonTest
        extends lexa.test.TestClass
{
    protected final static int DATA_SIZE = 100;
    protected DataSet data;

    private static DataSet populateItem(int x, Random rnd)
    {
        DataSet data = new ArrayDataSet()
                    .put("id", x)
                    .put("name", CommonTest.string(rnd, 3))
                    .put("forename", CommonTest.string(rnd, 2))
                    .put("age", rnd.nextInt(30) + 21)
                    .put("sex", rnd.nextBoolean() ? "m" : "f")
                    .put("rating", rnd.nextDouble())
                    .put("tags", CommonTest.stringArray(rnd, 1,rnd.nextInt(5) + 1));
        if (rnd.nextInt(3) == 1)
        {
            data.put("optional", rnd.nextInt(CommonTest.DATA_SIZE));
        }
        return data;
    }

   private static String string(Random rnd, int length)
    {
        char[] letters = {'a','b','c','d','e','f','g','k','l','m'};
        int mod = letters.length;
        char[] output = new char[length];
        int pos = rnd.nextInt(mod);
        for (int l = 0; l < length; l++)
        {
            output[l] = letters[pos];
            pos = rnd.nextInt(mod);
        }
        return new String (output);
    }
    private static DataArray stringArray(Random rnd, int length, int items)
    {
        DataArray array = new ArrayDataArray();
        for (int i=0; i<items; i++)
        {
            array.add(CommonTest.string(rnd, length));
        }
        return array;
    }

    static TestResult populate(DataSet data)
    {
        Random rnd = new Random(715913);
        for (int x = 0; x < CommonTest.DATA_SIZE; x++)
        {
            data.put(CommonTest.string(rnd, 6),
                    CommonTest.populateItem(x,rnd));
        }
        return TestResult.result(CommonTest.DATA_SIZE, data.size());
    }
    /**
     *
     * @return
     */
    public Object[] dataSetTypes()
    {
        return new Object[]{"array", "hash"};
    }

    /**
     *
     * @param arg
     * @return
     */
    public TestResult setUpClass(Object arg)
    {
        String type = (String)arg;
        switch (type)
        {
        case "array" :
        {
            this.data = new ArrayDataSet();
            break;
        }
        case "hash" :
        {
            this.data = new HashDataSet();
            break;
        }
        default :
        {
            this.data = null;
        }
        }
        return CommonTest.populate(this.data);
    }

    public TestResult tearDownClass(Object arg)
    {
        this.data = null;
        return TestResult.isNull(this.data);
    }
}
