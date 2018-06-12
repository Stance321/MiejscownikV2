package pl.kuba.jsontest2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakub on 25.05.18.
 */

public class PlacesAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<Place> allPlaces;
    private List<Place> filteredPlaces;
    private int selectedDistance;
    private ItemFilter mFilter = new ItemFilter();
    TextView rowPlaceName;
    TextView rowPlaceDistance;
    ViewHolder holder;
    boolean Opened;

    public PlacesAdapter(Context context, List<Place> values, int selectedDistance, boolean Opened) {
     //   super(context, R.layout.rowofplace, values);

        this.context = context;
        this.allPlaces = values;
        this.filteredPlaces = values;
        this.Opened = Opened;

        this.selectedDistance = selectedDistance;
    }

    @Override
    public int getCount() {
        return filteredPlaces.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredPlaces.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // View row = convertView;


        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rowofplace, parent, false);
            holder = new ViewHolder();

            holder.textPlaceName = (TextView) convertView.findViewById(R.id.rowPlaceName);
            holder.textPlaceDistance = (TextView) convertView.findViewById(R.id.rowPlaceDisance);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.textPlaceName.setText(filteredPlaces.get(position).getName());
        holder.textPlaceDistance.setText(String.valueOf(new Double(
                filteredPlaces.get(position)
                .getDistance()).intValue())
                + " meters ");
        if(filteredPlaces.get(position).getOpeningHours().openNow){
            holder.textOpened.setText("Otwarte");
        }
        else
            {
                holder.textOpened.setText("Zamkniete");
            }

        return convertView;
    }

    public Filter getFilter() {
        return mFilter;
    }

    static class ViewHolder{
        TextView textPlaceName;
        TextView textPlaceDistance;
        TextView textOpened;
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
                if (listofPlaces.get(i).getDistance() <= selectedDistance && listofPlaces.get(i).getOpeningHours().openNow == Opened) {
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
            filteredPlaces = (ArrayList<Place>) results.values;
            notifyDataSetChanged();

        }
    }
}
