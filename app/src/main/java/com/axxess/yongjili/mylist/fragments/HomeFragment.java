package com.axxess.yongjili.mylist.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.axxess.yongjili.mylist.utils.RecyclerItemClickListener;

import butterknife.Bind;
import butterknife.OnItemClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import com.axxess.yongjili.mylist.R;
import com.axxess.yongjili.mylist.MainActivity;
import com.axxess.yongjili.mylist.interactions.FetchDrawings;
import com.axxess.yongjili.mylist.utils.NetworkUtils;
import com.axxess.yongjili.mylist.models.Drawing;
import com.axxess.yongjili.mylist.adapters.MyListAdapter;
import com.axxess.yongjili.mylist.customViews.CustomListElementGenerator;

import com.bumptech.glide.Glide;

import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

/**
 * Created by yongjili on 9/15/17.
 */

/**
 *
 * This class is in charge of showing a list of search results.
 */
public class HomeFragment extends Fragment {

    // TAG, uncomment/comment when needed
    private static final String TAG = "HomeFragment";

    /**
     * I use two ways to generate the user interface: by Customized View or by RecyclerView.
     */

    // User interface elements
//    @Bind(R.id.main_list)
    RecyclerView recyclerView;

//    @Bind(R.id.main_list_alternative)
    LinearLayout customizedListView;

    /**
     * A reference to the holder activity
     */
    private MainActivity mainActivity;

    /**
     * the results of searching the specific drawings on the database.
     */
    private RealmResults<Drawing> results;

    /**
     * A reference to the realm
     */
    private Realm realm;

    /**
     * Generator of views for the index.
     */
    private CustomListElementGenerator viewGenerator;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mainActivity.disableHomeAsUp();
        viewGenerator = new CustomListElementGenerator(mainActivity);

        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.main_list);
        customizedListView = (LinearLayout) rootView.findViewById(R.id.main_list_alternative);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity.disableHomeAsUp();

        boolean internetConnection =  NetworkUtils.getConnectivityStatus(mainActivity)
                != NetworkUtils.TYPE_NOT_CONNECTED;
        if(internetConnection){
            results = realm.where(Drawing.class).findAll();
            boolean infoOnDatabase = (results.size() != 0);

            //If realm has the data, no need to query the server.
            if (infoOnDatabase){
                postQueryData();
            } else {
                new GetHeadsAsync(getContext(), true ).execute();
                //new CreateDrawingByCodeAsync(getContext()).execute();
            }
        }
        else Log.d(TAG, "queryData: no internet connection, don't run async task");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    /**
     * This click listener is ran when an element on the grid of elements is clicked,
     * it opens the product view for that certain element.
     * @param position The position of the selected item.
     */
    @OnItemClick(R.id.main_list)
    public void listItemClick(int position){
        Log.e("yongji", String.valueOf(position));
        Drawing result = results.get(position);
        ItemViewFragment fragment = new ItemViewFragment(result);
        mainActivity.addFragmentToStack(fragment);
    }

    /**
     * This helper method is ran whenever the search returned no results.
     */
    private void noSearchResultsHelper(){
        mainActivity.setTitle("No Search Results");
//        noMatchesFoundTv.setVisibility(View.VISIBLE);
    }

    /**
     * This method will be ran after the whole data has been queried form the database, this method
     * will set up the grid with the data returned form the search.
     */
    private void postQueryData(){
        try {
            MyListAdapter adapter = new MyListAdapter(getContext(), results);
            recyclerView.setAdapter(adapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {


                            // do whatever

                            if (results.get(position).getType().equals("text")){
                                ItemViewFragment fragment = new ItemViewFragment(results.get(position));
                                mainActivity.addFragmentToStack(fragment);
                            } else if (results.get(position).getType().equals("image")){
                                Dialog dialog = new Dialog(getContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                                dialog.setContentView(R.layout.dialog_press_image);

                                ImageView imageView = (ImageView) dialog.findViewById(R.id.container);

                                Glide.with(getContext())
                                        .load(results.get(position).getData())
                                        .placeholder(R.drawable.placeholder)
                                        .thumbnail(0.2f)
                                        .into(imageView);

                                dialog.show();

                                //Grab the window of the dialog, and change the width
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                Window window = dialog.getWindow();
                                lp.copyFrom(window.getAttributes());
                                //This makes the dialog take up the full width
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                window.setAttributes(lp);
                            }

                        }

                        @Override public void onLongItemClick(View view, int position) {
                            // do whatever
                            Log.e("YongjiLongClick", results.get(position).getData());
                        }
                    })
            );

            if(results.size() == 0) noSearchResultsHelper();
        } catch (NullPointerException npe){
            Log.e(TAG, npe.toString());
        }
    }

    /**
     * This async task is in charge of downloading the information from the server. If there is
     * already some information on the database, the info will be updated.
     */
    private class GetHeadsAsync extends AsyncTask<Void, Void, Void> {
        private Context context;
        private boolean showDialog;
        private ProgressDialog dialog;

        public GetHeadsAsync(Context context, boolean showDialog){
            this.context = context;
            this.showDialog = showDialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(showDialog){
                dialog = ProgressDialog.show(context, "", "Loading Information, Please wait...", true);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(showDialog){
                dialog.dismiss();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 1500ms
                        postQueryData();
                    }
                }, 1500);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            FetchDrawings.getDrawing();
            return null;
        }
    }

    private class CreateDrawingByCodeAsync extends AsyncTask<Void, Void, Void> {

        private Context context;

        CreateDrawingByCodeAsync(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 1500ms
                    Realm realm = Realm.getDefaultInstance();

                    RealmResults<Drawing> drawings = realm.where(Drawing.class).findAll();

                    for(int i = 0; i < drawings.size(); i++){
                        customizedListView.addView(
                                viewGenerator.generateAppointMentListItem(mainActivity, drawings.get(i), 0));
                    }
                }
            }, 1500);
        }

        @Override
        protected Void doInBackground(Void... params) {
            FetchDrawings.getDrawing();
            return null;
        }
    }
}
