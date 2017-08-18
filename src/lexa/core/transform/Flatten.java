/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * Flatten.java (lxTransform)
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: May 2017
 *==============================================================================
 */
package lexa.core.transform;

import lexa.core.data.DataArray;
import lexa.core.data.DataItem;
import lexa.core.data.DataSet;
import lexa.core.data.DataValue;

/**
 * Create a transformation to flatten contents
 * <p>
 * Based on a keyed field, this creates a duplicate item for each entry in the
 * named array value.
 *
 * @since 2017-05
 * @author william
 */
public class Flatten
        extends TransformStep
{
    private final String field;
    private int nextRead;

    /**
     * Create a transformation to flatten contents
     *
     * @param   parent
     *          the parent to the transformation
     * @param   field
     *          the field to be flattened
     */
    public Flatten(Transform parent, String field)
    {
        super(parent);
        this.field=field;
        this.nextRead=0;
    }

    @Override
    public DataSet getDataSet()
    {
        this.processTo(Integer.MAX_VALUE);
        return super.getDataSet();
    }

    @Override
    int processTo(int item)
    {
        if (this.results == null)
        {
            this.results=this.factory().getDataSet();
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
                        String key = next.getKey() + '_' + flattened;
                        this.results.put(key,
                                data.factory().clone(data)
                                    .put(this.field, value)
                        );
                        flattened++;
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
