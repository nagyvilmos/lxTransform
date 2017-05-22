/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * Contains.java
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

import java.util.ArrayList;
import java.util.List;
import lexa.core.data.DataItem;
import lexa.core.data.DataSet;
import lexa.core.data.DataValue;

/**
 *
 * @author william
 */
public class Contains
        extends TransformStep
{

    private final String key;
    private final DataValue value;
    private final List<Integer> matches;

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
        this.processTo(index);
        if (index < 0 || index >= this.matches.size())
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
