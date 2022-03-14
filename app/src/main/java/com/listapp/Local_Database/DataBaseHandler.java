package com.listapp.Local_Database;

/**
 * Created by syscraft on 7/3/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import com.listapp.API_Utility.Ratrofit_Implementation.Model.Splash_Response.StateCityResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivesh on 3/22/2017.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    private SQLiteDatabase db, db1;
    private Context context;
    private String STATE = "stateList";
    private String CITY = "cityList";

    public DataBaseHandler(Context context) {
        super(context, "listApp.db", null, 1);
        this.context = context;
        db = getWritableDatabase();
        db1 = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CITY + "(city TEXT , state TEXT,city_id TEXT PRIMARY KEY,state_id TEXT)");
        db.execSQL("create table " + STATE + "(state TEXT,state_id TEXT PRIMARY KEY)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean insertStateCityData(StateCityResponse stateCityResponse) {
        try {


            db.execSQL("DROP TABLE IF EXISTS " + CITY);
            db.execSQL("DROP TABLE IF EXISTS " + STATE);
            onCreate(db);
            insertState(stateCityResponse);
            ContentValues c = new ContentValues();
            List<StateCityResponse.Datum> stateList = stateCityResponse.getData();
            int length = stateList.size();
            for (int i = 0; i < length; i++) {
                List<StateCityResponse.Datum.City> cityList = stateList.get(i).getCity();
                int cityLength = cityList.size();
                for (int j = 0; j < cityLength; j++) {

                    c.put("city", cityList.get(j).getCityName());
                    c.put("city_id", cityList.get(j).getCityId());
                    c.put("state", stateList.get(i).getStateName());
                    c.put("state_id", stateList.get(i).getStateId());
                    long ii = db.insert(CITY, null, c);
            //        Log.e("<<City Data Inserted>>", i + "_" + ii);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean insertState(StateCityResponse stateCityResponse) {
        try {
            //  db.execSQL("DROP TABLE IF EXISTS "+STATE);
            //   onCreate(db);
            ContentValues c = new ContentValues();
            List<StateCityResponse.Datum> stateList = stateCityResponse.getData();
            int length = stateList.size();
            for (int i = 0; i < length; i++) {
                c.put("state", stateList.get(i).getStateName());
                c.put("state_id", stateList.get(i).getStateId());
                Long ii = db.insert(STATE, null, c);
          //      Log.e("<<State Data Inserted>>", i + "___" + ii);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Nullable
    public List<String> getStateList() {
        try {
            List<String> list = new ArrayList<>();
            //    list.add("Select option");
            Cursor c = db1.rawQuery("select * from " + STATE, null);
       //     Log.e(">>", c.getCount() + "");
            while (c.moveToNext()) {
                String name = c.getString(0);
                list.add(name);
            }
            //  while (c.moveToNext());
            return list;
        } catch (Exception e) {
       //     Log.e(">>", e.getMessage() + "");
            return null;
        }
    }

    @Nullable
    public List<String> getStateList(String statename) {
        try {
            List<String> list = new ArrayList<>();
            //    list.add("Select option");
            Cursor c = db1.rawQuery("select * from " + STATE, null);
      //      Log.e(">>", c.getCount() + "");
            while (c.moveToNext()) {
                String name = c.getString(0);
                if (!name.equals(statename))
                    list.add(name);
            }
            return list;
        } catch (Exception e) {
       //     Log.e(">>", e.getMessage() + "");
            return null;
        }
    }


    @Nullable
    public List<String> getCityList(String stateName) {
        try {
            List<String> list = new ArrayList<>();
            //      list.add("Select option");
            Cursor c = db1.rawQuery("select * from " + CITY + " where state = '" + stateName + "'", null);
            while (c.moveToNext()) {
                String name = c.getString(0);
                list.add(name);
            }
            return list;
        } catch (Exception e) {
       //     Log.e(">>", e.getMessage() + "");
            return null;
        }
    }


    @Nullable
    public List<String> getCityListExceptCityName(String stateName, String cityName) {
        try {
            List<String> list = new ArrayList<>();
            //      list.add("Select option");
            Cursor c = db1.rawQuery("select * from " + CITY + " where state = '" + stateName + "'", null);
            while (c.moveToNext()) {
                String name = c.getString(0);
                if (!name.equals(cityName))
                    list.add(name);
            }
            return list;
        } catch (Exception e) {
      //      Log.e(">>", e.getMessage() + "");
            return null;
        }
    }


    public String getCityID(String cityName) {

        String cityID = null;
        try {
            Cursor c = db1.rawQuery("select * from " + CITY + " where city = '" + cityName + "'", null);
            if (c.moveToNext()) {
                cityID = c.getString(2);
            }
       //     Log.e(">>", "CityID: " + cityID);
            return cityID;
        } catch (Exception e) {
       //     Log.e(">>", e.getMessage() + "");
            return cityID;
        }
    }

    public String getStateID(String stateName) {

        String stateID = null;
        try {
            Cursor c = db1.rawQuery("select * from " + STATE + " where state = '" + stateName + "'", null);
            if (c.moveToNext()) {
                stateID = c.getString(1);
            }
    //        Log.e(">>", "StateID: " + stateID);
            return stateID;
        } catch (Exception e) {
    //        Log.e(">>", e.getMessage() + "");
            return stateID;
        }
    }
}
