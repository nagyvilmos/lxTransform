/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * Truncate.java (lxTransform)
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: May 2017
 *==============================================================================
 */
package lexa.core.transform;

import lexa.core.data.*;

/**
 * A transform that truncates the input.
 *
 * @author  william
 * @since   2017-05
 */
public class Truncate
        extends TransformStep {

    /** indicates if the truncation is the top or bottom of the items */
    private final boolean readTop;
    /** the size to truncate to */
    private final int requiredSize;
    /** the starting point */
    private int start;
    private int end;

    /**
     * Create a transform to truncate the data.
     * @param   parent
     *          the parent to the transformation
     * @param   requiredSize
     *          the size to truncate to; negative is tail.
     */
    public Truncate(Transform parent, int requiredSize)
    {
        super(parent);
        this.readTop = requiredSize > 0;
        this.requiredSize = this.readTop ? requiredSize : -requiredSize;
    }

    @Override
    public DataItem item(int index)
    {
        if (index < 0 || index >= this.processTo(index))
        {
            return null;
        }
        return this.previous.item(start+index);
    }

    @Override
    public DataSet getDataSet()
    {
        if (this.previous.size() == this.size())
        {
            return this.previous.getDataSet();
        }
        return super.getDataSet();
    }

    @Override
    public int size()
    {
        this.processTo(this.requiredSize);
        return this.validatedItems;
    }

    @Override
    int processTo(int item)
    {
        if (item < this.validatedItems)
        {
            return this.validatedItems;
        }
        if (this.validatedItems == 0)
        {
            this.start = this.readTop ? 0 :
                    this.previous.size() > this.requiredSize ?
                        this.previous.size() - this.requiredSize :
                        0;
        }
        if (item > this.validatedItems)
        {
            this.validatedItems =
                    this.previous.processTo(this.start + item) - this.start;
            if (this.validatedItems > this.requiredSize)
            {
                this.validatedItems=this.requiredSize;
            }
        }
        return this.validatedItems;
    }
}
