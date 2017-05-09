package clparker.servicekitchen;

import android.app.Activity;
import android.content.res.Resources;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Thread.sleep;

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
    GridView gridView;
    ArrayList<Order> orderArrayList=new ArrayList<>();
    ArrayList<Order> onscreenOrderArrayList=new ArrayList<>();
    ArrayList<Order_Line> lineArrayList=new ArrayList<>();
    ArrayList<Recipe_Line> recipeLineArrayList=new ArrayList<>();
    ArrayList<Staff> staffArrayList=new ArrayList<>();

    ImageView iv;

    int selection=0;
    int selectionLine=0;
    int ordersNumber=0;

    boolean update=true;

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

        //orderListView=(ListView) findViewById(R.id.ordersListView);
        linesListView=(ListView) findViewById(R.id.linesListView);
        recipeLinesListView=(ListView) findViewById(R.id.recipeLinesListView);
        staffListView = (ListView) findViewById(R.id.staffList);
        gridView = (GridView) findViewById(R.id.ordersGridView);



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
                move(createScreenOrder());
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
        lineArrayList=objectHandler.getOrderArrayList().get(selection).getLines();
        lineArrayAdapter=new LinesAdapter(getActivity(), 0, lineArrayList);
        linesListView.setOnItemClickListener(mMessageClickedHandler);

        linesListView.setAdapter(lineArrayAdapter);
        lineArrayAdapter.notifyDataSetChanged();
    }

    public void setOrderGridAdapter()
    {
        //orderArrayAdapter=new GridOrderAdapter(getActivity(), 0, orderArrayList);
        gridView.setOnItemClickListener(mMessageClickedHandler);
        gridView.setAdapter(new GridOrderAdapter(getActivity(), 0, onscreenOrderArrayList));

        //orderArrayAdapter.notifyDataSetChanged();
    }
/*
    public void setOrderAdapter()
    {
        orderArrayAdapter=new OrderAdapter(getActivity(), 0, orderArrayList);
        orderListView.setOnItemClickListener(mMessageClickedHandler);

        orderListView.setAdapter(orderArrayAdapter);
        orderArrayAdapter.notifyDataSetChanged();
    }
*/
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



    public ImageView createScreenOrder()
    {
        // Initialize a new ImageView widget
        ImageView iv = new ImageView(getApplicationContext());

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_main);

        // Set an image for ImageView
        iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.orderticket2));

        iv.setScaleX(0.3f);

        // Create layout parameters for ImageView
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // Add rule to layout parameters
        // Add the ImageView below to Button
        //lp.addRule(RelativeLayout.BELOW, btn.getId());

        // Add layout parameters to ImageView
        iv.setLayoutParams(lp);

        // Finally, add the ImageView to layout
        rl.addView(iv);
        return iv;
    }

    public void removeScreenOrder()
    {
        iv.invalidate();
        iv.setVisibility(View.INVISIBLE);
    }


    public void move(ImageView image){
        //image = (ImageView)findViewById(R.id.imageViewTest);

        boolean found=false;
        for(int count=0; count<objectHandler.getOrderArrayList().size(); count++)
        {
            if(!objectHandler.getOrderArrayList().get(count).isOnscreen())
            {
                found=true;
            }
        }

        if(found)
        {
            iv = image;
            int xMoveValue = (100 * ordersNumber);
            TranslateAnimation translate1 = new TranslateAnimation(1000, xMoveValue - 135, 0, 0);
            translate1.setDuration(2000);
            image.startAnimation(translate1);
            ordersNumber++;
            BeginAnimation beginAnimation = new BeginAnimation();
            beginAnimation.execute(translate1);
        }

    }

    public Activity getActivity(){return this;}

    private class BeginAnimation extends AsyncTask<Animation, Object, Boolean>
    {
        Animation animation1;
        @Override
        protected Boolean doInBackground(Animation... params)
        {
            animation1 = params[0];
            while(!animation1.hasEnded())
            {

                try
                {
                    sleep(10);
                }
                catch(InterruptedException e)
                {
                    Log.d("Animation", "Wait failed with exception"+e.getMessage());
                }

            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result)
        {
            removeScreenOrder();

            boolean found=false;
            boolean next=false;
            for(int count=0; count<objectHandler.getOrderArrayList().size(); count++)
            {
                if (!objectHandler.getOrderArrayList().get(count).isOnscreen())
                {
                    if(!found)
                    {
                        found = true;
                        objectHandler.getOrderArrayList().get(count).setOnscreen(true);
                        onscreenOrderArrayList.add(objectHandler.getOrderArrayList().get(count));
                    }
                    else
                        next=true;
                }
            }
            setOrderGridAdapter();

            if(next)
                move(createScreenOrder());
            else
            {
                ManageCheckOrders manageCheckOrders = new ManageCheckOrders();
                manageCheckOrders.execute(1);
            }

            //displayNewOrder();
        }
    }

    private class GetOrders extends AsyncTask<Integer, Object, ArrayList<Order>>
    {
        @Override
        protected ArrayList<Order> doInBackground(Integer...params)
        {
            objectHandler.fetchOrders(mClient, cloudDB);
            return objectHandler.getOrderArrayList();
        }

        @Override
        protected void onPostExecute(ArrayList<Order> result)
        {
            //orderArrayList=result;
            setOrderGridAdapter();
            setLinesAdapter();
            setRecipeLinesAdapter();
            setStaffAdapter();
            move(createScreenOrder());


        }
    }

    private class ManageCheckOrders extends AsyncTask<Integer, Object, Boolean>
    {
        @Override
        protected Boolean doInBackground(Integer... params)
        {
            Boolean results=false;
            while(update)
            {
                try
                {
                    sleep(30000);
                }
                catch(InterruptedException e)
                {
                    Log.d("CheckingPoll", "Issue Sleeping"+e.getMessage());
                }

                if(objectHandler.fetchAndCheckOrders(mClient, cloudDB, objectHandler.getOrderArrayList()))
                {
                    results=true;
                }

            }
            return results;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            //if(result.booleanValue())
            //{
                //setOrderGridAdapter();
                //setLinesAdapter();
                //setRecipeLinesAdapter();
                //setStaffAdapter();
                move(createScreenOrder());
            //}
            //else
                //runAgain();
        }
    }
    public void runAgain()
    {
        ManageCheckOrders manageCheckOrders = new ManageCheckOrders();
        manageCheckOrders.execute(1);
    }

    /*
    private class CheckOrders extends AsyncTask<Integer, Object, Boolean>
    {
        @Override
        protected Boolean doInBackground(Integer...params)
        {
            return objectHandler.fetchAndCheckOrders(mClient, cloudDB, objectHandler.getOrderArrayList());
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            if(result)
            {
                setOrderGridAdapter();
                setLinesAdapter();
                setRecipeLinesAdapter();
                setStaffAdapter();
                move(createScreenOrder());
            }
        }
    }
    */
}
