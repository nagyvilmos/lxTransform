/*
 * ================================================================================
 * Lexa - Property of William Norman-Walker
 * --------------------------------------------------------------------------------
 * TestTransform.java
 *--------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: April 2009
 *--------------------------------------------------------------------------------
 * Change Log
 * Date:        By: Ref:        Description:
 * ---------    --- ----------  --------------------------------------------------
 * 2016-01-28   WNW 16-01       Update javadoc.
 * 2016-08-20   WNW 16-08       add printFormatted method to DataSet
 * 2016-09-12   WNW 16-09       Update for new config package and HashDataSet
 * 2016-12-09   WNW 16-09       Use the test lib.
 *================================================================================
 */
package lxtransform;

import java.io.IOException;

import lexa.test.TestClass;
import lexa.test.TestRun;

/**
 * @author william
 */
public class TestTransform
{

	/**
	 * @param args the command line arguments
	 * @throws java.io.IOException when an IO exception occurs
	 */
	public static void main(String[] args)
			throws IOException
	{
        TestClass[] tests = new TestClass[]{
            new ContainsTest(),
            new FlattenTest(),
            new GroupTest(),
            new SortTest(),
            new TruncateTest(),
            new MultipleTransformTest()
        };
        System.out.println(
                new TestRun(tests)
                        .execute()
                        .getReport()
        );
	}
}
