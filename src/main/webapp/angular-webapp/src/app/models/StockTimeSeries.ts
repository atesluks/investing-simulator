export class StockTimeSeries{
  private date: string;
  private open: string;
  private high: string;
  private low: string;
  private close: string;
  private volume: string;


  constructor(date: string, open: string, high: string, low: string, close: string, volume: string) {
    this.date = date;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.volume = volume;
  }
}
