package com.example.sunshine;

//import android.content.CursorLoader;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sunshine.data.WeatherContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

/**
 * Created by nagendra on 3/17/15.
 */
public class ForecastFragment extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor> {

    public static final int FORECAST_LOADER=0;
    private ForecastAdapter mForecastAdapter;

    String[] det;
    ListView listViewWeather;
//    private ForecastAdapter mForecastAdapter;

    public ForecastFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_refresh){
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateWeather() {
//        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location=Utility.getPreferredLocation(getActivity());
        new FetchWeatherTask(getActivity()).execute(location);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        String locationSetting = Utility.getPreferredLocation(getActivity());
//
//        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
//        Uri weatherForLocationUri =WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting, System.currentTimeMillis());
//        Cursor cur =getActivity().getContentResolver().query(weatherForLocationUri,null, null, null, sortOrder);
//        mForecastAdapter = new ForecastAdapter(getActivity(), cur, 0);

        mForecastAdapter=new ForecastAdapter(getActivity(),null,0);

                View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        String [] stringsWeatherData=new String[]{
//                "Today",
//                "Tomorrow",
//                "Tue",
//                "Wed",
//                "Thu",
//                "Fri",
//                "Sat",
//        };
//        ArrayList<String> arrayListWeatherData=new ArrayList<>(Arrays.asList(stringsWeatherData));
//        ArrayList<String> arrayListWeatherData=new ArrayList<>();

//        ArrayAdapter arrayAdapterWeather=new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textview,arrayListWeatherData);
        listViewWeather=(ListView)rootView.findViewById(R.id.listview_forecast);
//        listViewWeather.setAdapter(arrayAdapterWeather);

//        listViewWeather.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),((TextView)view).getText().toString(),Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(getActivity(),DetailActivity.class);
//                intent.putExtra(Intent.EXTRA_TEXT,((TextView)view).getText().toString());
//                startActivity(intent);
//            }
//        });




        return rootView;
    }

//    public class FetchWeatherTask extends AsyncTask<String,Void,String[]>{
//
//        private final String LOG_TAG=FetchWeatherTask.class.getSimpleName();
//
//        @Override
//        protected String[] doInBackground(String... params) {
//            if(params.length==0){
//                return null;
//            }
//            Log.d("MY zip code",params[0]);
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//
//// Will contain the raw JSON response as a string.
//            String forecastJsonStr = null;
//            String[] result=null;
//            try {
//
//// Construct the URL for the OpenWeatherMap query
//// Possible parameters are avaiable at OWM's forecast API page, at
//// http://openweathermap.org/API#forecast
//
//                String format="json";
//                String units="metric";
//                String days="7";
//
//                final String FORECAST_BASE_URL="http://api.openweathermap.org/data/2.5/forecast/daily?";
//                final String QUERY_PARAM="q";
//                final String FORMAT_PARAM="mode";
//                final String UNITS_PARAM="units";
//                final String DAYS_PARAM="cnt";
//
//                Uri builtUri=Uri.parse(FORECAST_BASE_URL).buildUpon().appendQueryParameter(QUERY_PARAM,params[0]).appendQueryParameter(FORMAT_PARAM,format).appendQueryParameter(UNITS_PARAM,units).appendQueryParameter(DAYS_PARAM,days).build();
//                URL url = new URL(builtUri.toString());
////                url=new URL("http://192.168.43.80/");
//
//                Log.d(LOG_TAG,builtUri.toString());
//// Create the request to OpenWeatherMap, and open the connection
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("POST");
//
//                Log.d(LOG_TAG,urlConnection.getURL().toString());
//                urlConnection.connect();
//
//// Read the input stream into a String
//
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//                if (inputStream == null) {
//// Nothing to do.
//                    return null;
//                }
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//// Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//// But it does make debugging a *lot* easier if you print out the completed
//// buffer for debugging.
//                    buffer.append(line + "\n");
//                }
//
//                if (buffer.length() == 0) {
//// Stream was empty. No point in parsing.
//                    return null;
//                }
//                forecastJsonStr = buffer.toString();
//
//
//                try {
//                     result = getWeatherDataFromJson(forecastJsonStr, 7);
////                    Log.d(LOG_TAG,result[0]);
////                    Log.d(LOG_TAG,result[1]);
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }
////                Log.d(LOG_TAG,forecastJsonStr);
//
//
//            } catch (IOException e) {
//                Log.e(LOG_TAG, "Error ", e);
//// If the code didn't successfully get the weather data, there's no point in attemping
//// to parse it.
//                return null;
//            } finally{
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e(LOG_TAG, "Error closing stream", e);
//                    }
//                }
//            }
//
//            return result;
//
//        }
//
//        @Override
//        protected void onPostExecute(String[] strings) {
////            ArrayList<String> arrayListWeatherData=new ArrayList<>(Arrays.asList(strings));
//            ArrayList<String> arrayListWeatherData=new ArrayList<>();
////            arrayAdapterWeather=new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textview,arrayListWeatherData);
//            arrayAdapterWeather.clear();
//            int count=strings.length;
//            for(int i=0;i<count;i++) {
//                arrayAdapterWeather.add(strings[i]);
//            }
//        }
//    }
//    public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex)
//            throws JSONException {
//        // TODO: add parsing code here
//        JSONArray jsonArray=new JSONArray(weatherJsonStr);
//        JSONObject jsonObject=jsonArray.getJSONObject(0);
//        return Double.parseDouble(jsonObject.get("max").toString());
////        return -1;
//    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    /* The date/time conversion code is going to be moved outside the asynctask later,
    * so for convenience we're breaking it out into its own method now.
    */
    private String getReadableDateString(long time){
// Because the API returns a unix timestamp (measured in seconds),
// it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
// For presentation, assume the user doesn't care about tenths of a degree.

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String units=sharedPreferences.getString(getResources().getString(R.string.pref_units_key),getResources().getString(R.string.pref_units_metric));

        if(units.equals(getResources().getString(R.string.pref_units_imperial))){
            high=high*1.8+32;
            low=low*1.8+32;
        }else if (units.equals(getResources().getString(R.string.pref_units_metric))){
            Log.d("MY units","imperial");
        }

        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy: constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {

// These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DESCRIPTION = "main";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

// OWM returns daily forecasts based upon the local time of the city that is being
// asked for, which means that we need to know the GMT offset to translate this data
// properly.

// Since this data is also sent in-order and the first day is always the
// current day, we're going to take advantage of that to get a nice
// normalized UTC date for all of our weather.

        Time dayTime = new Time();
        dayTime.setToNow();

// we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

// now we work exclusively in UTC
        dayTime = new Time();

        String[] resultStrs = new String[numDays];
        for(int i = 0; i < weatherArray.length(); i++) {
// For now, using the format "Day, description, hi/low"
            String day;
            String description;
            String highAndLow;

// Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

// The date/time is returned as a long. We need to convert that
// into something human-readable, since most people won't read "1400356800" as
// "this saturday".
            long dateTime;
// Cheating to convert this to UTC time, which is what we want anyhow
            dateTime = dayTime.setJulianDay(julianStartDay+i);
            day = getReadableDateString(dateTime);

// description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

// Temperatures are in a child object called "temp". Try not to name variables
// "temp" when working with temperature. It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);

            highAndLow = formatHighLows(high, low);
            resultStrs[i] = day + " - " + description + " - " + highAndLow;
        }

        for (String s : resultStrs) {
//            Log.v("FF", "Forecast entry: " + s);
        }
        return resultStrs;

    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        String locationSetting=Utility.getPreferredLocation(getActivity());

        String sortOrder= WeatherContract.WeatherEntry.COLUMN_DATE+ " ASC";
        Uri weatherForLocationUri= WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting,System.currentTimeMillis());

        return new CursorLoader(getActivity(),
                weatherForLocationUri,
                null,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mForecastAdapter.swapCursor(data);
    }



    @Override
    public void onLoaderReset(Loader loader) {
        mForecastAdapter.swapCursor(null);
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        //    private ArrayAdapter<String> mForecastAdapter;
        private final Context mContext;

        public FetchWeatherTask(Context context/*, ArrayAdapter<String> forecastAdapter*/) {
            mContext = context;
//        mForecastAdapter = forecastAdapter;
        }

        private boolean DEBUG = true;
//
//    /* The date/time conversion code is going to be moved outside the asynctask later,
//     * so for convenience we're breaking it out into its own method now.
//     */
//    private String getReadableDateString(long time){
//        // Because the API returns a unix timestamp (measured in seconds),
//        // it must be converted to milliseconds in order to be converted to valid date.
//        Date date = new Date(time);
//        SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
//        return format.format(date).toString();
//    }
//
//    /**
//     * Prepare the weather high/lows for presentation.
//     */
//    private String formatHighLows(double high, double low) {
//        // Data is fetched in Celsius by default.
//        // If user prefers to see in Fahrenheit, convert the values here.
//        // We do this rather than fetching in Fahrenheit so that the user can
//        // change this option without us having to re-fetch the data once
//        // we start storing the values in a database.
//        SharedPreferences sharedPrefs =
//                PreferenceManager.getDefaultSharedPreferences(mContext);
//        String unitType = sharedPrefs.getString(
//                mContext.getString(R.string.pref_units_key),
//                mContext.getString(R.string.pref_units_metric));
//
//        if (unitType.equals(mContext.getString(R.string.pref_units_imperial))) {
//            high = (high * 1.8) + 32;
//            low = (low * 1.8) + 32;
//        } else if (!unitType.equals(mContext.getString(R.string.pref_units_metric))) {
//            Log.d(LOG_TAG, "Unit type not found: " + unitType);
//        }
//
//        // For presentation, assume the user doesn't care about tenths of a degree.
//        long roundedHigh = Math.round(high);
//        long roundedLow = Math.round(low);
//
//        String highLowStr = roundedHigh + "/" + roundedLow;
//        return highLowStr;
//    }

        /**
         * Helper method to handle insertion of a new location in the weather database.
         *
         * @param locationSetting The location string used to request updates from the server.
         * @param cityName A human-readable city name, e.g "Mountain View"
         * @param lat the latitude of the city
         * @param lon the longitude of the city
         * @return the row ID of the added location.
         */
        long addLocation(String locationSetting, String cityName, double lat, double lon) {
            Log.d("dododo","sss");
            // Students: First, check if the location with this city name exists in the db
            Cursor locationCursor=mContext.getContentResolver().query(
                    WeatherContract.LocationEntry.CONTENT_URI,
                    new String[]{WeatherContract.LocationEntry._ID},
                    WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING+" = ?",
                    new String[]{locationSetting},
                    null
            );
            if (0==0){
                Log.d("dododo zero","equals zero");
            }
            // If it exists, return the current ID
            if (locationCursor.moveToFirst()){
                long id=locationCursor.getLong(0);
                locationCursor.close();
                Log.d("dododo zero","maybe");
                if (id==-1){
                    Log.d("dododo zero","equals minus 1");
                }
                return id;
            }
            // Otherwise, insert it using the content resolver and the base URI
            else {
                ContentValues contentValues=new ContentValues();

                contentValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME,cityName);
                contentValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT,lat);
                contentValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG,lon);
                contentValues.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,locationSetting);

                Uri uri=mContext.getContentResolver().insert(WeatherContract.LocationEntry.CONTENT_URI, contentValues);
                Log.d("dododo",uri.toString()+" means "+uri.getLastPathSegment());
                return Integer.parseInt(uri.getLastPathSegment());

            }
//        return -1;
        }

    /*
        Students: This code will allow the FetchWeatherTask to continue to return the strings that
        the UX expects so that we can continue to test the application even once we begin using
        the database.
     */
//    String[] convertContentValuesToUXFormat(Vector<ContentValues> cvv) {
//        // return strings to keep UI functional for now
//        String[] resultStrs = new String[cvv.size()];
//        for ( int i = 0; i < cvv.size(); i++ ) {
//            ContentValues weatherValues = cvv.elementAt(i);
//            String highAndLow = formatHighLows(
//                    weatherValues.getAsDouble(WeatherEntry.COLUMN_MAX_TEMP),
//                    weatherValues.getAsDouble(WeatherEntry.COLUMN_MIN_TEMP));
//            resultStrs[i] = getReadableDateString(
//                    weatherValues.getAsLong(WeatherEntry.COLUMN_DATE)) +
//                    " - " + weatherValues.getAsString(WeatherEntry.COLUMN_SHORT_DESC) +
//                    " - " + highAndLow;
//        }
//        return resultStrs;
//    }

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private String[] getWeatherDataFromJson(String forecastJsonStr,
                                            String locationSetting)
                throws JSONException {
            String []ans=null;

            // Now we have a String representing the complete forecast in JSON Format.
            // Fortunately parsing is easy:  constructor takes the JSON string and converts it
            // into an Object hierarchy for us.

            // These are the names of the JSON objects that need to be extracted.

            // Location information
            final String OWM_CITY = "city";
            final String OWM_CITY_NAME = "name";
            final String OWM_COORD = "coord";

            // Location coordinate
            final String OWM_LATITUDE = "lat";
            final String OWM_LONGITUDE = "lon";

            // Weather information.  Each day's forecast info is an element of the "list" array.
            final String OWM_LIST = "list";

            final String OWM_PRESSURE = "pressure";
            final String OWM_HUMIDITY = "humidity";
            final String OWM_WINDSPEED = "speed";
            final String OWM_WIND_DIRECTION = "deg";

            // All temperatures are children of the "temp" object.
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";

            final String OWM_WEATHER = "weather";
            final String OWM_DESCRIPTION = "main";
            final String OWM_WEATHER_ID = "id";


            try {
                JSONObject forecastJson = new JSONObject(forecastJsonStr);
                JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

                JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);
                String cityName = cityJson.getString(OWM_CITY_NAME);

                JSONObject cityCoord = cityJson.getJSONObject(OWM_COORD);
                double cityLatitude = cityCoord.getDouble(OWM_LATITUDE);
                double cityLongitude = cityCoord.getDouble(OWM_LONGITUDE);

                long locationId = addLocation(locationSetting, cityName, cityLatitude, cityLongitude);

                // Insert the new weather information into the database
                Vector<ContentValues> cVVector = new Vector<ContentValues>(weatherArray.length());


                // OWM returns daily forecasts based upon the local time of the city that is being
                // asked for, which means that we need to know the GMT offset to translate this data
                // properly.

                // Since this data is also sent in-order and the first day is always the
                // current day, we're going to take advantage of that to get a nice
                // normalized UTC date for all of our weather.

                Time dayTime = new Time();
                dayTime.setToNow();

                // we start at the day returned by local time. Otherwise this is a mess.
                int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

                // now we work exclusively in UTC
                dayTime = new Time();

                ans=new String[weatherArray.length()];
                det=new String[weatherArray.length()];

                Calendar cal=Calendar.getInstance();

                for(int i = 0; i < weatherArray.length(); i++) {
                    // These are the values that will be collected.
                    long dateTime;
                    double pressure;
                    int humidity;
                    double windSpeed;
                    double windDirection;

                    double high;
                    double low;

                    String description;
                    int weatherId;

                    // Get the JSON object representing the day
                    JSONObject dayForecast = weatherArray.getJSONObject(i);

                    // Cheating to convert this to UTC time, which is what we want anyhow
                    dateTime = dayTime.setJulianDay(julianStartDay+i);

                    pressure = dayForecast.getDouble(OWM_PRESSURE);
                    humidity = dayForecast.getInt(OWM_HUMIDITY);
                    windSpeed = dayForecast.getDouble(OWM_WINDSPEED);
                    windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION);

                    // Description is in a child array called "weather", which is 1 element long.
                    // That element also contains a weather code.
                    JSONObject weatherObject =
                            dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                    description = weatherObject.getString(OWM_DESCRIPTION);
                    weatherId = weatherObject.getInt(OWM_WEATHER_ID);

                    // Temperatures are in a child object called "temp".  Try not to name variables
                    // "temp" when working with temperature.  It confuses everybody.
                    JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                    high = temperatureObject.getDouble(OWM_MAX);
                    low = temperatureObject.getDouble(OWM_MIN);

                    ContentValues weatherValues = new ContentValues();

                    weatherValues.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, locationId);
                    weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, dateTime);
                    weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, humidity);
                    weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, pressure);
                    weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
                    weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, windDirection);
                    weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, high);
                    weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, low);
                    weatherValues.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, description);
                    weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weatherId);

                    if(i>0)
                    cal.add(cal.DATE,1);
                    int num=cal.get(Calendar.DAY_OF_WEEK)+i;

                    det[i]= ""+Utility.getDay(num)+" "+cal.get(Calendar.DATE)+" - "+description+" - "+high+" / "+low;
//                    if (num>7){
//                        num=num%7+1;
//                    }
                    ans[i]=""+Utility.getDay(num)+" Min "+low+" Max "+high;
//                    ans[i]=""+locationId+humidity+pressure+windSpeed+high+low+" "+dateTime;

                    cVVector.add(weatherValues);
                }

                int inserted=0;

                // add to database
                if ( cVVector.size() > 0 ) {
                    // Student: call bulkInsert to add the weatherEntries to the database here
                    ContentValues[] contentValueses=new ContentValues[cVVector.size()];
                    cVVector.toArray(contentValueses);
//                mContext.getContentResolver().bulkInsert(WeatherEntry.CONTENT_URI,contentValueses);
                    inserted=mContext.getContentResolver().bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI,contentValueses);
//            }

//
//            // Sort order:  Ascending, by date.
//            String sortOrder = WeatherEntry.COLUMN_DATE + " ASC";
//            Uri weatherForLocationUri = WeatherEntry.buildWeatherLocationWithStartDate(
//                    locationSetting, System.currentTimeMillis());
//
//
//
//
//            // Students: Uncomment the next lines to display what what you stored in the bulkInsert
//
//            Cursor cur = mContext.getContentResolver().query(weatherForLocationUri,
//                    null, null, null, sortOrder);
//
//            cVVector = new Vector<ContentValues>(cur.getCount());
//            if ( cur.moveToFirst() ) {
//                do {
//                    ContentValues cv = new ContentValues();
//                    DatabaseUtils.cursorRowToContentValues(cur, cv);
//                    cVVector.add(cv);
//                } while (cur.moveToNext());

                }

                Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " Inserted");


//            String[] resultStrs = convertContentValuesToUXFormat(cVVector);
//            return resultStrs;

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return ans;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null && mForecastAdapter != null) {
//                mForecastAdapter.clear();

                ArrayList<String> arrayListWeatherData=new ArrayList<>();


                for(String dayForecastStr : result) {
                    arrayListWeatherData.add(dayForecastStr);
//                    mForecastAdapter.add(dayForecastStr);
                    Log.d("hh",dayForecastStr);
                }
                ArrayAdapter arrayAdapterWeather=new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textview,arrayListWeatherData);

                listViewWeather.setAdapter(arrayAdapterWeather);
                listViewWeather.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent(getActivity(),DetailActivity.class);
                        intent.putExtra(Intent.EXTRA_TEXT,det[position]);
                        startActivity(intent);

                    }
                });

                // New data is back from the server.  Hooray!
            }
        }

        @Override
        protected String[] doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }
            String locationQuery = params[0];

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 14;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String FORECAST_BASE_URL =
                        "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                getWeatherDataFromJson(forecastJsonStr, locationQuery);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }
            catch (JSONException e){
                e.printStackTrace();
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

        try {
            return getWeatherDataFromJson(forecastJsonStr, locationQuery);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }


    }
}