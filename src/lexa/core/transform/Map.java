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
 * A transform that applies an {@link ExpressionMap} to the input.
 *
 * @author  willaimnw
 * @since   2017-08
 */
public class Map
        extends TransformStep
{
    /** the expression map to apply to the data */
    private final ExpressionMap map;

    /**
     * Create a group transform
     *
     * @param   parent
     *          the parent to the transformation
     * @param   map
     *          the {@link ExpressionMap} to apply.
     */
    public Map(Transform parent, ExpressionMap map)
    {
        super(parent);
        this.map = map;
        this.results = parent.factory().getDataSet();
    }

    @Override
    public int size()
    {
        this.processTo(this.previous.size());
        return this.results.size();
    }

    @Override
    int processTo(int item)
    {
        if (this.validatedItems == 0)
        {
            this.results = this.previous.factory().getDataSet();
        }
        while(item > this.results.size())
        {
            DataItem di = this.previous.item(this.validatedItems);
            if (di == null)
            {
                break;
            }
            DataSet ds = di.getDataSet();
            this.results.put(di.getKey(),
                    new MapDataSet(this.map, ds).evaluate());
            this.validatedItems++;
        }
        return this.results.size();
    }
}
