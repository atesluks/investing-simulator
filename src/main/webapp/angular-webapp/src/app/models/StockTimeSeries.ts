export class StockTimeSeries{
  private date: number;
  private open: number;
  private high: number;
  private low: number;
  private close: number;
  private volume: number;


  constructor(date: number, open: number, high: number, low: number, close: number, volume: number) {
    this.date = date;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.volume = volume;
  }
}
