/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * Group.java (lxTransform)
 *------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: May 2017
 *==============================================================================
 */
package lexa.core.transform;

import java.util.ArrayList;
import java.util.List;
import lexa.core.data.DataArray;
import lexa.core.data.DataFactory;
import lexa.core.data.DataItem;
import lexa.core.data.DataSet;
import lexa.core.data.DataType;
import lexa.core.data.DataValue;

/**
 * Grouping functions for a transform
 * <p>
 * Without any parameters, the grouping operation simply counts all the entries
 * resulting in a data set of contents:
 * <pre>
 * count % &lt;number of items&gt;
 * </pre>
 * <p>
 * Given group data, as a data set, the data is grouped by a key and outputs
 * calculated.
 * Grouping data is in the format:
 * <pre>
 * key [
 *   &lt;field name&gt;
 *   ...
 * ]
 * strip ? &lt;flag to strip trailing _n from key names&gt;
 * group [
 *   &lt;field name&gt;
 *   ...
 * ]
 * grouping {
 *   &lt;group&gt; {
 *     &lt;action&gt; - &lt;field&gt;
 *   }
 * }
 * </pre>
 *
 * @author william
 * @since 2017-05
 */
public class Group
        extends TransformStep
{
    /** fields to make up the key a_b_c */
    private final DataArray key;
    /** strip _n from end of key names; */
    private final boolean strip;
    /** all grouping fields */
    private final DataArray group;
    /** how the numbers should be grouped. */
    private final List<Grouping> groupings;
    /** next input to read */
    private int nextRead;

    /**
     * Create a group transform to count the entries.
     *
     * @param   parent
     *          the parent to the transformation
     */
    public Group(Transform parent)
    {
        this(parent, parent.factory().getDataSet());
    }

    /**
     * Create a group transform
     *
     * @param   parent
     *          the parent to the transformation
     * @param   grouping
     *          grouping parameters; see {@link Group}
     */
    public Group(Transform parent, DataSet grouping)
    {
        super(parent);
        this.key= DataType.ARRAY.equals(grouping.getType("key")) ?
                grouping.getArray("key") :
                grouping.factory().getDataArray();

        this.strip = DataType.BOOLEAN.equals(grouping.getType("strip")) ?
                grouping.getBoolean("strip") : true;

        this.group = DataType.ARRAY.equals(grouping.getType("group")) ?
                grouping.getArray("group") :
                grouping.factory().getDataArray();
        this.groupings = Group.buildGroupings(
                grouping.contains("grouping") ?
                    grouping.getDataSet("grouping") :
                    super.factory().getDataSet()
                        .put("count",
                            super.factory().getDataSet()
                                .put("count",null)));
        this.nextRead=0;
    }

    @Override
    int processTo(int item)
    {
        if (item <= this.validatedItems)
        {
            return this.validatedItems;
        }
        // one off we have to process the lot.
        this.results=super.factory().getDataSet();
        while (this.nextRead < this.previous.size() )
        {
            DataItem next = this.previous.item(this.nextRead);
            DataSet groupData = this.getGroup(next);
            this.applyGroupings(groupData, next.getDataSet());
            this.nextRead++;
        }
        this.validatedItems = this.results.size();
        return this.validatedItems;
    }

    @Override
    public int size()
    {
        this.processTo(Integer.MAX_VALUE);
        return this.validatedItems;
    }

    private void applyGroupings(DataSet groupData, DataSet data)
    {
        for (Grouping grouping : this.groupings)
        {
            grouping.group(groupData, data);
        }
    }

    private DataSet getGroup(DataItem item)
    {
        DataSet data = item.getDataSet();
        return this.getGroupByKey(data);
    }

    private DataSet getGroupByKey(DataSet data)
    {
        // based on the GROUP names,
        String id;
        if (this.key.size() > 0)
        {
            id = data.getObject(this.key.get(0).getString()).toString();
            // no append extra keys
            for (int k = 1; k < this.key.size(); k++)
            {
                id = id + '_' +
                        data.getObject(this.key.get(k).getString()).toString();
            }
        }
        else
        {
            id = "all";
        }
        DataSet keyData = this.results.getDataSet(id);
        if (keyData == null)
        {
            DataFactory factory = this.factory();
            keyData = factory.getDataSet();
            for (DataValue value : this.key)
            {
                String k = value.getString();
                keyData.put(factory.getDataItem(k,
                        factory.clone(data.getValue(k))
                ));
            }
            this.results.put(id, keyData);
        }
        return this.getGroupByGroup(keyData, data);
    }

    private DataSet getGroupByGroup(DataSet keyData, DataSet item)
    {
        if (this.group.size() == 0)
        {
            return keyData;
        }
        throw new UnsupportedOperationException();
    }

    private static List<Grouping> buildCounts(DataSet countData)
    {
        List<Grouping> counts = new ArrayList();

        for (DataItem countItem : countData)
        {
            String key = countItem.getKey();
            String field = countItem.getString();

            if (field == null )
            {
                counts.add(new CountAll(key));
            }
            else
            {
                counts.add(new Count(key, field));
            }
        }

        return counts;
    }

    private static List<Grouping> buildGroupings(DataSet GroupingData)
    {
        List<Grouping> groupings = new ArrayList();
        for (DataItem groupItem : GroupingData)
        {
            DataSet groupData = groupItem.getDataSet();
            switch (groupItem.getKey())
            {
                case "count" :  { groupings.addAll(Group.buildCounts(groupData));   break; }
                case "sum"   :  { groupings.addAll(Group.buildSums(groupData));     break; }
                default :
                {
                    throw new UnsupportedOperationException("Group.buildGroupings not supported yet.");
                }
            }

        }
        return groupings;
    }
    private static List<Grouping> buildSums(DataSet sumData)
    {
        List<Grouping> sums = new ArrayList();

        for (DataItem sumItem : sumData)
        {
            String key = sumItem.getKey();
            String field = sumItem.getString();

            if (field == null )
            {
                throw new IllegalArgumentException("Cannot sum without a field name");
            }
            else
            {
                sums.add(new Sum(key, field));
            }
        }

        return sums;
    }

    private static class CountAll
            implements Grouping
    {
        private final String key;

        public CountAll(String key)
        {
            this.key = key;
        }

        @Override
        public void group(DataSet total, DataSet item)
        {
            int count = total.contains(this.key) ?
                    total.getInteger(this.key) + 1 : 1;
            total.put(this.key, count);
        }
    }

    private static class Count
            extends CountAll
    {

        private final String field;

        public Count(String key, String field)
        {
            super(key);
            this.field = field;
        }

        @Override
        public void group(DataSet total, DataSet item)
        {
            if (item.contains(this.field))
            {
                super.group(total,item);
            }
        }
    }

    private interface Grouping
    {
        void group(DataSet total, DataSet item);
    }

    private static class Sum
            implements Grouping
    {
        private final String key;
        private final String field;

        public Sum(String key, String field)
        {
            this.key = key;
            this.field = field;
        }

        @Override
        public void group(DataSet total, DataSet item)
        {
            if (item.contains(this.field))
            {
                DataItem addItem = item.get(this.field);
                DataItem totalItem = total.get(this.key);
                if (totalItem == null)
                {
                    total.put(this.key, addItem.getValue());
                }
                else
                {
                    switch (totalItem.getType())
                    {
                        case INTEGER :
                        {
                            total.put(this.key,
                                    totalItem.getInteger() + addItem.getInteger());
                            break;
                        }
                        case LONG :
                        {
                            total.put(this.key,
                                    totalItem.getLong()+ addItem.getLong());
                            break;
                        }
                        case DOUBLE :
                        {
                            total.put(this.key,
                                    totalItem.getDouble()+ addItem.getDouble());
                            break;
                        }
                    }
                }
            }
        }
    }
}
