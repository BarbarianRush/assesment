package com.example.assesment.connection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.belladati.httpclientandroidlib.HttpResponse;
import com.belladati.httpclientandroidlib.NameValuePair;
import com.belladati.httpclientandroidlib.client.ClientProtocolException;
import com.belladati.httpclientandroidlib.client.HttpClient;
import com.belladati.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
import com.belladati.httpclientandroidlib.client.methods.HttpGet;
import com.belladati.httpclientandroidlib.client.methods.HttpPost;
import com.belladati.httpclientandroidlib.impl.client.DefaultHttpClient;
import com.belladati.httpclientandroidlib.message.BasicNameValuePair;
import com.belladati.httpclientandroidlib.params.HttpConnectionParams;
import com.belladati.httpclientandroidlib.params.HttpParams;
import com.example.assesment.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by dcastrillo on 17/10/2014.
 */
public class RestClient {

    public enum RequestType{
        POST,
        GET
    }

    HttpClient httpclient;
    Context context;

    public RestClient(Context context){
        this.context = context;
        httpclient = new DefaultHttpClient();
        HttpParams params = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 20000);
        HttpConnectionParams.setSoTimeout(params, 20000);
    }

    public void findGnomes(PetitionRequestMonitoring petitionRequestMonitoring){
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(petitionRequestMonitoring, RequestType.GET);
        httpAsyncTask.execute(context.getResources().getString(R.string.gnomes_source));
    }

    private String GET(String url){
        InputStream inputStream = null;
        String result = "";
        Log.i("RestClient" , "url: " + url);
        try {

            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
            String headerCode = String.valueOf(httpResponse.getStatusLine().getStatusCode());

            if (headerCode != null){
                String headerCodeF = headerCode.substring(0,1);
                if (headerCodeF.equalsIgnoreCase("5")){
                    Log.e("InputStream", "Error 5xx");
                    return null;
                }
            }

            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
            }else {
                result = "";
            }

        } catch (Exception e) {
            String err = (e.getMessage()==null)?"Error de conexión":e.getMessage();
            Log.d("InputStream", err);
            return null;
        }
        Log.i("RestClient" , "Result: " + result);
        return result;
    }

    private String POST(String url, List<String[]> values){
        HttpPost httpPost = new HttpPost(url);
        InputStream inputStream = null;
        String result = "";
        Log.i("RestClient", "POST url: " + url);

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(values.size());
            for (int i=0; i<=values.size()-1; i++){
                nameValuePairs.add(new BasicNameValuePair(values.get(i)[0], values.get(i)[1]));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();

            String headerCode = String.valueOf(httpResponse.getStatusLine().getStatusCode());

            if (headerCode != null){
                String headerCodeF = headerCode.substring(0,1);
                if (headerCodeF.equalsIgnoreCase("5")){
                    Log.e("InputStream", "Error 5xx");
                    return null;
                }
            }

            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "";

        } catch (ClientProtocolException e) {
            String err = (e.getMessage()==null)?"Error de conexión":e.getMessage();
            Log.d("InputStream", err);
        } catch (IOException e) {
            String err = (e.getMessage()==null)?"Error de conexión":e.getMessage();
            Log.d("InputStream", err);
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        PetitionRequestMonitoring petitionRequestMonitoring;
        RequestType requestType;
        List<String[]> params;

        public HttpAsyncTask(PetitionRequestMonitoring petitionRequestMonitoring, RequestType requestType){
            this.petitionRequestMonitoring = petitionRequestMonitoring;
            this.requestType = requestType;
            params= new ArrayList<String[]>();
        }

        @Override
        protected void onPreExecute() {
            petitionRequestMonitoring.onServiceRequestStarted();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            switch (requestType){
                case GET:
                    return GET(urls[0]);
                case POST:
                    Log.i("RestClient", "RestClient entra en POST con params");
                    return POST(urls[0], params);
                default:
                    return GET(urls[0]);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            petitionRequestMonitoring.onServiceRequestFinished(result);
        }

        public List<String[]> getParams() {
            return params;
        }

        public void setParams(List<String[]> params) {
            this.params = params;
        }
    }


}