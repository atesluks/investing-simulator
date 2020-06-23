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

/**
 * This method retrieves financial information for stocks from Alphavantage API,
 * launches and keeps the ongoing process of retrieving and updating the financial data
 */
public class FinancialApi {

    //API restrictions:
    //500 API requests per day and 5 per minute
    //20 stocks (+ time series data) -> each stock and time series data is updated 12-13 times per day
    //Each stock is updated ~once per two hours

    //Base URL for API calls
    private final String BASE_URL = "https://www.alphavantage.co/query?";
    //API key
    private final String API_KEY="S8A9YDO8RF1BSB8W";
    //Link for demo time series data information
    private final String DEMO_TIME_SESRIES = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=IBM&outputsize=full&apikey=demo";

    //HashMap of all stocks (key - stock symbol, value - StockInfo object)
    //the variable is static so it can be accessed and shared within all application
    public static Map<String, StockInfo> stocks;
    //Priority queue for retrieving quote information about stocks.
    //Is used that all stocks ae evenly updated
    private List<String> priorityQueue;
    //Priority queue for stocks' historical time series data
    //Is used that all stocks' historical time series data is updated evenly
    private List<String> priorityQueueTimeSeries;
    //Variable that tells the application if the financial information should keep updating or not
    private boolean keepUpdating;
    //Variable that tells the application when the requests were blocked for the last time (so the
    //ongoing updating process is more efficient)
    private LocalDateTime lastBlockedRequestTime;

    //for testing/auditing
    private Map<String, Double> testPrices;
    private Map<String, String> testTimeSeries;

    /**
     * The constructor of the class, initializes all necessary objects and
     * launches all required processes
     */
    public FinancialApi() {
        //initialization of used objects
        priorityQueue = new LinkedList<>();
        priorityQueueTimeSeries = new LinkedList<>();
        lastBlockedRequestTime = LocalDateTime.now().minusMinutes(5);
        testPrices = new HashMap<>();
        testTimeSeries = new HashMap<>();
        keepUpdating = true;
        //retrieved symbols of stocks that will be used in this application
        // (20 stocks were picked for this application due to API constraints)
        prepareListOfStocks();
        //starting ongoing financial data updating process
        ongoingUpdatingAll();
    }

    /**
     * The method updates query informatin about the provided stock
     * @param symbol - a String object, symbol of stock that has to be updated
     */
    public void updateStockQuotes(String symbol){
        //if a stock with provided symbol has not been updated yet and is in the priority queue
        // (on its way to update) then we will not do that twice, so we stop the function (return)
        if (priorityQueue.contains(symbol)){
            return;
        }
        else{
            //otherwise, if the symbol is not in the queue, then it should be updated
            // and we let the application know (and other future processed) that the stock is
            // in the process of updating
            priorityQueue.add(symbol);
        }

        //if there is less than one minute passed since the last API block, we will wait
        //for the time its is left (there are imit of 5 requests per a minute, so if we got a
        // block, we have to wait at least a minute)
        long timeDifference = lastBlockedRequestTime.until(LocalDateTime.now(), SECONDS);
        if (timeDifference<60){
            try {
                TimeUnit.SECONDS.sleep(timeDifference + 1);
            } catch (InterruptedException e) {
                System.err.println("Something went wrong in FinancialAPI.UpdateStockQuotes(): "+e);
            }
        }

        //Custom API url for getting quote information for the searched stock
        String uri = BASE_URL+"function=GLOBAL_QUOTE&symbol="+symbol+"&apikey="+API_KEY;

        //Initializing an HttpClient Object and the request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        //sending an asynchronous request for the data (asynchronous, because we want to send
        // many requests at the same time and not wait while previous will be over)
        //Once the response is retrieved, we pass it to the processResponse function to process it
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(x -> {
                    processResponse(x.body(), symbol);
                    return x.body();
                });
    }

    /**
     * The function updates multiple stocks
     * @param symbols list of symbols of stocks that have to be updated
     */
    public void updateStockQuotes(Set<String> symbols){
        for (String s : symbols){
            updateStockQuotes(s);
        }
    }

    /**
     * This function updates time series data for the passed stock
     * @param symbol stock symbol for which time series has to be retrieved
     */
    public void updateStockTimeSeries(String symbol){
        //if a stock with provided symbol has not been updated yet and is in the priority queue
        // (on its way to update) then we will not do that twice, so we stop the function (return)
        if (priorityQueueTimeSeries.contains(symbol)){
            return;
        }
        else{
            //if the symbol is not in the priority queue, then it should be updated
            priorityQueueTimeSeries.add(symbol);
        }

        //if there is less than one minute passed since the last API block, we will wait
        //for the remaining time before sending the API request
        long timeDifference = lastBlockedRequestTime.until(LocalDateTime.now(), SECONDS);
        if (timeDifference<60){
            try {
                TimeUnit.SECONDS.sleep(timeDifference+1);
            } catch (InterruptedException e) {
                System.err.println("Something went wrong in FinancialAPI.UpdateStockQuotes(): "+e);
            }
        }

        //Custom API url for retrieving time series information for a particular stock
        String uri = BASE_URL+"function=TIME_SERIES_DAILY&symbol="+symbol+"&outputsize=full&apikey="+API_KEY;

        //Initializing an HttpClient and HttpRequest objects
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        //Sending the request asynchronously and then passing the resonse to the processing function
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(x -> {
                    processResponseTimeSeries(x.body(), symbol);
                    return x.body();
                });
    }

    /**
     * the function updates time series data for multiple functions
     * @param symbols - list of stock symbols that has to be updated
     */
    public void updateStockTimeSeries(Set<String> symbols){
        for (String s : symbols){
            updateStockTimeSeries(s);
        }
    }

    /**
     * The function processes stock quote data response
     * @param output - String response from the HttpRequest
     * @param symbol - symbol of the stock for which the information was retrieved
     */
    private void processResponse(String output, String symbol){
        //System.out.println("Response: "+output.replace('\n', ' '));

        //removing the stock from the priority queue, so we let the application know that the stock is updated
        priorityQueue.remove(symbol);
        try{
            //parsing JSON objects to our custom StockInfo object
            JSONObject jsonObject = new JSONObject(output);
            JSONObject quote = jsonObject.getJSONObject("Global Quote");
            StockInfo stockInfo = stocks.get(symbol);
            stockInfo.setPrice(Double.parseDouble(quote.getString("05. price")));
            stockInfo.setDailyChangePercents(quote.getString("10. change percent"));
            stockInfo.setLastUpdated(LocalDateTime.now());

            //logging the information
            System.out.println("--- "+symbol+" was retrieved successfully!!! The queue: "+priorityQueue);
            testPrices.put(symbol, stockInfo.getPrice());
            System.out.println("--- "+testPrices);

            //If the priority queue is empty, we let to know about it
            if (priorityQueue.isEmpty())
                System.out.println("All stocks updated. Next update will be in an hour");
        }catch(Exception e){
            //If an exception is thrown (when the response was not as expected) we log that
            System.out.println("\t\t*API got blocked for the symbol "+symbol+". Will retry a bit later... ");
            //System.out.println("Error: "+e);

            //Measuring time between our last API call block and this one
            long timeDifference = lastBlockedRequestTime.until(LocalDateTime.now(), SECONDS);

            //If the difference in both times is large enough, we record current API blocking time as the latest blocking time.
            //Otherwise, blocking time remains as it was, and this call was blocked because less than one minute has passed since the last block,
            //so we have to wait till one minute expires from that previous API block
            if (timeDifference>45)
                lastBlockedRequestTime = LocalDateTime.now();

            //finally, if we catch an exception, that means that information abbout this stock was not updated successfully,
            // so we relaunch update of this stock once again
            updateStockQuotes(symbol);
        }
    }

    /**
     * The function processes stock time series data response
     * @param output - String response from the HttpRequest
     * @param symbol - symbol of the stock for which the information was retrieved
     */
    private void processResponseTimeSeries(String output, String symbol){
        //System.out.println("Response: "+output.replace('\n', ' '));

        //removing the stock from the priority queue, so we let the application know that
        //the time series data for the stock is updated
        priorityQueueTimeSeries.remove(symbol);
        try{
            //Parsing JSON response
            JSONObject jsonObject = new JSONObject(output).getJSONObject("Time Series (Daily)");

            StockInfo stockInfo = stocks.get(symbol);

            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()){
                String key = keys.next();
                JSONObject oneDayData = jsonObject.getJSONObject(key);
                StockTimeSeriesData timeSeriesData = new StockTimeSeriesData();

                timeSeriesData.setDate(key);
                timeSeriesData.setOpen(Double.parseDouble(oneDayData.getString("1. open")));
                timeSeriesData.setClose(Double.parseDouble(oneDayData.getString("4. close")));
                timeSeriesData.setHigh(Double.parseDouble(oneDayData.getString("2. high")));
                timeSeriesData.setLow(Double.parseDouble(oneDayData.getString("3. low")));
                timeSeriesData.setVolume(Double.parseDouble(oneDayData.getString("5. volume")));

                stockInfo.getTimeSeries().add(timeSeriesData);
                Collections.sort(stockInfo.getTimeSeries());
            }

            //logging
            System.out.println("--------- Time series for "+symbol+" was retrieved successfully!!! The time series queue: "+priorityQueueTimeSeries);
            testTimeSeries.put(symbol, output.substring(0, 50).replace("\n", " "));
            System.out.println("--------- "+testTimeSeries);

            //if the priority queue is empty, we log that everything is updated
            if (priorityQueueTimeSeries.isEmpty())
                System.out.println("Time series data for all stocks is updated. Next update will be in an hour");
        }catch(Exception e){
            //if the exception is catched, that means that the request was blocked and we have to wait
            //for some time before sending request once again
            System.out.println("\t\t***API got blocked for time series for the symbol "+symbol+". Will retry a bit later... " + output.substring(0, 200).replace("\n", " "));
            long timeDifference = lastBlockedRequestTime.until(LocalDateTime.now(), SECONDS);

            //if time difference between last blocked request and this one is large enough, then we record this time
            //as a new time of last blocked API call
            if (timeDifference>45)
                lastBlockedRequestTime = LocalDateTime.now();

            //sine the data was not updated for this stock, we call a method to update it once again
            updateStockTimeSeries(symbol);
        }
    }

    /**
     * A method that launches all ongoing updating process
     */
    private void ongoingUpdatingAll(){
        //the process runs in a separate thread, here the runnable is implemented
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //while 'keepUpdating' variable is true, the process will run continuously
                //the variable can be changed upon request
                while (keepUpdating){
                    //Lists for priority queues are initialized
                    priorityQueue = new LinkedList<>();
                    priorityQueueTimeSeries = new LinkedList<>();

                    //update for quote data and time series data for all stocks is launched
                    //the update will happen every 2 hours due to API limitations
                    try {
                        System.out.println("\n\n----- BIG PRICE UPDATE! -----");
                        updateStockQuotes(getStocks().keySet());
                        updateStockTimeSeries(getStocks().keySet());
                        TimeUnit.HOURS.sleep(2);
                    } catch (InterruptedException e) {
                        //in case some exceptions will occure, they wll be logged
                        System.err.println("Something went wrong in FinancialApi.updating all: "+e);
                    }
                }
            }
        });

        //the thread is launched
        thread.start();

    }

    /**
     * The method takes custom list of stocks that will be used in the application.
     * There are 20 selected stocks stored in the data file in the resource folder
     */
    private void prepareListOfStocks(){
        //initializing a HashMap where keys will be String objects (symbols of stocks)
        //and values will be StockInfo objects
        stocks = new HashMap<>();

        //Reading all text from the data file
        String output= "";
        try{
            //getting a file from a resource folder (with 25 companies)
            InputStream resource = new ClassPathResource("static/apidata/USCompanies.txt").getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
            output = reader.lines().collect(Collectors.joining("\n"));
        }catch (Exception e){
            e.printStackTrace();
        }

        //Converting the output text from the file to JSON Object and parsing it to our
        // custom StockInfo objects
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


    //returns HashMap of all stocks
    public Map<String, StockInfo> getStocks() {
        return stocks;
    }

    //the method returns value of keepUpdating variable.
    //the variable is true if the stocks are updating,
    //and false if the updating process is paused
    public boolean isKeepUpdating() {
        return keepUpdating;
    }

    //the method starts or stops the updating process depending on which value is passed
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
