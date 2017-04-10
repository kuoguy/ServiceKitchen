package clparker.servicekitchen;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Clown on 02/01/2017.
 */

public class OrderAdapter extends ArrayAdapter<Order> {

    private Context context;
    private List<Order> items;

    public OrderAdapter(Context context, int resource, ArrayList<Order> objects) {
        super(context, resource, objects);

        this.context = context;
        this.items = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Order itemObj = items.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.orderlist, null);

        TextView numberText = (TextView) view.findViewById(R.id.numberTextView);
        TextView nameText = (TextView) view.findViewById(R.id.orderTextView);
        TextView orderTimeText = (TextView) view.findViewById(R.id.textViewAlt);

        String nameTextString = "Table: ";
        nameTextString+=Integer.toString(itemObj.getTable());
        String numberTextString = "#";
        numberTextString+=Integer.toString(position);

        String orderTimeString = "Age: ";
        Calendar tempCalendar = Calendar.getInstance();
        Long tempTime = tempCalendar.getTimeInMillis();
        Long orderTime = Long.parseLong(itemObj.getCreatedTime());
        tempTime=tempTime-orderTime;
        //tempTime=(tempTime/1000);
        //orderTimeString+=tempTime;

        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);
        String time = df.format(new Date(tempTime));
        orderTimeString+=time;

        numberText.setText(numberTextString);
        numberText.setTextSize(35);
        nameText.setText(nameTextString);
        nameText.setTextSize(35);
        orderTimeText.setText(orderTimeString);
        orderTimeText.setTextSize(30);

        return view;
    }

}
