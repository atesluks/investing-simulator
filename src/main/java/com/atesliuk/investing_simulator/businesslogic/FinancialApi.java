package com.atesliuk.investing_simulator.businesslogic;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import pl.zankowski.iextrading4j.client.IEXCloudClient;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FinancialApi {

    private List<String> symbols;

    private IEXCloudClient cloudClient;

    public FinancialApi() {
        prepareListOfSymbols();

        try {
            Stock tesla = YahooFinance.get("TSLA", true);
            System.out.println(tesla.getHistory());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public List<Stock> getStockInfo(List<String> requestedStocks){
        return null;
    }

    public List<String> getSymbols() {
        return new ArrayList<>(symbols);
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = new ArrayList<>(symbols);
    }

    public void getStockInfo(String ticker){

    }

    private void prepareListOfSymbols(){
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

    public static void main(String[] args) {
        FinancialApi f = new FinancialApi();
    }


}
