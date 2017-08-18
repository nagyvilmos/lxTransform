/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * Sort.java (lxTransform)
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: May 2017
 *==============================================================================
 */
package lexa.core.transform;

import lexa.core.data.*;

/**
 * A transform that sorts the input.
 *
 * @author  william
 * @since   2017-05
 */
class Sort
        extends TransformStep {

    /** the fields to sort by */
    private final String[] fields;
    /** indicates if the field is ascending or descending order */
    private final boolean[] ascending;
    /** the indexes of the sorted items */
    private int[] sorted;

    /**
     * Create a sort transform
     *
     * @param   parent
     *          the parent to the transformation
     * @param   field
     *          the field to sort on
     * @param   ascending
     *          is the order ascending
     */
    public Sort(Transform parent, String field, boolean ascending)
    {
        this(parent, new String[]{field}, new boolean[]{ascending});
    }

    /**
     * Create a sort transform
     *
     * @param   parent
     *          the parent to the transformation
     * @param   field
     *          array of fields to sort on
     * @param   ascending
     *          are the orders ascending
     */
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
        if (index < 0 || index >= this.processTo(index))
        {
            return null;
        }
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
            // initisalise the index
            for (int i = 0; i < size; i++)
            {
                this.sorted[i] = i;
            }
            this.quickSort(0, size);
        }
        this.validatedItems=this.size();
        return this.validatedItems;
    }

    private void quickSort(int min, int max)
    {
        if (min < max - 1)
        {
            int pivot = this.partition(min, max);
            if (pivot == max)
            {
                pivot--;
            }
            this.quickSort(min, pivot);
            this.quickSort(pivot, max);
        }
    }


    private int partition(int min, int max)
    {
        int pivot = max - 1;
        int split = max;
        int index = min;
        while (index < split)
        {
            if (compare(index, pivot) <= 0)
            {
                // leave it where it is
                index++;
            }
            else
            {
                split--;
                if (pivot == split)
                {
                    pivot = index;
                }
                int swap = this.sorted[split];
                this.sorted[split] = this.sorted[index];
                this.sorted[index] = swap;
            }
        }
        return split;
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
