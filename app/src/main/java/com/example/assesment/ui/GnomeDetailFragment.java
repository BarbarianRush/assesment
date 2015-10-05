package com.example.assesment.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.assesment.R;
import com.example.assesment.android.GnomeItemAdapter;
import com.example.assesment.android.MainActivityEventDispatch;
import com.example.assesment.connection.JsonParser;
import com.example.assesment.model.Gnome;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Icarus on 04/10/2015.
 */
public class GnomeDetailFragment extends Fragment {

    private MainActivityEventDispatch.FragmentTag fTag = MainActivityEventDispatch.FragmentTag.GNOME_DETAIL;

    private JsonParser jsonParser;
    private Gnome gnome;
    private List<String> gnomeNameList;
    private ArrayList<Gnome> totalGnomes;
    private ArrayList<Gnome> gnomeFriendsList;

    private GnomeItemAdapter gnomeItemAdapter;

    private ListView lv_fragment_gnome_friends;
    private TextView tv_fragment_gnome_detail_weight, tv_fragment_gnome_detail_height, tv_fragment_gnome_detail_hair_color, tv_fragment_gnome_detail_professions;
    private FrameLayout fl_fragment_gnome_detail_hair_color_code;

    public GnomeDetailFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null){
            gnome = (Gnome) savedInstanceState.getParcelable("Gnome");
            totalGnomes = (ArrayList<Gnome>) savedInstanceState.getSerializable("Gnomes");
            gnomeNameList = new ArrayList<String>();
            gnomeNameList.addAll(gnome.getFriends());

        }

        View view = inflater.inflate(R.layout.fragment_gnome_detail, container, false);

        jsonParser = new JsonParser();

        lv_fragment_gnome_friends = (ListView) view.findViewById(R.id.lv_fragment_gnome_friends);

        tv_fragment_gnome_detail_weight = (TextView) view.findViewById(R.id.tv_fragment_gnome_detail_weight);
        tv_fragment_gnome_detail_height = (TextView) view.findViewById(R.id.tv_fragment_gnome_detail_height);
        tv_fragment_gnome_detail_hair_color = (TextView) view.findViewById(R.id.tv_fragment_gnome_detail_hair_color);
        tv_fragment_gnome_detail_professions = (TextView) view.findViewById(R.id.tv_fragment_gnome_detail_professions);
        fl_fragment_gnome_detail_hair_color_code = (FrameLayout) view.findViewById(R.id.fl_fragment_gnome_detail_hair_color_code);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ((MainActivityEventDispatch) getActivity()).onFragmentTransition(fTag);

        Bundle args = getArguments();
        if (args.containsKey("Gnome")){
            gnome = (Gnome) args.getParcelable("Gnome");
            totalGnomes = (ArrayList<Gnome>) args.getSerializable("Gnomes");
            gnomeNameList = new ArrayList<String>();
            gnomeNameList.addAll(gnome.getFriends());

            tv_fragment_gnome_detail_weight.setText(gnome.getWeight());
            tv_fragment_gnome_detail_height.setText(gnome.getHeight());
            tv_fragment_gnome_detail_hair_color.setText(gnome.getHair_color());
            if (gnome.getHair_color().equalsIgnoreCase("Black")){
                fl_fragment_gnome_detail_hair_color_code.setBackgroundColor(getResources().getColor(R.color.black));
            }else if(gnome.getHair_color().equalsIgnoreCase("Pink")){
                fl_fragment_gnome_detail_hair_color_code.setBackgroundColor(getResources().getColor(R.color.fuchsia));
            }else if(gnome.getHair_color().equalsIgnoreCase("Green")){
                fl_fragment_gnome_detail_hair_color_code.setBackgroundColor(getResources().getColor(R.color.green));
            }else if(gnome.getHair_color().equalsIgnoreCase("Red")){
                fl_fragment_gnome_detail_hair_color_code.setBackgroundColor(getResources().getColor(R.color.red));
            }else if(gnome.getHair_color().equalsIgnoreCase("Gray")){
                fl_fragment_gnome_detail_hair_color_code.setBackgroundColor(getResources().getColor(R.color.gray));
            }
            List<String> professions = gnome.getProfessions();
            String professionsString = "";
            for (int i=0; i<=professions.size()-1; i++){
                professionsString = professionsString + professions.get(i);
                if (i < professions.size()-1){
                    professionsString = professionsString + ", ";
                }
            }

            tv_fragment_gnome_detail_professions.setText(professionsString);

            ((MainActivityEventDispatch) getActivity()).changeMainContentHeader("Gnome aged: " + gnome.getAge(), gnome.getName());
            ((MainActivityEventDispatch) getActivity()).changeMainContentPicture(gnome.getThumbnail());

            gnomeFriendsList = new ArrayList<Gnome>();

            for (int i=0; i<= gnomeNameList.size()-1; i++){
                for (int j = 0; j <= totalGnomes.size() - 1; j++) {
                    Gnome gnome = totalGnomes.get(j);
                    if (gnome.getName().equalsIgnoreCase(gnomeNameList.get(i))){
                        gnomeFriendsList.add(gnome);
                    }
                }
            }

            gnomeItemAdapter = new GnomeItemAdapter(getActivity(), R.layout.rl_gnome_item, gnomeFriendsList);
            lv_fragment_gnome_friends.setAdapter(gnomeItemAdapter);
            lv_fragment_gnome_friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.button_click));
                    Gnome gnome = ((GnomeItemAdapter) parent.getAdapter()).getFilteredItems().get(position);

                    Bundle args = new Bundle();
                    args.putParcelable("Gnome", gnome);
                    args.putSerializable("Gnomes", totalGnomes);

                    Fragment fragment = new GnomeDetailFragment();
                    fragment.setArguments(args);
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fl_main_fragment_container, fragment, "GnomeDetailFragment");
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
        }

    }
}
