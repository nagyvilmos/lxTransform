/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * Contains.java (lxTransform)
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: May 2017
 *==============================================================================
 */
package lexa.core.transform;

import java.util.ArrayList;
import java.util.List;
import lexa.core.data.DataItem;
import lexa.core.data.DataSet;
import lexa.core.data.DataValue;

/**
 * Extract items from a data set.
 * @author william
 * @since 2017-05
 */
public class Contains
        extends TransformStep
{

    private final String key;
    private final DataValue value;
    private final List<Integer> matches;

    /**
     * Extract the items containing a matching key/value
     * @param   parent
     *          the parent to the transformation
     * @param   key
     *          the key to be matched
     * @param   value
     *          the value required for the key
     */
    public Contains(Transform parent, String key, Object value)
    {
        super(parent);
        this.key=key;
        this.value= super.factory().getDataValue(value);
        this.matches = new ArrayList();
    }

    @Override
    public DataItem item(int index)
    {
        if (index < 0 || index >= this.processTo(index))
        {
            return null;
        }
        return this.previous.item(this.matches.get(index));
    }

    @Override
    public int size()
    {
        this.processTo(this.previous.size());
        return this.matches.size();
    }

    @Override
    int processTo(int item)
    {
        if (this.validatedItems == 0)
        {
            this.matches.clear();
        }
        while(item > this.matches.size())
        {
            DataItem di = this.previous.item(this.validatedItems);
            if (di == null)
            {
                break;
            }
            DataSet ds = di.getDataSet();
            if (ds != null && ds.item(key).equals(this.value) )
            {
                this.matches.add(this.validatedItems);
            }
            this.validatedItems++;
        }
        return this.matches.size();
    }

}
