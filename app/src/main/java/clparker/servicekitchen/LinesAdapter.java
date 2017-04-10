package clparker.servicekitchen;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clown on 03/01/2017.
 */

public class LinesAdapter extends ArrayAdapter<Order_Line> {

    private Context context;
    private List<Order_Line> items;

    public LinesAdapter (Context context, int resource, ArrayList<Order_Line> objects) {
        super(context, resource, objects);

        this.context = context;
        this.items = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Order_Line itemObj = items.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.orderlineslist, null);

        TextView numberText = (TextView) view.findViewById(R.id.numberTextView);
        TextView nameText = (TextView) view.findViewById(R.id.orderTextView);

        String nameTextString = itemObj.getLine().getName();
        String numberTextString = Integer.toString(itemObj.getQuantity());

        numberText.setText(numberTextString);
        numberText.setTextSize(35);
        nameText.setText(nameTextString);
        nameText.setTextSize(35);

        return view;
    }


}
