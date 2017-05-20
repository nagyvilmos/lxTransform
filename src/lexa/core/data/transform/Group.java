/*
 * =============================================================================
 * Lexa - Property of William Norman-Walker
 * -----------------------------------------------------------------------------
 * Group.java
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

package lexa.core.data.transform;

import java.util.ArrayList;
import java.util.List;
import lexa.core.data.ArrayDataArray;
import lexa.core.data.ArrayDataSet;
import lexa.core.data.DataArray;
import lexa.core.data.DataItem;
import lexa.core.data.DataSet;

/**
 *
 * @author william
 */
public class Group
        extends TransformStep
{
    private final DataArray keys;           // fields to make up the key a_b_c
    private final boolean strip;            // strip _n from end of key names;
    private final DataArray group;          // all grouping fields
    private final List<Grouping> groupings; // how the numbers should be grouped.
    private int nextRead;

    public Group(Transform parent)
    {
        this(parent, new ArrayDataSet());
    }

    public Group(Transform parent, DataSet grouping)
    {
        super(parent);
        this.keys= grouping.getArray("keys");


        this.strip = grouping.contains("strip") ?
                grouping.getBoolean("strip") : true;

        this.group = this.keys == null ?
                grouping.getArray("group") :
                    new ArrayDataArray(this.keys)
                        .addAll(grouping.getArray("group"));

        this.groupings = Group.buildGroupings(
                grouping.contains("grouping") ?
                    grouping.getDataSet("grouping") :
                    new ArrayDataSet()
                        .put("count",
                            new ArrayDataSet()
                                .put("count",null)));
        this.nextRead=0;
    }

    @Override
    public DataItem item(int index)
    {
        if (index < 0 || index >= this.processTo(index))
        {
            return null;
        }
        return this.results.get(index);
    }

    @Override
    int processTo(int item)
    {
        if (this.results != null)
        {
            return this.validatedItems;
        }
        // one off we have to process the lot.
        this.results=new ArrayDataSet();
        while (this.nextRead < this.previous.size() )
        {
            DataItem next = this.previous.item(this.nextRead);
            DataSet groupData = this.getGroup(next);
            this.applyGroupings(groupData, next.getDataSet());
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

    private void applyGroupings(DataSet groupData, DataSet data)
    {
        for (Grouping grouping : this.groupings)
        {
            grouping.group(groupData, data);
        }
    }

    private DataSet getGroup(DataItem next)
    {
        // based on the grouping

        if (this.group == null)
        {
            // single summation
            if (!this.results.contains("all"))
            {
                this.results.put("all", new ArrayDataSet());
                this.validatedItems++; // actually it's one/
            }
            return this.results.getDataSet("all");
        }
        return this.getGroup(next.getDataSet());
    }

    private DataSet getGroup(DataSet data)
    {
        String key = this.getKeyFromData(data);
        if (this.group == null)
        {
            // single summation
            if (!this.results.contains(key))
            {
                this.results.put(key, new ArrayDataSet());
                this.validatedItems++; // actually it's one/
            }
            return this.results.getDataSet(key);
        }
        throw new UnsupportedOperationException("Group.getGroup not supported yet.");
    }

    private String getKeyFromData(DataSet data)
    {
        throw new UnsupportedOperationException("Group.getKeyFromData not supported yet.");
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
                sums.add(new SumAll(key));
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

    private static class SumAll
            implements Grouping
    {
        private final String key;

        public SumAll(String key)
        {
            this.key = key;
        }

        @Override
        public void group(DataSet total, DataSet item)
        {
//            Object sum = total.contains(this.key) ?
//                    total.getObject(this.key) + item.getObject(this.key) : item.getObject(this.key);
            total.put(this.key, 1);
        }
    }

    private static class Sum
            extends SumAll
    {

        private final String field;

        public Sum(String key, String field)
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
    }}
