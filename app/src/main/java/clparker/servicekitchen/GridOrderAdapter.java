package clparker.servicekitchen;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Clown on 09/05/2017.
 */

public class GridOrderAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Order> items;

    public GridOrderAdapter(Context context, int resource, ArrayList<Order> objects) {
        this.context=context;
        this.items=objects;
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Order itemObj=items.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.gridorderlist, null);

        TextView t1 = (TextView) view.findViewById(R.id.textViewNumber);
        TextView t2 = (TextView) view.findViewById(R.id.textViewID);
        TextView t3 = (TextView) view.findViewById(R.id.textViewTime);

        String nameString = Integer.toString(position);
        String idString = itemObj.getStatus();
        String timeString = itemObj.getCreatedTime();

        t1.setText(nameString);
        t1.setTextSize(14);
        t2.setText(idString);
        t2.setTextSize(8);
        t3.setText(timeString);
        t3.setTextSize(8);

        return view;
    }
}