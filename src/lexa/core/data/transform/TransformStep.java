/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * TransformStep.java
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: February 2017
 *------------------------------------------------------------------------------
 * Change Log
 * Date:        By: Description:
 * ----------   --- ------------------------------------------------------------
 * -            -   -
 *==============================================================================
 */
package lexa.core.data.transform;

import lexa.core.data.DataSet;
import lexa.core.data.ArrayDataSet;

/**
 * A step in a data set transformation
 * @author william
 * @since 2017-02
 */
public abstract class TransformStep
    extends Transform
{

    // any search step can see it's previous values
    protected final Transform previous;

    @Override
    public DataSet getDataSet()
    {
        if (this.size() == this.previous.size())
        {
            // take the previous as it is unchanged
            return this.previous.getDataSet();
        }
        if (this.results == null)
        {
            this.results = new ArrayDataSet();
            for (int i = 0; i < this.size(); i++)
            {
                this.results.put(this.item(i));
            }
        }
        return this.results;
    }

    @Override
    int processTo(int item)
    {
        this.results=null;
        return super.processTo(item);
    }


    TransformStep(Transform previous)
    {
        this.previous = previous;
        this.previous.children.add(this);
    }
}
