package com.atesliuk.investing_simulator.businesslogic;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FinancialApi {

    private List<String> symbols;

    public FinancialApi() {
        symbols = new ArrayList<>();
        String output= "";

        try{
            //getting a file from a resource folder
            InputStream resource = new ClassPathResource("static/apidata/USCompanies.txt").getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
            output = reader.lines().collect(Collectors.joining("\n"));
        }catch (Exception e){
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray(output);

        for (int i=0; i<jsonArray.length();i++){
            JSONObject jo = jsonArray.getJSONObject(i);
            symbols.add(jo.getString("symbol"));
        }

    }

    public void getStockInfo(String ticker){

    }

    public static void main(String[] args) {
        FinancialApi f = new FinancialApi();
    }


}
