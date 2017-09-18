package com.axxess.yongjili.mylist.interactions;

import android.util.Log;

import com.axxess.yongjili.mylist.models.Drawing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yongjili on 9/16/17.
 */

/**
 * This helper class is in charge of retrieving all the information.
 * This class has public modifier so that every class can access it and use it's methods.
 */

public class FetchDrawings {

    // TAG for debugging, comment/uncomment when necessary
    private static final String TAG = "FetchDrawings";

    /**
     * Helper function that hit given url to get information.
     */

    public static void getDrawing () {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url("https://raw.githubusercontent.com/AxxessTech/Mobile-Projects/master/challenge.json")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.e(TAG, response.toString());

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);

                } else {
                    // do something with the result
                    //It's an error to call ResponseBody.string() multiple times. The documentation
                    // explains why.
//                        Log.e("isSuccessful", response.body().string().toString());

                    try {
                        Realm realm = Realm.getDefaultInstance();

                        JSONArray jsonArray = new JSONArray(response.body().string());
                        JSONObject singleJSONObject;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                singleJSONObject = (JSONObject) jsonArray.get(i);

                                Object obj = singleJSONObject.get("id");

                                if(obj instanceof Integer | obj instanceof Long){
                                    realm.beginTransaction();
                                    Drawing drawing = realm.createOrUpdateObjectFromJson(Drawing.class, singleJSONObject);
                                    Log.e("SaveServerObject:", drawing.toString());

                                    realm.commitTransaction();
                                } else if (obj instanceof String){
                                    realm.beginTransaction();
                                    Drawing drawing = realm.createOrUpdateObjectFromJson(Drawing.class, singleJSONObject);

                                    Log.e("SaveServerObject:", drawing.toString());
                                    realm.commitTransaction();
                                }

                            } catch (JSONException | NullPointerException ex) {
                                Log.e("Exception", "execute: " + ex.toString() );
                            }
                        }
                        realm.close();

                    } catch (JSONException | IOException | NullPointerException ex){
                        Log.e("Exception", "execute: " + ex.toString() );
                    }
                }
            }
        });
    }

}
