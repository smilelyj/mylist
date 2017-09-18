package com.axxess.yongjili.mylist.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.axxess.yongjili.mylist.models.Drawing;
import com.axxess.yongjili.mylist.R;
import com.axxess.yongjili.mylist.MainActivity;


import butterknife.BindColor;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by YongjiLi on 2/2/17.
 */
/**
 * This fragment allows you to view a {@link com.axxess.yongjili.mylist.models.Drawing}
 */
public class ItemViewFragment extends Fragment {

    public static final String TAG = "ItemViewFragment";
    //    private Realm realm;
    private Drawing result;
    private MainActivity mainActivity;

    @Bind(R.id.id_text)
    TextView resultId;
    @Bind(R.id.type_text)
    TextView resultType;
    @Bind(R.id.date_text)
    TextView resultDate;
    @Bind(R.id.data_text)
    TextView resultData;

    @BindColor(R.color.white) int white;

    public ItemViewFragment(){
    }

    public ItemViewFragment(Drawing result) {
        this.result = result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View main_view = inflater.inflate(R.layout.fragment_view_result, container, false);
        ButterKnife.bind(this, main_view);
        return main_view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpToolbar();
        setupViews();
    }

    @Override
    public void onPause() {
        super.onPause();
//        realm.close();
    }

    /**
     * This helper method sets up the toolbar.
     */
    private void setUpToolbar(){
//        mainActivity.setToolbarTitle(result.getId());
        mainActivity.setBackButton();
    }

    /**
     * This helper method set ups the view with the necessary information about the selected
     * result.
     */
    private void setupViews(){
        resultId.setText(result.getId());
        resultType.setText(result.getType());
        resultDate.setText(result.getDate());
        resultData.setText(result.getData());

    }

}

