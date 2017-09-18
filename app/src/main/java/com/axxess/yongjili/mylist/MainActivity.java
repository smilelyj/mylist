package com.axxess.yongjili.mylist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.axxess.yongjili.mylist.fragments.HomeFragment;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;

/**
 * This Activity will work as the holder activity for the fragments of this app.
 */
public class MainActivity extends AppCompatActivity {

    // TAG, please uncomment when needed
    //private static final String TAG = "MainActivity";
    /**
     * This element contains the current fragment being displayed.
     */
    private Fragment mContent;

    /**
     * A reference to the toolbar in the activity.
     */
    private Toolbar toolbar;

    /**
     * This text view is the text view that displays the title of the toolbar.
     */
    private TextView toolbarTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize stetho
        // TODO : Do this only in debug, remove for production
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        // Initialize realm
        Realm.init(this);

        mContent = new HomeFragment();
        Bundle args = new Bundle();
        mContent.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, mContent)
                .commit();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(toolbar);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP ){

            Window window = getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

//        toolbar = (Toolbar) findViewById(R.id.home_toolbar);
//        toolbarTitleTextView = (TextView) toolbar.findViewById(R.id.text_toolbar_title);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                popFragmentFromStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Helper function in charge of enabling the up button for certain fragments.
     */
    public void enableHomeAsUp(){
        // Get a support ActionBar corresponding to this toolbar
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Helper function that disables the up button for certain fragments.
     */
    public void disableHomeAsUp(){
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        try {
            ab.setDisplayHomeAsUpEnabled(false);
        } catch (NullPointerException npe){
            Log.e("disableHomeAsUp", npe.toString());
        }
    }

    /**
     * This helper function is in charge of adding a fragment to the fragment stack, changing
     * the current content to that fragment.
     * @param fragment The fragment to be added to the stack.
     */
    public void addFragmentToStack(Fragment fragment){
        mContent = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(null) // add to the back of the stack
                .commit();
    }

    /**
     * Method that removes a fragment from the stack.
     */
    public void popFragmentFromStack() {
        getSupportFragmentManager()
                .popBackStackImmediate();
        // update mContent with the fragment currently at the frame
        mContent = getSupportFragmentManager().findFragmentById(R.id.frame);
    }

    /**
     * Sets a the specified text on the toolbar.
     * @param title The title to be displayed on the toolbar.
     */
    public void setToolbarTitle(String title){
        toolbarTitleTextView.setText(title);
    }

    /**
     * This helper method changes the left corner icon to a back button. When clicked, it
     * will go back on the navigation process.
     */
    public void setBackButton(){
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_white);
    }

}

