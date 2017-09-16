package com.ad_revenue.damp.Services;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class JSONService {
    private final JSONParser parser = new JSONParser();

    public JSONService() {
        //Leaving this here just in case.
    }

    public void writeProperty(Context context, String toWrite, String fileName) {

    }

    public String getProperty(String propertyName, Object sectionName) {
        JSONObject json = (JSONObject) sectionName;
        return (String) json.get(propertyName);
    }

    public String[] getProperties(Context context, String fileName, String propertyName) {
        try {
            InputStream stream = context.getAssets().open(fileName);
            InputStreamReader reader = new InputStreamReader(stream);
            JSONArray myJson = (JSONArray) parser.parse(reader);

            String[] toReturn = new String[myJson.size()];
            Iterator jsonIterator = myJson.iterator();
            int count = 0;

            while(jsonIterator.hasNext()) {
                toReturn[count] = getProperty(propertyName, jsonIterator.next());
                count++;
            }

            return toReturn;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[1];
    }
}