/*
 *==============================================================================
 * Lexa - Property of William Norman-Walker
 *------------------------------------------------------------------------------
 * TestTransform.java (lxTransform)
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: February 2017
 *==============================================================================
 */
package lxtransform;

import java.io.IOException;

import lexa.test.TestClass;
import lexa.test.TestRun;

/**
 * @author william
 * @since 2017-05
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
            new MapTest(),
            new SortTest(),
            new TruncateTest(),
            new MultipleTransformTest()
        };
        System.out.println(
                new TestRun(tests)
                        .execute()
                        .getReport(false, true)
        );
	}
}
