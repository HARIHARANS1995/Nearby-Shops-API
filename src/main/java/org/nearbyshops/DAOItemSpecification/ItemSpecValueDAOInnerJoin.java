package org.nearbyshops.DAOItemSpecification;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.Item;
import org.nearbyshops.Model.ItemCategory;
import org.nearbyshops.Model.Shop;
import org.nearbyshops.Model.ShopItem;
import org.nearbyshops.Model.ModelItemSpecification.ItemSpecificationItem;
import org.nearbyshops.Model.ModelItemSpecification.ItemSpecificationValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 10/3/17.
 */
public class ItemSpecValueDAOInnerJoin {

    private HikariDataSource dataSource = Globals.getDataSource();




    public List<ItemSpecificationValue> getItemSpecNameForFilters(
            Integer itemSpecID,
            Integer itemCatID, Integer shopID,
            Double latCenter, Double lonCenter,
            String searchString,
            String sortBy,
            Integer limit, Integer offset
    ) {


        boolean isfirst = true;
        String query = "";

        String queryJoin = "SELECT DISTINCT "

                + " count ( " + Item.TABLE_NAME + "." + Item.ITEM_ID + " ) as total_items,"
                + ItemSpecificationValue.TABLE_NAME + "." + ItemSpecificationValue.ID + ","
                + ItemSpecificationValue.TABLE_NAME + "." + ItemSpecificationValue.TITLE + ""
//                + ItemSpecificationName.TABLE_NAME + "." + ItemSpecificationName.DESCRIPTION + ","
//                + ItemSpecificationName.TABLE_NAME + "." + ItemSpecificationName.IMAGE_FILENAME + ","
//                + ItemSpecificationName.TABLE_NAME + "." + ItemSpecificationName.GIDB_ID + ","
//                + ItemSpecificationName.TABLE_NAME + "." + ItemSpecificationName.GIDB_SERVICE_URL + ","
//                + ItemSpecificationName.TABLE_NAME + "." + ItemSpecificationName.TIMESTAMP_CREATED + ""

                + " FROM " + ItemSpecificationValue.TABLE_NAME
                + " INNER JOIN " + ItemSpecificationItem.TABLE_NAME + " ON ( " + ItemSpecificationValue.TABLE_NAME + "." + ItemSpecificationValue.ID  + " = "  + ItemSpecificationItem.TABLE_NAME + "." + ItemSpecificationItem.ITEM_SPECIFICATION_VALUE_ID + " ) "
                + " INNER JOIN " + Item.TABLE_NAME + " ON ( " + Item.TABLE_NAME + "." + Item.ITEM_ID + " = "  + ItemSpecificationItem.TABLE_NAME + "." + ItemSpecificationItem.ITEM_ID + " ) "
                + " INNER JOIN " + ItemCategory.TABLE_NAME + " ON ( " + ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + " ) "
                + " INNER JOIN " + ShopItem.TABLE_NAME + " ON ( " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + "=" + Item.TABLE_NAME + "." + Item.ITEM_ID + " ) "
                + " INNER JOIN " + Shop.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + "=" + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + " ) "
                + " WHERE " + Shop.TABLE_NAME + "." + Shop.IS_OPEN + " = TRUE "
                + " AND " + Shop.TABLE_NAME + "." + Shop.SHOP_ENABLED + " = TRUE "
                + " AND " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + " > 0 ";


//        isfirst = false;


        if(itemCatID != null)
        {
            queryJoin = queryJoin + " AND "
                    + ItemCategory.TABLE_NAME
                    + "."
                    + ItemCategory.ITEM_CATEGORY_ID + " = ? ";

//            isfirst = false;
        }


        if(itemSpecID != null)
        {
            String queryPart = ItemSpecificationValue.TABLE_NAME + "." + ItemSpecificationValue.ITEM_SPECIFICATION_NAME_ID + " = ? ";

            queryJoin = queryJoin + " AND " + queryPart;


//            if(isfirst)
//            {
//                queryJoin = queryJoin + " WHERE " + queryPart;
//            }
//            else
//            {
//                queryJoin = queryJoin + " AND " + queryPart;
//            }
//
//            isfirst = false;
        }




        // latCenter, lonCenter,latCenter

        // Applying filters

        if(latCenter !=null && lonCenter !=null)
        {
            // Applying shop visibility filter. Gives all the shops which are visible at the given location defined by
            // latCenter and lonCenter. For more information see the API documentation.


            String queryPartlatLonCenter = "";

            queryPartlatLonCenter = queryPartlatLonCenter + " 6371.01 * acos( cos( radians( ? )) * cos( radians( lat_center) ) * cos(radians( lon_center ) - " +
                    " radians( ? ))"
                    + " + sin( radians( ? )) * sin(radians(lat_center))) <= delivery_range ";

//            String queryPartlatLonCenter = "";

//            queryPartlatLonCenter = queryPartlatLonCenter + " 6371.01 * acos( cos( radians("
//                    + latCenter + ")) * cos( radians( lat_center) ) * cos(radians( lon_center ) - radians("
//                    + lonCenter + "))"
//                    + " + sin( radians(" + latCenter + ")) * sin(radians(lat_center))) <= delivery_range ";


//            if(isfirst)
//            {
//                queryJoin = queryJoin + " WHERE " + queryPartlatLonCenter;
//                isfirst = false;
//            }
//            else
//            {
//                queryJoin = queryJoin + " AND " + queryPartlatLonCenter;
//            }


            queryJoin = queryJoin + " AND " + queryPartlatLonCenter;

        }



        if(searchString !=null)
        {
            String queryPartSearch = Item.TABLE_NAME + "." + Item.ITEM_NAME +" ilike ? ";

            queryJoin = queryJoin + " AND " + queryPartSearch;


//            if(isfirst)
//            {
//                queryJoin = queryJoin + " WHERE " + queryPartSearch;
//                isfirst = false;
//            }
//            else
//            {
//                queryJoin = queryJoin + " AND " + queryPartSearch;
//            }

        }




        if(shopID != null)
        {

//            if(isfirst)
//            {
//
//                queryJoin = queryJoin + " WHERE "
//                        + Shop.TABLE_NAME
//                        + "."
//                        + Shop.SHOP_ID + " = ? ";
//
//                isfirst = false;
//            }
//            else
//            {
//                queryJoin = queryJoin + " AND "
//                        + Shop.TABLE_NAME
//                        + "."
//                        + Shop.SHOP_ID + " = ? ";
//            }



            queryJoin = queryJoin + " AND "
                    + Shop.TABLE_NAME
                    + "."
                    + Shop.SHOP_ID + " = ? ";

        }



        // all the non-aggregate columns which are present in select must be present in group by also.
        queryJoin = queryJoin

                + " group by "
                + ItemSpecificationValue.TABLE_NAME + "." + ItemSpecificationValue.ID;





        if(sortBy!=null)
        {
            if(!sortBy.equals(""))
            {
                String queryPartSortBy = " ORDER BY " + sortBy;

                queryJoin = queryJoin + queryPartSortBy;
            }
        }



        if(limit != null)
        {

            String queryPartLimitOffset = "";

            if(offset>0)
            {
                queryPartLimitOffset = " LIMIT " + limit + " " + " OFFSET " + offset;

            }else
            {
                queryPartLimitOffset = " LIMIT " + limit + " " + " OFFSET " + 0;
            }


            queryJoin = queryJoin + queryPartLimitOffset;
        }




        query = queryJoin;


        ArrayList<ItemSpecificationValue> itemSpecNameList = new ArrayList<ItemSpecificationValue>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);

            int i = 0;
            if(itemCatID!=null)
            {
                statement.setObject(++i,itemCatID);
            }

            if(itemSpecID!=null)
            {
                statement.setObject(++i,itemSpecID);
            }

            if(latCenter!=null && lonCenter!=null)
            {
                statement.setObject(++i,latCenter);
                statement.setObject(++i,lonCenter);
                statement.setObject(++i,latCenter);
            }

            if(searchString!=null)
            {
                statement.setString(++i,"%" + searchString + "%");
            }


            if(shopID!=null)
            {
                statement.setObject(++i,shopID);
            }



            System.out.println(query);
            rs = statement.executeQuery();

            while(rs.next())
            {
                ItemSpecificationValue itemSpecValue = new ItemSpecificationValue();

                itemSpecValue.setId(rs.getInt(ItemSpecificationValue.ID));
                itemSpecValue.setTitle(rs.getString(ItemSpecificationValue.TITLE));
                itemSpecValue.setRt_item_count(rs.getInt("total_items"));
//                itemSpecName.setDescription(rs.getString(ItemSpecificationName.DESCRIPTION));
//                itemSpecName.setImageFilename(rs.getString(ItemSpecificationName.IMAGE_FILENAME));
//                itemSpecName.setGidbID(rs.getInt(ItemSpecificationName.GIDB_ID));
//                itemSpecName.setGidbServiceURL(rs.getString(ItemSpecificationName.GIDB_SERVICE_URL));

                itemSpecNameList.add(itemSpecValue);
            }



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        finally

        {

            try {
                if(rs!=null)
                {rs.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(statement!=null)
                {statement.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(connection!=null)
                {connection.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return itemSpecNameList;
    }



}
