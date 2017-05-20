/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * Transform.java
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: February 2017
 *------------------------------------------------------------------------------
 * Change Log
 * Date:        By: Description:
 * ----------   --- ------------------------------------------------------------
 * -            -   -
 *==============================================================================
 */
package lexa.core.data.transform;

import java.util.HashSet;
import java.util.Set;
import lexa.core.data.ArrayDataSet;
import lexa.core.data.DataItem;
import lexa.core.data.DataSet;

/**
 *
 * @author william
 */
public class Transform
{

    protected final Set<Transform> children;
    protected int validatedItems;
    protected DataSet results;

    protected Transform()
    {
        this(null);
    }
    public Transform(DataSet data)
    {
        this.results=data;
        this.children =new HashSet();
        this.validatedItems=0;
    }

    public DataSet getDataSet()
    {
        return this.results;
    }

    public DataItem item(int index)
    {
        return this.results.get(index);
    }

    public int size()
    {
        return this.results == null ? 0 :
                this.results.size();
    }

    int processTo(int item)
    {
        // this is allways already fully processed
        return this.size();
    }

    protected void sourceUpdated()
    {
        this.validatedItems=0;
        for (Transform child : this.children)
        {
            child.sourceUpdated();
        }
    }
    public Transform contains(String key, Object value)
    {
        return new Contains(this, key, value);
    }
    public Transform flatten(String key)
    {
        return new Flatten(this, key);
    }
    public Transform group()
    {
        return new Group(this);
    }
    public Transform group(DataSet grouping)
    {
        return new Group(this,
                grouping != null ? grouping :
                        new ArrayDataSet());
    }
    public Transform sort(String field, boolean ascending)
    {
        return new Sort(this, field, ascending);
    }
    public Transform sort(String[] field, boolean[] ascending)
    {
        return new Sort(this, field, ascending);
    }
    public Transform truncate(int requiredSize)
    {
        return new Truncate(this, requiredSize);
    }
}
