export class Portfolio{
  id:number;
  action: string;
  ticker: string;
  amount: number;
  date: string;
  profit: number;
  portfolio: number; //id of a portfolio


  constructor(id: number, action: string, ticker: string, amount: number, date: string, profit: number, portfolio: number) {
    this.id = id;
    this.action = action;
    this.ticker = ticker;
    this.amount = amount;
    this.date = date;
    this.profit = profit;
    this.portfolio = portfolio;
  }
}
