package clparker.servicekitchen;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ObjectHandler objectHandler = new ObjectHandler();
    private CloudDBConnector cloudDB = new CloudDBConnector();
    MobileServiceClient mClient;

    ArrayAdapter<Order> orderArrayAdapter;
    ArrayAdapter<Order_Line> lineArrayAdapter;
    ArrayAdapter<Recipe_Line> recipeLineArrayAdapter;
    ArrayAdapter<Staff> staffArrayAdapter;
    ListView orderListView;
    ListView linesListView;
    ListView recipeLinesListView;
    ListView staffListView;
    ArrayList<Order> orderArrayList=new ArrayList<>();
    ArrayList<Order_Line> lineArrayList=new ArrayList<>();
    ArrayList<Recipe_Line> recipeLineArrayList=new ArrayList<>();
    ArrayList<Staff> staffArrayList=new ArrayList<>();

    int selection=0;
    int selectionLine=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mClient = new MobileServiceClient(
                    "https://cparkertest1.azurewebsites.net",
                    this
            );
        }catch(MalformedURLException e)
        {
            Log.d("CloudDB", "Failed to create mClient object using URL provided"+e.getMessage());
        }

        orderListView=(ListView) findViewById(R.id.ordersListView);
        linesListView=(ListView) findViewById(R.id.linesListView);
        recipeLinesListView=(ListView) findViewById(R.id.recipeLinesListView);
        staffListView = (ListView) findViewById(R.id.staffList);




        GetOrders getOrders = new GetOrders();
        getOrders.execute(1);

    }

    AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {

            if(getResources().getResourceEntryName(parent.getId()).equals("ordersListView"))
            {
                selection = position;
                setLinesAdapter();
            }
            else if(getResources().getResourceEntryName(parent.getId()).equals("linesListView"))
            {
                selectionLine = position;
                setRecipeLinesAdapter();
            }
        }
    };

    public void setRecipeLinesAdapter()
    {
        recipeLineArrayList=lineArrayList.get(selectionLine).getLine().getRecipeLines();
        recipeLineArrayAdapter=new RecipeLineAdapter(getActivity(), 0 , recipeLineArrayList);
        recipeLinesListView.setOnItemClickListener(mMessageClickedHandler);

        recipeLinesListView.setAdapter(recipeLineArrayAdapter);
        recipeLineArrayAdapter.notifyDataSetChanged();
    }

    public void setLinesAdapter()
    {
        lineArrayList=orderArrayList.get(selection).getLines();
        lineArrayAdapter=new LinesAdapter(getActivity(), 0, lineArrayList);
        linesListView.setOnItemClickListener(mMessageClickedHandler);

        linesListView.setAdapter(lineArrayAdapter);
        lineArrayAdapter.notifyDataSetChanged();
    }

    public void setOrderAdapter()
    {
        orderArrayAdapter=new OrderAdapter(getActivity(), 0, orderArrayList);
        orderListView.setOnItemClickListener(mMessageClickedHandler);

        orderListView.setAdapter(orderArrayAdapter);
        orderArrayAdapter.notifyDataSetChanged();
    }

    public void setStaffAdapter()
    {
        staffArrayAdapter=new StaffAdapter(getActivity(), 0, staffArrayList);
        //staffListView.setOnClickListener();

        staffListView.setAdapter(staffArrayAdapter);

        Staff tempStaff = new Staff();
        tempStaff.setName("Derek");
        tempStaff.setPosition("Manager");
        tempStaff.setId("1");
        staffArrayList.add(tempStaff);


        staffArrayAdapter.notifyDataSetChanged();
    }

    public Activity getActivity(){return this;}

    private class GetOrders extends AsyncTask<Integer, Object, ArrayList<Order>>
    {
        @Override
        protected ArrayList<Order> doInBackground(Integer...params)
        {
            /*
            Order newOrder=new Order();
            newOrder.setLocation(1);
            newOrder.setTable(5);
            Calendar nCalendar = Calendar.getInstance();
            newOrder.setCreated(nCalendar);
            newOrder.setCreatedTime(Long.toString(nCalendar.getTimeInMillis()));
            */
            /*
            Order_Line[] orderLine=new Order_Line[3];
            orderLine[0]=new Order_Line();
            orderLine[1]=new Order_Line();
            orderLine[2]=new Order_Line();
            orderLine[0].setRecipe("5d351660-d948-4762-a60b-518caf5a2b19");
            orderLine[1].setRecipe("b373de2c-2319-4b56-8076-427742be8b4b");
            orderLine[2].setRecipe("c0cdd812-6b7b-49b1-86ab-b62937c901c0");
            orderLine[0].setOrderId("41b5b229-fb0a-4b75-9397-1927f9945bd8");
            orderLine[1].setOrderId("41b5b229-fb0a-4b75-9397-1927f9945bd8");
            orderLine[2].setOrderId("41b5b229-fb0a-4b75-9397-1927f9945bd8");
            cloudDB.addOrderLine(mClient, orderLine[0]);
            cloudDB.addOrderLine(mClient, orderLine[1]);
            cloudDB.addOrderLine(mClient, orderLine[2]);
            */
            //newOrder.addLine(orderLine[0]);
            //newOrder.addLine(orderLine[1]);
            //cloudDB.addOrder(mClient, newOrder);

            objectHandler.fetchOrders(mClient, cloudDB);
            return objectHandler.getOrderArrayList();
        }

        @Override
        protected void onPostExecute(ArrayList<Order> result)
        {
            orderArrayList=result;
            setOrderAdapter();
            setLinesAdapter();
            setRecipeLinesAdapter();
            setStaffAdapter();
        }
    }

}
