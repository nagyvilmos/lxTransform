/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * Sort.java
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
package lexa.core.transform;

import lexa.core.data.ArrayFactory;
import lexa.core.data.DataFactory;
import lexa.core.data.DataItem;
import lexa.core.data.DataSet;
import lexa.core.data.DataValue;

/**
 *
 * @author william
 */
class Sort
        extends TransformStep {

    private final String[] fields;
    private final boolean[] ascending;
    private int[] sorted;

    public Sort(Transform parent, String field, boolean ascending)
    {
        this(parent, new String[]{field}, new boolean[]{ascending});
    }
    public Sort(Transform parent, String[] fields, boolean[] ascending)
    {
        super(parent);
        if (fields.length != ascending.length)
        {
            throw new IllegalArgumentException("Mismatched number of fields and order");
        }
        this.fields=fields;
        this.ascending= ascending;
        this.results = null;
    }

    @Override
    public DataItem item(int index)
    {
        if (index < 0 || index >= this.size())
        {
            return null;
        }
        this.processTo(index);
        return this.previous.item(this.sorted[index]);
    }


    @Override
    public int size()
    {
        return this.previous.size();
    }

    @Override
    public DataFactory factory()
    {
        return ArrayFactory.factory;
    }
    @Override
    public DataSet getDataSet()
    {
        if (this.results == null)
        {
            this.results = this.factory().getDataSet();
            for (int i = 0; i < this.size(); i++)
            {
                this.results.put(this.item(i));
            }
        }
        return this.results;
    }

    @Override
    int processTo(int item)
    {
        if (this.validatedItems > 0)
        {
            return this.validatedItems; // already done everything
        }
        int size = this.size();
        this.sorted = new int[size];
        if (size>0)
        {
            this.splitSort(0, size);
        }
        this.validatedItems=this.size();
        return this.validatedItems;
    }
    private void splitSort(int min, int max)
    {
        if (min +1 == max)
        {
            this.sorted[min]=min;
            return;
        }
        int mid = (min + max) / 2;
        assert(mid!=min && mid!=max);
        this.splitSort(min, mid);
        this.splitSort(mid, max);


        int left = min;
        int right = mid;
        while (left < mid && right < max)
        {
            if (compare(left, right) <= 0)
            {
                left++;
            }
            else
            {
                // swap
                int temp = this.sorted[left];
                this.sorted[left] = this.sorted[right];
                this.sorted[right]=temp;
                right++;
            }
        }
    }

    private int compare(int from, int to)
    {
        for (int f=0; f < this.fields.length; f++)
        {
            DataValue fromValue = this.previous.item(this.sorted[from]).getDataSet().item(this.fields[f]);
            DataValue toValue   = this.previous.item(this.sorted[to]  ).getDataSet().item(this.fields[f]);

            int compare = fromValue == null ?
                    toValue == null ? 0 : 1 :
                    toValue == null ? -1 :
                    fromValue.compareTo(toValue);

            if (compare !=0)
            {
                return this.ascending[f] ?
                    compare :
                    -compare;
            }
        }
        return 0;
    }

}
