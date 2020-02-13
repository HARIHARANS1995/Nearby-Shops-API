package org.nearbyshops.Model.ModelEndpoint;

import org.nearbyshops.Model.Item;
import org.nearbyshops.Model.ItemCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 30/6/16.
 */
public class ItemEndPoint {

    private Integer itemCount;
    private Integer offset;
    private Integer limit;
    private Integer max_limit;
    private List<Item> results;
    private List<ItemCategory> subcategories;






    // getters and setters

    public List<ItemCategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<ItemCategory> subcategories) {
        this.subcategories = subcategories;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }


    public List<Item> getResults() {
        return results;
    }

    public void setResults(List<Item> results) {
        this.results = results;
    }

    public Integer getMax_limit() {
        return max_limit;
    }

    public void setMax_limit(Integer max_limit) {
        this.max_limit = max_limit;
    }
}
