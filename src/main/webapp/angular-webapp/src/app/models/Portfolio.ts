export class Portfolio{
  id:number;
  name: string;
  dateOfCreation: string;
  initialInvestment: number;
  user: number; //userId
  portfolioStocks: number[]; //ids of PortfolioStocks (stocks in the portfolio)
  deals: number[]; //ids of deals in the portfolio
  user_referenced_id: number;
  current_portfolio_value: number;
  cash: number;


  constructor(name: string, initialInvestment: number, user_referenced_id: number) {
    this.name = name;
    this.initialInvestment = initialInvestment;
    this.user_referenced_id = user_referenced_id;
  }
}
