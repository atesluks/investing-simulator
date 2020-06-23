import {StockTimeSeries} from "./StockTimeSeries";

export class Stock{

  companyName: string;
  symbol: string;
  price: number;
  dailyChangePercents: string;
  exchange: string;
  lastUpdated: string;
  timeSeries: StockTimeSeries[];


  constructor(companyName: string, price: number) {
    this.companyName = companyName;
    this.price = price;
  }
}
