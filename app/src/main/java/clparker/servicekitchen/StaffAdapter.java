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
 * Created by Clown on 27/02/2017.
 */

public class StaffAdapter extends ArrayAdapter<Staff> {

    private Context context;
    private List<Staff> items;

    public StaffAdapter (Context context, int resource, ArrayList<Staff> objects) {
        super(context, resource, objects);

        this.context = context;
        this.items = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Staff staffObj = items.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.stafflist, null);

        TextView nameText = (TextView) view.findViewById(R.id.nameText);
        TextView positionText = (TextView) view.findViewById(R.id.positionText);

        String nameTextString, positionTextString;
        nameTextString=staffObj.getName();
        positionTextString=staffObj.getPosition();

        nameText.setText(nameTextString);
        positionText.setText(positionTextString);

        return view;
    }

}
