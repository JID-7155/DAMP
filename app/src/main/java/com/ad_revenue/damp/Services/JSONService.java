package com.ad_revenue.damp.Services;

import com.ad_revenue.damp.Exceptions.JSONException;

import android.content.Context;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;

public class JSONService {
    private final JSONParser parser = new JSONParser();

    public JSONService() {
        //Leaving this here just in case.
    }

    public String getProperty(String propertyName, Context context) throws JSONException {
        //Pass in getApplicationContext for context, or maybe just replace it here.
        //TODO: Fix up InputStream.
        try {
            InputStream stream = context.getAssets().open("example.json");
            InputStreamReader reader = new InputStreamReader(stream);

            Object obj = parser.parse(reader);

            JSONObject json = (JSONObject) obj;

            return (String) json.get(propertyName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new JSONException();
    }
}
