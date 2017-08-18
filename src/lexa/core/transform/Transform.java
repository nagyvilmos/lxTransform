/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * Transform.java (lxTransform)
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: February 2017
 *==============================================================================
 */
package lexa.core.transform;

import java.util.HashSet;
import java.util.Set;
import lexa.core.data.ArrayFactory;
import lexa.core.data.DataFactory;
import lexa.core.data.DataFactoryItem;
import lexa.core.data.DataItem;
import lexa.core.data.DataSet;
import lexa.core.expression.map.*;

/**
 * Base Transformation
 * <p>
 * This is the root item for all transformations
 *
 * @author william
 * @since 2017-02
 */
public class Transform
        implements DataFactoryItem
{

    /** the child transforms */
    protected final Set<Transform> children;
    /** the items which have been validated */
    protected int validatedItems;
    /** the full result set, only ever built as needed */
    protected DataSet results;

    /**
     * Create a transformation without any input
     */
    protected Transform()
    {
        this(null);
    }

    /**
     * Create a transformation for a data set
     * @param   data
     *          the input to the transformation
     */
    public Transform(DataSet data)
    {
        this.results=data;
        this.children =new HashSet();
        this.validatedItems=0;
    }

    /**
     * Get the data factory supporting this transforms data
     * @return  a data factory
     */
    @Override
    public DataFactory factory()
    {
        if (this.results == null)
        {
            return ArrayFactory.factory;
        }
        return this.results.factory();
    }

    /**
     * Get the data set with the results of the transformation
     * <p>
     * A call to this will always result in the transformation being completely
     * processed.
     * @return  the results
     */
    public DataSet getDataSet()
    {
        return this.results;
    }

    /**
     * Get an item from the result set,
     * <p>
     * The transform is processed only to the point where this item is available.
     * @param   index
     *          the index for the required item
     * @return  the required item
     */
    public DataItem item(int index)
    {
        return this.results.get(index);
    }

    /**
     * Get the size of the result set.
     * <p>
     * A call to this will always result in the transformation being completely
     * processed.
     * @return  the size of the results
     */
    public int size()
    {
        return this.results == null ? 0 :
                this.results.size();
    }

    /**
     * Process the transformation
     * <p>
     * Process the transformation up to the numbered item.
     *
     * @param   item
     *          the number of items to be processed
     * @return  the number of items processed; the least of requested items and
     *          total number of items.
     */
    int processTo(int item)
    {
        // this is allways already fully processed
        return this.size();
    }

    /**
     * The source data has been updated.
     * <p>
     * When the source data is updated this is called and cascades down through
     * the transformations.  It indicates that the transform must be reset.
     */
    protected void sourceUpdated()
    {
        this.validatedItems=0;
        for (Transform child : this.children)
        {
            child.sourceUpdated();
        }
    }

    /**
     * Create a transformation of contained items; equivalent to
     * {@link Contains#Contains(lexa.core.transform.Transform, java.lang.String, java.lang.Object)
     * new Contains(this, key, value)}
     *
     * @param   key
     *          the key to an item in the source to be matched
     * @param   value
     *          the value to match the key on.
     * @return  the transformation
     */
    public Transform contains(String key, Object value)
    {
        return new Contains(this, key, value);
    }

    /**
     * Create a transformation of flattened items; equivalent to
     * {@link Flatten#Flatten(lexa.core.transform.Transform, java.lang.String)
     * new Flatten(this, key)}
     *
     * @param   key
     *          the key to an item in the source to be matched
     * @return  the transformation
     */
    public Transform flatten(String key)
    {
        return new Flatten(this, key);
    }

    /**
     * Create a transformation of grouped items; equivalent to
     * {@link Group#Group(lexa.core.transform.Transform) new Group(this)}
     *
     * @return  the transformation
     */
    public Transform group()
    {
        return new Group(this);
    }

    /**
     * Create a transformation of grouped items; equivalent to
     * {@link Group#Group(lexa.core.transform.Transform, lexa.core.data.DataSet)
     * new Group(this, grouping)}
     *
     * @param   grouping
     *          the grouping options.
     * @return  the transformation
     */
    public Transform group(DataSet grouping)
    {
        return new Group(this,
                grouping != null ? grouping :
                        this.factory().getDataSet());
    }

    /**
     * Create a transformation of mapped items; equivalent to
     * {@link Map#Map(lexa.core.transform.Transform, lexa.core.expression.map.ExpressionMap)
     * new Group(this, map)}
     *
     * @param   map
     *          the map.
     * @return  the transformation
     */
    public Transform map(ExpressionMap map)
    {
        return new Map(this, map);
    }

    /**
     * Create a transformation of sorted items; equivalent to
     * {@link Sort#Sort(lexa.core.transform.Transform, java.lang.String, boolean)
     * new Group(this, field, ascending)}
     *
     * @param   field
     *          the field to sort on
     * @param   ascending
     *          is the order ascending
     * @return  the transformation
     */
    public Transform sort(String field, boolean ascending)
    {
        return new Sort(this, field, ascending);
    }

    /**
     * Create a transformation of sorted items; equivalent to
     * {@link Sort#Sort(lexa.core.transform.Transform, java.lang.String[], boolean[])
     * new Group(this, field, ascending)}
     *
     * @param   field
     *          array of fields to sort on
     * @param   ascending
     *          are the orders ascending
     * @return  the transformation
     */
    public Transform sort(String[] field, boolean[] ascending)
    {
        return new Sort(this, field, ascending);
    }

    /**
     * Create a transformation of truncated items; equivalent to
     * {@link Truncate#Truncate(lexa.core.transform.Transform, int)
     * new Group(this, requiredSize)}
     *
     * @param   requiredSize
     *          the size to truncate to; negative is tail.
     * @return  the transformation
     */
    public Transform truncate(int requiredSize)
    {
        return new Truncate(this, requiredSize);
    }
}
