package clparker.servicekitchen;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clown on 09/01/2017.
 */

public class RecipeLineAdapter extends ArrayAdapter<Recipe_Line> {
    Context context;
    List<Recipe_Line> items;

    public RecipeLineAdapter (Context context, int resource, ArrayList<Recipe_Line> objects) {
        super(context, resource, objects);

        this.context = context;
        this.items = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Recipe_Line itemObj = items.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recipelineslist, null);

        TextView numberText = (TextView) view.findViewById(R.id.numberTextView);
        TextView nameText = (TextView) view.findViewById(R.id.orderTextView);
        TextView altText = (TextView) view.findViewById(R.id.textViewAlt);

        String numberTextString = itemObj.getItem().getItemCategory();
        String nameTextString = itemObj.getItem().getItemSubCategory();
        String altTextString = itemObj.getItem().getItemName();

        numberText.setText(numberTextString);
        numberText.setTextSize(35);
        nameText.setText(nameTextString);
        nameText.setTextSize(35);
        altText.setText(altTextString);
        altText.setTextSize(35);

        return view;
    }
}
