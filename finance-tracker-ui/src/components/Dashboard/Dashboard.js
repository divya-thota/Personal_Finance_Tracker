import React from 'react';
import MilestoneTracker from './MilestoneTracker';
import CategoryExpenseChart from "./CategoryExpenseChart";
import IncomeExpenseChart from "./IncomeExpenseChart";
import InvestmentPieChart from "./InvestmentPieChart";
import InvestmentProfitsChart from "./InvestmentProfitsChart";
import UpcomingBills from "./UpcomingBills";

import '../../css/Dashboard.css';

const Dashboard = ({ filteredAllExpensesData, filteredAllIncomesData, expenseCategories }) => {
//PASTEL COlORS
//["#fbb4ae","#b3cde3","#ccebc5","#decbe4","#fed9a6","#ffffcc","#e5d8bd","#fddaec","#f2f2f2"]
  return (
    <div>
      <h2>Dashboard Content</h2>
      <p>Welcome to the Dashboard section!</p>
      <div className="container mb-2">
        <div className="row">
          <div className="col-md-4 card mt-2">
            <h5 className="text-center">Expenses</h5>
            <CategoryExpenseChart />
          </div>
          <MilestoneTracker />
        </div>
        <div className="row">
          <div className="col-md-4 card mt-2">
            <h5 className="text-center">Income</h5>
            <CategoryExpenseChart />
          </div>
          <div className="col-md-5 card mt-2">
            <h5 className="text-center">Income vs Expenses</h5>
            <IncomeExpenseChart />
          </div>
          <div className="col-md-3 card mt-2 ps-0 pe-0">
            <h5 className="text-center">📅 Upcoming Bills</h5>
            <UpcomingBills />
          </div>
        </div>
        <div className="row">
          <div className="col-md-4 card mt-2">
            <h5 className="text-center">Investment Profits</h5>
            <InvestmentProfitsChart />
          </div>
          <div className="col-md-4 card mt-2">
            <h5 className="text-center">All Investments</h5>
            <InvestmentPieChart />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
