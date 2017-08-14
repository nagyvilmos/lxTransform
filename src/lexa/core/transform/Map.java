/*==============================================================================
 * Lexa - Property of William Norman-Walker
 *------------------------------------------------------------------------------
 * Map.java (lxTransform)
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: August 2017
 *==============================================================================
 */

package lexa.core.transform;

import lexa.core.data.*;
import lexa.core.expression.map.*;

/**
 *
 * @author  willaimnw
 * @since   2017-08
 */
public class Map
        extends TransformStep
{
    private final ExpressionMap map;
    private DataSet transformed;

    public Map(Transform parent, ExpressionMap map)
    {
        super(parent);
        this.map = map;
        this.transformed = parent.factory().getDataSet();
    }
    @Override
    public DataItem item(int index)
    {
        this.processTo(index);
        if (index < 0 || index >= this.transformed.size())
        {
            return null;
        }
        return this.transformed.get(index);
    }

    @Override
    public int size()
    {
        this.processTo(this.previous.size());
        return this.transformed.size();
    }

    @Override
    int processTo(int item)
    {
        if (this.validatedItems == 0)
        {
            this.transformed = this.previous.factory().getDataSet();
        }
        while(item > this.transformed.size())
        {
            DataItem di = this.previous.item(this.validatedItems);
            if (di == null)
            {
                break;
            }
            DataSet ds = di.getDataSet();
            this.transformed = new MapDataSet(this.map, ds);
            this.validatedItems++;
        }
        return this.transformed.size();
    }
}
