package org.nearbyshops.DAOs.DAOOrders;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Model.ModelOrderStatus.OrderStatusHomeDelivery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DAOOrderDeliveryGuy {

    private HikariDataSource dataSource = Globals.getDataSource();




    public int pickupOrder(int orderID,int deliveryGuyID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME

                + " SET " + Order.STATUS_HOME_DELIVERY + " = ?,"
                            + Order.DELIVERY_GUY_SELF_ID + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?";





        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);
            int i = 0;

            statement.setObject(++i,OrderStatusHomeDelivery.HANDOVER_REQUESTED);
            statement.setObject(++i,deliveryGuyID);
            statement.setObject(++i,orderID);


            updatedRows = statement.executeUpdate();
            System.out.println("Total rows updated: " + updatedRows);

            //conn.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {

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



        return updatedRows;
    }



    public int acceptOrder(int orderID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME
                            + " SET "   + Order.STATUS_HOME_DELIVERY + " = ?"
                            + " WHERE " + Order.ORDER_ID + " = ?"
                            + " AND "   + Order.STATUS_HOME_DELIVERY + " = ? ";


        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusHomeDelivery.OUT_FOR_DELIVERY);
            statement.setObject(++i, orderID);
            statement.setObject(++i, OrderStatusHomeDelivery.HANDOVER_REQUESTED);


            updatedRows = statement.executeUpdate();
//            System.out.println("Total rows updated: " + updatedRows);

            //conn.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally

        {

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

        return updatedRows;
    }



    public int declineOrder(int orderID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME
                + " SET "   + Order.STATUS_HOME_DELIVERY + " = ?,"
                            + Order.DELIVERY_GUY_SELF_ID + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "   + Order.STATUS_HOME_DELIVERY + " = ? ";


        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusHomeDelivery.ORDER_PACKED);
            statement.setObject(++i,null);
            statement.setObject(++i,orderID);
            statement.setObject(++i, OrderStatusHomeDelivery.HANDOVER_REQUESTED);


            updatedRows = statement.executeUpdate();
            System.out.println("Total rows updated: " + updatedRows);

            //conn.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally

        {

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

        return updatedRows;
    }



    public int deliverOrder(int orderID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME
                + " SET "   + Order.STATUS_HOME_DELIVERY + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "   + Order.STATUS_HOME_DELIVERY + " = ? ";


        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusHomeDelivery.DELIVERED);
            statement.setObject(++i,orderID);
            statement.setObject(++i, OrderStatusHomeDelivery.OUT_FOR_DELIVERY);


            updatedRows = statement.executeUpdate();
            System.out.println("Total rows updated: " + updatedRows);

            //conn.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally

        {

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

        return updatedRows;
    }




    public int returnOrder(int orderID)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME
                + " SET "   + Order.STATUS_HOME_DELIVERY + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?"
                + " AND "   + Order.STATUS_HOME_DELIVERY + " = ? ";


        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setObject(++i, OrderStatusHomeDelivery.RETURN_REQUESTED);
            statement.setObject(++i,orderID);
            statement.setObject(++i, OrderStatusHomeDelivery.OUT_FOR_DELIVERY);


            updatedRows = statement.executeUpdate();
            System.out.println("Total rows updated: " + updatedRows);

            //conn.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally

        {

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

        return updatedRows;
    }


}
