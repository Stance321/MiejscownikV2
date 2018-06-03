package pl.kuba.jsontest2;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jakub on 02.06.18.
 */

public class DetailsPopupAdapter extends ArrayAdapter<Place> {



    private Context context;
    private List<Place> values;

    public DetailsPopupAdapter(Context context, List<Place> values) {
        super(context, R.layout.placedetails_popup, values);

        this.context = context;
        this.values = values;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;


        if (row == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.placedetails_popup, parent, false);
        }

        TextView nameTextView = (TextView) row.findViewById(R.id.rowPlaceName);
        TextView raitingTextView = (TextView) row.findViewById(R.id.raitingTextView);

        Place item = values.get(position);
        String placeName = item.getName();
        float raiting = item.getRating();
        nameTextView.setText(placeName);
        raitingTextView.setText(""+raiting);


        return row;
    }

    @Nullable
    @Override
    public Place getItem(int position) {
        return super.getItem(position);
    }
}
