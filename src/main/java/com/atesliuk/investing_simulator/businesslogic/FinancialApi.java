package com.atesliuk.investing_simulator.businesslogic;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FinancialApi {

    private List<String> symbols;

    public FinancialApi() {
        symbols = new ArrayList<>();

        String output="";
        try {
            output= new String(Files.readAllBytes(Paths.get("C:\\Users\\TA\\Desktop\\coding\\MyProjects\\InvestingSimulator\\src\\main\\java\\com\\atesliuk\\investing_simulator\\businesslogic\\apidata\\USCompanies.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray(output);

        for (int i=0; i<jsonArray.length();i++){
            JSONObject jo = jsonArray.getJSONObject(i);
            symbols.add(jo.getString("symbol"));
            System.out.println(jo.getString("symbol"));
        }



    }





    public static void main(String[] args) {
        FinancialApi f = new FinancialApi();
    }


}
