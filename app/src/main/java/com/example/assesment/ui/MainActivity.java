package com.example.assesment.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.assesment.R;
import com.example.assesment.android.MainActivityEventDispatch;
import com.example.assesment.connection.JsonParser;
import com.example.assesment.connection.PetitionRequestMonitoring;
import com.example.assesment.connection.RestClient;
import com.example.assesment.model.Gnome;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;

public class MainActivity extends ActionBarActivity implements MainActivityEventDispatch {

    FragmentTag currentFragmentDisplayed = FragmentTag.GNOMES;

    private ImageView iv_main_content_header;
    private TextView tv_main_content_header_title;
    private TextView tv_main_content_header_name;

    RestClient rs;
    JsonParser jsonParser;
    private DialogLoading dialogLoading;
    private DialogDefault dialogLogout;
    private Hashtable<String,Object> gnomes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jsonParser = new JsonParser();

        iv_main_content_header = (ImageView) findViewById(R.id.iv_main_content_header);
        tv_main_content_header_title = (TextView) findViewById(R.id.tv_main_content_header_title);
        tv_main_content_header_name = (TextView) findViewById(R.id.tv_main_content_header_name);

        dialogLoading = new DialogLoading(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLoading.dismiss();
            }
        });

        dialogLogout = new DialogDefault(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch ((Integer)v.getTag()){
                    case 1:
                        finish();
                        break;
                    case 0:
                        dialogLogout.dismiss();
                        break;
                }
            }
        });

        rs = new RestClient(this);
        rs.findGnomes(new PetitionRequestMonitoring() {
            @Override
            public void onServiceRequestStarted() {
                dialogLoading.show();
                dialogLoading.setCustomTitle("Conectivity status");
                dialogLoading.setTextBodyColor(getResources().getColor(R.color.green));
                dialogLoading.setAcceptButtonText("OK");
                dialogLoading.setTextBody("Recovering data...");
                dialogLoading.setPb_dialog_loadingVisibility(View.VISIBLE);
                dialogLoading.setBt_dialog_loading_acceptVisibility(View.GONE);
            }

            @Override
            public void onServiceRequestFinished(String result) {
                Log.i("Test", "search results " + result);

                if (result == null || result.equalsIgnoreCase("")) {
                    dialogLoading.setTextBodyColor(getResources().getColor(R.color.red));
                    dialogLoading.setTextBody("Connection error, check your internet availability and try again.");
                    dialogLoading.setPb_dialog_loadingVisibility(View.GONE);
                    dialogLoading.setBt_dialog_loading_acceptVisibility(View.VISIBLE);
                } else {
                    jsonParser = new JsonParser();
                    gnomes = jsonParser.retrieveJson(result);
                    gnomes = (Hashtable<String,Object>)gnomes.get("Brastlewark");
                    /*if (gnomes != null){
                        //Log.i("Test", gnomes.get());
                        ArrayList<Gnome> gnomeArrayList = new ArrayList<Gnome>();

                        for (int i=0; i<= gnomes.size()-1; i++){
                            Hashtable<String,Object> gnome = new Hashtable<String, Object>();
                            gnome = (Hashtable<String, Object>) gnomes.get(String.valueOf(i));

                        }
                    }*/


                    dialogLoading.dismiss();
                    }
                //finish();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("gnomes", gnomes);
                    Fragment fragment = new GnomesFragment();
                    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    fragment.setArguments(bundle);
                    ft.replace(R.id.fl_main_fragment_container, fragment, "GnomesFragment");
                    ft.addToBackStack(null);
                    ft.commit();
            }
        });

    }

    @Override
    public void onFragmentTransition(FragmentTag fTag) {
        currentFragmentDisplayed = fTag;

        switch(currentFragmentDisplayed) {

            case GNOMES:
                iv_main_content_header.setImageDrawable(getResources().getDrawable(R.drawable.buildtown));
                tv_main_content_header_title.setText("Town");
                tv_main_content_header_name.setText("Brastlewark");

                break;
            case GNOME_DETAIL:
                iv_main_content_header.setImageDrawable(null);
                break;
        }
    }

    @Override
    public void changeMainContentHeader(String title, String name) {
        tv_main_content_header_title.setText(title);
        tv_main_content_header_name.setText(name);
    }

    @Override
    public void changeMainContentPicture(String url) {
        Log.i("MainActivity", "picture url: " + url);
        new DownloadImageTask(iv_main_content_header).execute(url);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            return;
        }
        super.onBackPressed();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_exit:
                dialogLogout.show();
                dialogLogout.setCustomTitle("Exit Brastlewark");
                dialogLogout.setTextBody("Are you sure you want to exit the town?");
                dialogLogout.setAcceptButtonText("Yes");
                dialogLogout.setCancelButtonText("Cancel");
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
