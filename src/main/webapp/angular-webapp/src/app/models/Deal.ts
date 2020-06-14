export class Deal{

  id:number;
  stockSymbol: string;
  amount: number;
  openPrice: number;
  closingPrice: number;
  openDate: string;
  closingDate: string;
  portfolio_referenced_id: number;
  portfolio: number; //id of a portfolio


  constructor(stockSymbol: string, amount: number, portfolio_referenced_id: number) {
    this.stockSymbol = stockSymbol;
    this.amount = amount;
    this.portfolio_referenced_id = portfolio_referenced_id;
  }
}
