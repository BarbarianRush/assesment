package com.example.assesment.connection;

import java.util.Hashtable;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
* Custom JSON Parser class used for mapping JSON String into Hashtable<String,Object>.
*
* @author  dcastrillo
* @version 1.0
* @since   2015-10-02
*/

public class JsonParser {
	
	public JsonParser(){
		
	}

	public Hashtable<String,Object> retrieveJson(String jsonObjectString){
		Hashtable<String,Object> mLocalHashtable = new Hashtable<String, Object>();
		JSONObject jObject = null;
        JSONArray jsonArray = null;
		
		try {
			jObject = new JSONObject(jsonObjectString);
			
			@SuppressWarnings("unchecked")
			Iterator<String> jObjectKeyIterator = jObject.keys();
			
			for(int i=0; i<jObject.length(); i++){
				String key = jObjectKeyIterator.next();
				Object mObj = jObject.get(key);
				
				if(mObj instanceof JSONArray){
					mLocalHashtable.put(key,convertJSONArrayToHash((JSONArray)mObj));
				}else if(mObj instanceof String) {
                    mLocalHashtable.put(key, String.valueOf(mObj));
				}else{
					//Log.w("JsonParser","Objeto desconocido en key: " + key);
                    mLocalHashtable.put(key, String.valueOf(mObj));
					continue;
				}
			}
		}catch (JSONException e) {

            try {
                jsonArray = new JSONArray(jsonObjectString);
                return convertJSONArrayToHash(jsonArray);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return null;
		}

		return mLocalHashtable;
	}
	
	public Hashtable<String, Object> convertJSONArrayToHash(JSONArray mJsonArray){
		Hashtable<String, Object> mHashtable = new Hashtable<String, Object>();
		for (int i=0; i<mJsonArray.length(); i++){
			Object mObj;
			
			try {
				mObj = mJsonArray.get(i);
				
				if (mObj instanceof String){
					mHashtable.put(String.valueOf(i),String.valueOf(mObj));
				}else if(mObj instanceof JSONObject){
					final String mJSONObjectStringified = ((JSONObject)mObj).toString();
					mHashtable.put(String.valueOf(i),retrieveJson(mJSONObjectStringified));
				}else{
					//Log.w("JsonParser","Objeto de JSONArray desconocido en key: " + i);
                    mHashtable.put(String.valueOf(i),String.valueOf(mObj));
					continue;
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
                return null;
			}
			
			
		}
		
		return mHashtable;
	}
}
