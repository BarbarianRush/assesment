package com.example.assesment.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.assesment.R;
import com.example.assesment.model.Gnome;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Icarus on 04/10/2015.
 */
public class GnomeItemAdapter extends ArrayAdapter<Gnome> implements Filterable {

    private ArrayList<Gnome> items;
    private ArrayList<Gnome> filteredItems;
    private int localViewResourceId;


    public GnomeItemAdapter(Context context, int customViewResourceId, ArrayList<Gnome> externalItems) {
        super(context, customViewResourceId, externalItems);
        this.items = externalItems;
        this.filteredItems = externalItems;
        this.localViewResourceId = customViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (localViewResourceId){

            case R.layout.rl_gnome_item:

                final Gnome gnome = filteredItems.get(position);

                if (v == null) {
                    v = inflater.inflate(R.layout.rl_gnome_item, parent, false);
                }

                if (position % 2 == 1) {
                    v.setBackgroundColor(getContext().getResources().getColor(R.color.White));
                } else {
                    v.setBackgroundColor(getContext().getResources().getColor(R.color.AppVLightBlue));
                }

                final ImageView iv_gnome_icon = (ImageView) v.findViewById(R.id.iv_gnome_item_icon);

                if (gnome.getGender().equalsIgnoreCase("m")){
                    iv_gnome_icon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.gnome_m));
                }else{
                    iv_gnome_icon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.gnome_f));
                }

                final TextView tv_gnome_item_name = (TextView) v.findViewById(R.id.tv_gnome_item_name);
                tv_gnome_item_name.setText(gnome.getName());
                final TextView tv_gnome_item_age = (TextView) v.findViewById(R.id.tv_gnome_item_age);
                tv_gnome_item_age.setText("Age: " + gnome.getAge());

                return v;

            default:
                return super.getView(position, convertView, parent);

        }
    }

    @Override
    public int getCount() {
        return filteredItems != null? filteredItems.size() : 0;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredItems = (ArrayList<Gnome>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<Gnome> filteredArrayPatientEpisodes = new ArrayList<Gnome>();

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < items.size(); i++) {
                    String fullName = items.get(i).getName();
                    if (fullName.toLowerCase().startsWith(constraint.toString()))  {
                        filteredArrayPatientEpisodes.add(items.get(i));
                    }
                }

                results.count = filteredArrayPatientEpisodes.size();
                results.values = filteredArrayPatientEpisodes;

                return results;
            }
        };

        return filter;
    }

    public ArrayList<Gnome> getItems() {
        return items;
    }

    public ArrayList<Gnome> getFilteredItems() {
        return filteredItems;
    }


}
