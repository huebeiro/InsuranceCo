package com.huebeiro.insuranceco.connection;

import android.os.AsyncTask;
import android.util.Log;

import com.huebeiro.insuranceco.model.Brand;
import com.huebeiro.insuranceco.model.Model;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Adilson on 18/04/2016.
 */

public class HttpRequestModels extends AsyncTask<Integer, Void, Model[]> {
    @Override
    protected Model[] doInBackground(Integer... params) {
        try {
            final String url = "http://fipeapi.appspot.com/api/1/carros/veiculos/" + params[0] + ".json";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Model[] models = restTemplate.getForEntity(url, Model[].class).getBody();
            return models;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Model[] models) {
        super.onPostExecute(models);
        if(models != null){
            Log.i("HttpRequestModels", models.length + " models returned!");
        } else {
            Log.e("HttpRequestModels", "Couldn't return models!");
        }
    }
}