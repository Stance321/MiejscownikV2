package pl.kuba.jsontest2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

import java.util.List;

/**
 * Created by jakub on 25.05.18.
 */

public class PlacesAdapter extends ArrayAdapter<Place> {

    private Context context;
    private List<Place> values;

    public PlacesAdapter(Context context, List<Place> values) {
        super(context, R.layout.rowofplace, values);

        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.rowofplace, parent, false);
        }

        TextView textView = (TextView) row.findViewById(R.id.rowPlaceName);

        Place item = values.get(position);
        String message = item.getName();
        textView.setText(message);

        return row;
    }

}
