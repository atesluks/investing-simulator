package com.atesliuk.investing_simulator.financials;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



public class FinancialApi {

    private Map<String, StockInfo> stocks;

    private final String BASE_URL = "https://www.alphavantage.co/query?";
    private final String API_KEY="S8A9YDO8RF1BSB8W";


    public FinancialApi() {
        prepareListOfStocks();

    }

    public void updateStockQuotes (String symbol){

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL+"function=GLOBAL_QUOTE&symbol="+symbol+"&apikey="+API_KEY))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonObject = new JSONObject(response.body());
            System.out.println(jsonObject);
            JSONObject quote = jsonObject.getJSONObject("Global Quote");
            StockInfo stockInfo = stocks.get(quote.getString("01. symbol"));
            stockInfo.setPrice(quote.getDouble("05. price"));
            stockInfo.setDailyChangePercents(quote.getDouble("10. change percent"));

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void updateStockQuotes(List<String> symbols){
        Map<String, StockInfo> result = new HashMap<>();
        for (String s : symbols){
            updateStockQuotes(s);
        }
    }

    private void prepareListOfStocks(){
        stocks = new HashMap<>();

        String output= "";
        try{
            //getting a file from a resource folder (with 25 companies)
            InputStream resource = new ClassPathResource("static/apidata/USCompanies.txt").getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
            output = reader.lines().collect(Collectors.joining("\n"));
        }catch (Exception e){
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray(output);

        for (int i=0; i<jsonArray.length();i++){
            JSONObject jo = jsonArray.getJSONObject(i);

            StockInfo stock = new StockInfo();
            stock.setCompanyName(jo.getString("name"));
            stock.setSymbol(jo.getString("symbol"));
            stock.setExchange(jo.getString("exchange"));

            stocks.put(stock.getSymbol(), stock);
        }
    }

    // getters

    public Map<String, StockInfo> getStocks() {
        return stocks;
    }



    public static void main(String[] args) {
        FinancialApi f = new FinancialApi();
        System.out.println(f.stocks);
    }


}
