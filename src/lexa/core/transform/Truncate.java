/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * Truncate.java
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

package lexa.core.transform;

import lexa.core.data.DataItem;

/**
 *
 * @author william
 */
public class Truncate
        extends TransformStep {

    private final boolean readTop;
    private final int requiredSize;
    private int start;
    private int end;
    public Truncate(Transform previous, int requiredSize)
    {
        super(previous);
        this.readTop = requiredSize > 0;
        this.requiredSize = this.readTop ? requiredSize : -requiredSize;
    }

    @Override
    public DataItem item(int index)
    {
        this.processTo(index);
        if (index < 0 || index >= this.validatedItems)
        {
            return null;
        }
        return this.previous.item(start+index);
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
