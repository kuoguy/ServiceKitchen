package clparker.servicekitchen;



import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Clown on 02/01/2017.
 */

public class ObjectHandler {

    ArrayList<Order> orderArrayList = new ArrayList<>();
    ArrayList<Staff> staffArrayList = new ArrayList<>();
    ArrayList<Item> itemArrayList = new ArrayList<>();

    public ArrayList<Order> getOrderArrayList() {return orderArrayList;}

    //Fetches all orders for this location and places in ordersArrayList
    public void fetchOrders(MobileServiceClient mClient, CloudDBConnector cloudDB)
    {
        List<Order> orders=cloudDB.getOrders(mClient, 0);
        orderArrayList.clear();
        Log.d("CloudDBConnector", "Orders size: "+orders.size());
        if(orders!=null)
        {
            for (int count = 0; count < orders.size(); count++) {
                orderArrayList.add(orders.get(count));

            }
        }

    }

    public ArrayList<Order> fetchAndCheckOrders(MobileServiceClient mClient, CloudDBConnector cloudDB, ArrayList<Order> currentOrders)
    {
        ArrayList<Order> foundOrders = cloudDB.checkAndGetOrders(mClient, currentOrders, 0);
        boolean result=false;

        if(foundOrders.size()!=0)
        {
            result=true;
            for(int count=0; count<foundOrders.size(); count++)
            {

                orderArrayList.add(foundOrders.get(count));
                orderArrayList.get(count).setOnscreen(false);
            }
        }

        return foundOrders;
    }





}
