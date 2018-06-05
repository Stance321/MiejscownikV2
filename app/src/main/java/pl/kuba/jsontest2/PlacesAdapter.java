package pl.kuba.jsontest2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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

public class PlacesAdapter extends ArrayAdapter implements Filterable {

    private Context context;
    private List<Place> allPlaces;
    private List<Place> filteredPlaces;
    private int selectedDistance;
    private ItemFilter mFilter = new ItemFilter();
    TextView rowPlaceName;
    TextView rowPlaceDistance;


    public PlacesAdapter(Context context, List<Place> values, int selectedDistance) {
        super(context, R.layout.rowofplace, values);

        this.context = context;
        this.allPlaces = values;
        this.filteredPlaces = values;

        this.selectedDistance = selectedDistance;
    }

    @Override
    public int getCount() {
        return filteredPlaces.size();
    }



    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;


        if (row == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.rowofplace, parent, false);

            rowPlaceName = (TextView) row.findViewById(R.id.rowPlaceName);
            rowPlaceDistance = (TextView) row.findViewById(R.id.rowPlaceDisance);
        }



        rowPlaceName.setText(filteredPlaces.get(position).getName());
        rowPlaceDistance.setText(String.valueOf(new Double(
                filteredPlaces.get(position)
                .getDistance()).intValue())
                + " meters");
        return row;
    }

    public Filter getFilter() {
        return mFilter;
    }


    private class ItemFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            Double a = new Double(Double.parseDouble(charSequence.toString()));
            FilterResults results = new FilterResults();

            final List<Place> listofPlaces = allPlaces;

            int count = listofPlaces.size();

            final ArrayList<Place> nList = new ArrayList<Place>(count);


            for(int i =0; i<count; i++){
                if (listofPlaces.get(i).getDistance() <= selectedDistance) {
                    nList.add(listofPlaces.get(i));
                    System.out.println(listofPlaces.get(i).getDistance() + " SELECTED DISTANCE " + selectedDistance);

                }
            }
            results.values = nList;
            results.count = nList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            filteredPlaces = (List<Place>) results.values;
            notifyDataSetChanged();

        }
    }
}
