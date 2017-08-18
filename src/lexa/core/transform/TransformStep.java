/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * TransformStep.java (lxTransform)
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: February 2017
 *==============================================================================
 */
package lexa.core.transform;

import lexa.core.data.*;

/**
 * A step in a data set transformation
 * @author william
 * @since 2017-02
 */
public abstract class TransformStep
    extends Transform
{

    /** The previous step in the transformation */
    protected final Transform previous;

    @Override
    public DataFactory factory()
    {
        if (this.results == null)
        {
            return this.previous.factory();
        }
        return this.results.factory();
    }

    @Override
    public DataItem item(int index)
    {
        if (index < 0 || index >= this.processTo(index))
        {
            return null;
        }
        return this.results.get(index);
    }

    @Override
    public DataSet getDataSet()
    {
        int size  = this.processTo(this.previous.size());
        if (this.results != null)
        {
            return this.results;
        }
        this.results = this.factory().getDataSet();
        for (int index = 0;
             index < size;
             index++)
        {
            this.results.put(this.item(index));
        }
        return this.results;
    }

    TransformStep(Transform previous)
    {
        this.previous = previous;
        this.previous.children.add(this);
    }
}
