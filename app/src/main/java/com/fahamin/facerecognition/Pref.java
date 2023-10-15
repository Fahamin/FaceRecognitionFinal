package com.fahamin.facerecognition;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pref {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int OUTPUT_SIZE = 192; //Output size of model

    public Pref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("HashMap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void insertToSP(HashMap<String, SimilarityClassifier.Recognition> jsonMap) {

        jsonMap.putAll(readFromSP());
        String jsonString = new Gson().toJson(jsonMap);
        editor.putString("map", jsonString);
        //System.out.println("Input josn"+jsonString.toString());
        editor.apply();
        Toast.makeText(context, "Recognitions Saved", Toast.LENGTH_SHORT).show();
    }

    public void insetToImage(String key, String value) {
        editor.putString(key, value);
        //System.out.println("Input josn"+jsonString.toString());
        editor.apply();
    }

    public HashMap<String, SimilarityClassifier.Recognition> readFromSP() {
        String defValue = new Gson().toJson(new HashMap<String, SimilarityClassifier.Recognition>());
        String json = sharedPreferences.getString("map", defValue);
        TypeToken<HashMap<String, SimilarityClassifier.Recognition>> token = new TypeToken<HashMap<String, SimilarityClassifier.Recognition>>() {
        };
        HashMap<String, SimilarityClassifier.Recognition> retrievedMap = new Gson().fromJson(json, token.getType());
        // System.out.println("Output map"+retrievedMap.toString());

        //During type conversion and save/load procedure,format changes(eg float converted to double).
        //So embeddings need to be extracted from it in required format(eg.double to float).
        for (Map.Entry<String, SimilarityClassifier.Recognition> entry : retrievedMap.entrySet()) {
            float[][] output = new float[1][OUTPUT_SIZE];
            ArrayList arrayList = (ArrayList) entry.getValue().getExtra();
            arrayList = (ArrayList) arrayList.get(0);
            for (int counter = 0; counter < arrayList.size(); counter++) {
                output[0][counter] = ((Double) arrayList.get(counter)).floatValue();
            }
            entry.getValue().setExtra(output);


        }
        Toast.makeText(context, "Recognitions Loaded", Toast.LENGTH_SHORT).show();
        return retrievedMap;
    }

}
