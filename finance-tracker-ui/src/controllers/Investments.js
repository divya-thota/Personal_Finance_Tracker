const fetchInvestments = async () => {
  const response = await fetch("/database/all_investments");
  const investments = await response.json();
  return investments.map((investment) => ({
    ActivityDate: investment.date,
    Account: investment.investment_account,
    Name: investment.stock_name,
    Quantity: investment.number_of_shares,
    BuyPrice: investment.purchase_amount ? `$ ${investment.purchase_amount}` : null,
    PricePer: investment.purchase_share_cost ? `$ ${investment.purchase_share_cost}` : null,
    SellPrice: investment.sell_amount ? `$ ${investment.sell_amount}` : null,
    SellPer: investment.sell_share_cost ? `$ ${investment.sell_share_cost}` : null,
    Dividend: investment.total_dividend ? `$ ${investment.total_dividend}` : null,
    Profit: investment.profits ? `$ ${investment.profits}` : null,
    Loss: investment.loss ? `$ ${investment.loss}` : null,
  }));
};

export { fetchInvestments };