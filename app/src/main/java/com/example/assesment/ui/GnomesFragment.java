package com.example.assesment.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.assesment.R;
import com.example.assesment.android.GnomeItemAdapter;
import com.example.assesment.android.MainActivityEventDispatch;
import com.example.assesment.connection.JsonParser;
import com.example.assesment.model.Gnome;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

/**
 * Created by Icarus on 04/10/2015.
 */
public class GnomesFragment extends Fragment {

    private MainActivityEventDispatch.FragmentTag fTag = MainActivityEventDispatch.FragmentTag.GNOMES;

    private JsonParser jsonParser;
    private Hashtable<String,Object> gnomes;
    Random random = new Random();

    private Button bt_fragment_gnomes_search;
    private EditText et_fragment_gnomes_search;
    private ListView lv_fragment_gnomes;
    private ArrayList<Gnome> gnomesList;

    private GnomeItemAdapter gnomeItemAdapter;

    public GnomesFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        jsonParser = new JsonParser();

        if (savedInstanceState != null) {
            gnomes = (Hashtable<String,Object>)savedInstanceState.getSerializable("Gnomes");
            gnomesList = (ArrayList<Gnome>) savedInstanceState.getSerializable("GnomesList");
        }

        View v = inflater.inflate(R.layout.fragment_gnomes, container, false);
        lv_fragment_gnomes = (ListView) v.findViewById(R.id.lv_fragment_gnomes);
        et_fragment_gnomes_search = (EditText) v.findViewById(R.id.et_fragment_gnomes_search);
        bt_fragment_gnomes_search = (Button) v.findViewById(R.id.bt_fragment_gnomes_search);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();

        if (args != null){
            gnomes = (Hashtable<String,Object>)args.getSerializable("gnomes");
        }

        ((MainActivityEventDispatch)getActivity()).onFragmentTransition(fTag);

        if (gnomesList == null) {
            gnomesList = new ArrayList<Gnome>();

            for (int i = 0; i <= gnomes.size() - 1; i++) {
                Gnome gnomeItem = new Gnome();
                Hashtable<String, Object> gnome = (Hashtable<String, Object>) gnomes.get(String.valueOf(i));
                gnomeItem.setId(gnome.get("id").toString());
                gnomeItem.setName(gnome.get("name").toString());
                gnomeItem.setThumbnail(gnome.get("thumbnail").toString());
                gnomeItem.setAge(gnome.get("age").toString());
                gnomeItem.setWeight(gnome.get("weight").toString());
                gnomeItem.setHeight(gnome.get("height").toString());
                gnomeItem.setHair_color(gnome.get("hair_color").toString());
                if (random.nextBoolean() == true) {
                    gnomeItem.setGender("m");
                } else {
                    gnomeItem.setGender("f");
                }
                Hashtable<String, Object> professions = (Hashtable<String, Object>) gnome.get("professions");
                for (int j = 0; j <= professions.size() - 1; j++) {
                    gnomeItem.getProfessions().add(String.valueOf(professions.get(String.valueOf(j))));
                }

                Hashtable<String, Object> friends = (Hashtable<String, Object>) gnome.get("friends");
                for (int j = 0; j <= friends.size() - 1; j++) {
                    gnomeItem.getFriends().add(String.valueOf(friends.get(String.valueOf(j))));
                }
                //gnomeItem.setProfession(gnome.get("professions").toString());
                //gnomeItem.setAge(getDiffYears(birthDate, currentDate));

                gnomesList.add(gnomeItem);
            }

        }
        if (gnomesList.size() == 0){
            //placeholder incase of empty json

            /*gnomesList.add(new PatientEpisode(getActivity()));
            gnomeItemAdapter = new PatientItemAdapter(getActivity(), R.layout.tv_empty_patient_episode_list_placeholder, gnomesList);
            lv_fragment_patients.setAdapter(gnomeItemAdapter);
            lv_fragment_patients.setClickable(false);
            et_fragment_patients_search.setFocusable(false);*/

        }else {
            gnomeItemAdapter = new GnomeItemAdapter(getActivity(), R.layout.rl_gnome_item, gnomesList);
            lv_fragment_gnomes.setAdapter(gnomeItemAdapter);
            lv_fragment_gnomes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.button_click));
                    Gnome gnome = ((GnomeItemAdapter) parent.getAdapter()).getFilteredItems().get(position);

                    Bundle args = new Bundle();
                    args.putParcelable("Gnome", gnome);
                    args.putSerializable("Gnomes", gnomesList);

                    Fragment fragment = new GnomeDetailFragment();
                    fragment.setArguments(args);
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fl_main_fragment_container, fragment, "GnomeDetailFragment");
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
        }

        et_fragment_gnomes_search.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 0){
                    gnomeItemAdapter.getFilter().filter(s.toString());
                }
            }
        });

        bt_fragment_gnomes_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.button_click));

                gnomeItemAdapter.getFilter().filter(et_fragment_gnomes_search.getEditableText().toString());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (gnomes != null) {
            Bundle args = new Bundle();
            args.putSerializable("Gnomes", gnomes);
            args.putSerializable("GnomesList", gnomesList);
        }
        super.onSaveInstanceState(outState);
    }
}
