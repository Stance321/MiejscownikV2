package pl.kuba.jsontest2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakub on 25.05.18.
 */

public class PlacesAdapter extends ArrayAdapter<Place> implements Filterable {

    private Context context;
    private List<Place> allPlaces;
    private List<Place> filteredPlaces;
    private int selectedDistance;

    public PlacesAdapter(Context context, List<Place> values, int selectedDistance) {
        super(context, R.layout.rowofplace, values);

        this.context = context;
        this.allPlaces = values;
        this.filteredPlaces = values;

        this.selectedDistance = selectedDistance;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.rowofplace, parent, false);
        }

        TextView rowPlaceName = (TextView) row.findViewById(R.id.rowPlaceName);
        TextView rowPlaceDistance = (TextView) row.findViewById(R.id.rowPlaceDisance);

        Place item = allPlaces.get(position);
        if(selectedDistance >= item.getDistance()) {

            rowPlaceName.setText(item.getName());
            Double a = new Double(item.getDistance());

            rowPlaceDistance.setText(String.valueOf(a.intValue()) + " meters");
        }

        return row;
    }


    private class ItemFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            Integer a = new Integer(Integer.parseInt(charSequence.toString()));

            return null;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredPlaces = (ArrayList<Place>) filterResults.values;

        }
    }
}
