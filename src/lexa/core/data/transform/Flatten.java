/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * Flatten.java
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

package lexa.core.data.transform;

import lexa.core.data.ArrayDataSet;
import lexa.core.data.DataArray;
import lexa.core.data.DataItem;
import lexa.core.data.DataSet;
import lexa.core.data.DataValue;

/**
 *
 * @author william
 */
public class Flatten
        extends TransformStep
{
    private final String field;
    private int nextRead;

    public Flatten(Transform parent, String field)
    {
        super(parent);
        this.field=field;
        this.nextRead=0;
    }

    @Override
    public DataItem item(int index)
    {
        this.processTo(index);
        if (index < 0 || index >= this.results.size())
        {
            return null;
        }
        return this.results.get(index);
    }
    @Override
    int processTo(int item)
    {
        if (this.results == null)
        {
            this.results=new ArrayDataSet();
        }
        while (this.validatedItems < item &&
                this.nextRead < this.previous.size() )
        {
            DataItem next = this.previous.item(this.nextRead);
            DataSet data = next.getDataSet(); //next == null ? null :
            DataArray flatten = data.getArray(this.field); //data == null ? null :
            if (flatten == null )
            {
                // nothing to flatten, either it's a flat field or not included.
                this.results.put(next);
                this.validatedItems++;
            }
            else
            {
                if (flatten.size() > 2)
                {
                    int flattened = 0;
                    for (DataValue value : flatten)
                    {
                        flattened++;
                        String key = next.getKey() + '_' + flattened;
                        this.results.put(key,
                                data.put(this.field, value)
                        );

                    }
                    this.validatedItems+=flattened;
                }
                else
                {
                    this.results.put(next.getKey() ,
                            data.put(this.field, flatten.get(0))
                    );
                    this.validatedItems++;
                }
            }
            this.nextRead++;
        }
        return this.validatedItems;
    }
    @Override
    public int size()
    {
        this.processTo(Integer.MAX_VALUE);
        return this.results.size();
    }
}
