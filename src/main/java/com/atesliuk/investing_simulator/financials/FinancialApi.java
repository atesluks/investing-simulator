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
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.SECONDS;


public class FinancialApi {

    //API restrictions:
    //500 API requests per day (5 per minute)
    //20 stocks -> each stock is updated 25 times per day
    //Each stock is updated ~once per hour

    private final String BASE_URL = "https://www.alphavantage.co/query?";
    private final String API_KEY="S8A9YDO8RF1BSB8W";

    private Map<String, StockInfo> stocks;
    private List<String> priorityQueue;
    private boolean keepUpdating;
    private LocalDateTime lastBlockedRequestTime;

    //for testing/auditing
    private Map<String, String> testPrices;


    public FinancialApi() {
        prepareListOfStocks();
        priorityQueue = new LinkedList<>();
        lastBlockedRequestTime = LocalDateTime.now().minusMinutes(5);
        testPrices = new HashMap<>();
        keepUpdating = true;
        ongoingUpdatingAll();
    }

    public void updateStockQuotes(String symbol){
        //if a symbol has not been updated yet and is in the queue (on its way to update)
        //then we will not do that twice, so we return
        if (priorityQueue.contains(symbol)){
            return;
        }
        else{
            //if the symbol is not in the queue, then it should be updated
            priorityQueue.add(symbol);
        }

        //if there is less than one minute passed since the last API block, we will wait
        long timeDifference = lastBlockedRequestTime.until(LocalDateTime.now(), SECONDS);
        if (timeDifference<60){
            try {
                TimeUnit.SECONDS.sleep(timeDifference+5);
            } catch (InterruptedException e) {
                System.err.println("Something went wrong in FinancialAPI.UpdateStockQuotes(): "+e);
            }
        }

        String uri = BASE_URL+"function=GLOBAL_QUOTE&symbol="+symbol+"&apikey="+API_KEY;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(x -> {
                    processResponse(x.body(), symbol);
                    return x.body();
                });
    }

    public void updateStockQuotes(Set<String> symbols){
        Map<String, StockInfo> result = new HashMap<>();
        for (String s : symbols){
            updateStockQuotes(s);
        }
    }

    private void processResponse(String output, String symbol){
        priorityQueue.remove(symbol);
        try{
            JSONObject jsonObject = new JSONObject(output);
            JSONObject quote = jsonObject.getJSONObject("Global Quote");
            StockInfo stockInfo = stocks.get(symbol);
            stockInfo.setPrice(quote.getString("05. price"));
            stockInfo.setDailyChangePercents(quote.getString("10. change percent"));

            System.out.println("--- "+symbol+" was retrieved successfully!!! The queue: "+priorityQueue);
            testPrices.put(symbol, stockInfo.getPrice());
            System.out.println("--- "+testPrices);
        }catch(Exception e){
            System.out.println("\t\t*API got blocked for the symbol "+symbol+". Will retry a bit later... ");
            long timeDifference = lastBlockedRequestTime.until(LocalDateTime.now(), SECONDS);
            if (timeDifference>30)
                lastBlockedRequestTime = LocalDateTime.now();
            updateStockQuotes(symbol);
        }
    }

    private void ongoingUpdatingAll(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (keepUpdating){
                    priorityQueue = new LinkedList<>();
                    try {
                        System.out.println("\n\n----- BIG PRICE UPDATE! -----");
                        updateStockQuotes(getStocks().keySet());
                        TimeUnit.HOURS.sleep(1);
                    } catch (InterruptedException e) {
                        System.err.println("Something went wrong in FinancialApi.updating all: "+e);
                    }
                }
            }
        });

        thread.start();

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



    public Map<String, StockInfo> getStocks() {
        return stocks;
    }

    public boolean isKeepUpdating() {
        return keepUpdating;
    }

    public void setKeepUpdating(boolean keepUpdating) {
        //if the process was stopped and now it is launched,
        //then we should start the updating process from the beginning
        if (this.keepUpdating==false && keepUpdating == true){
            this.keepUpdating = true;
            ongoingUpdatingAll();
            System.out.println("\n --- keepUpdating was set to true - updating will start soon! ---\n");

        //if the process is running, then we should stop it
        }else if (this.keepUpdating==true && keepUpdating == false){
            this.keepUpdating = keepUpdating;
            System.out.println("\n --- keepUpdating was set to false - updating will stop soon! ---\n");
        }

    }

}
