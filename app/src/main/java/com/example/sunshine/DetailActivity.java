package com.example.sunshine;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {

//    ShareActionProvider
    private ShareActionProvider mShareActionProvider;


    //    private static Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        intent=getIntent();
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);

//        // Get the menu item.
//        MenuItem menuItem = menu.findItem(R.id.menu_item_share);
//        // Get the provider and hold onto it to set/change the share intent.
//        mShareActionProvider = (ShareActionProvider) menuItem.getActionProvider();
//        // Set history different from the default before getting the action
//        // view since a call to MenuItem.getActionView() calls
//        // onCreateActionView() which uses the backing file name. Omit this
//        // line if using the default share history file is desired.
//        mShareActionProvider.setShareHistoryFileName("custom_share_history.xml");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        private static final String LOG_TAG=DetailFragment.class.getSimpleName();
        private static final String FORECAST_SHARE_HASHTAG=" #ss App";
        private String mForecastStr;


        public DetailFragment() {
            setHasOptionsMenu(true);
        }

//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            int id=item.getItemId();
//            if(id==R.id.menu_item_share){
//
//            }
//            return super.onOptionsItemSelected(item);
//        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.detailfragment,menu);
            MenuItem menuItem=menu.findItem(R.id.menu_item_share);

            ShareActionProvider shareActionProvider=(ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
            if(shareActionProvider!=null){
                shareActionProvider.setShareIntent(createShareForecastIntent());
            }else{
                Log.d(LOG_TAG,"null?");
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            mForecastStr=getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
            String weatherDetails=mForecastStr;

            String day=weatherDetails.substring(0,weatherDetails.indexOf(' '));
            String date=weatherDetails.substring(weatherDetails.indexOf(' ')+1,weatherDetails.indexOf('-'));
            String weather=weatherDetails.substring(weatherDetails.indexOf('-')+1,weatherDetails.lastIndexOf('-'));
            String max=weatherDetails.substring(weatherDetails.lastIndexOf('-')+1,weatherDetails.indexOf('/'));
            String min=weatherDetails.substring(weatherDetails.indexOf('/')+1);

            ((TextView)rootView.findViewById(R.id.textViewWhen)).setText(day);
            ((TextView)rootView.findViewById(R.id.textViewDate)).setText(date);
            ((TextView)rootView.findViewById(R.id.textViewWeather)).setText(weather);
            ((TextView)rootView.findViewById(R.id.textViewMaxTemp)).setText(max);
            ((TextView)rootView.findViewById(R.id.textViewMinTemp)).setText(min);

            return rootView;
        }
        private Intent createShareForecastIntent(){
            Intent shareIntent=new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,mForecastStr+FORECAST_SHARE_HASHTAG);
            return shareIntent;
        }
    }
//    public void doShare(Intent shareIntent) {
//        // When you want to share set the share intent.
//        if(mShareActionProvider!=null) {
//            mShareActionProvider.setShareIntent(shareIntent);
//        }
//    }

}
