package com.huebeiro.insuranceco.connection;

import android.os.AsyncTask;
import android.util.Log;

import com.huebeiro.insuranceco.model.Brand;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Adilson on 18/04/2016.
 */

public class HttpRequestBrands extends AsyncTask<Void, Void, Brand[]> {
    @Override
    protected Brand[] doInBackground(Void... params) {
        try {
            final String url = "http://fipeapi.appspot.com/api/1/carros/marcas.json";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Brand[] brands = restTemplate.getForEntity(url, Brand[].class).getBody();
            return brands;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Brand[] brands) {
        super.onPostExecute(brands);
        if(brands != null){
            Log.i("HttpRequestBrands", brands.length + " brands returned!");
        } else {
            Log.e("HttpRequestBrands", "Couldn't return brands!");
        }
    }
}