import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navigation from './components/AppNavigation/Navigation';
import 'bootstrap/dist/css/bootstrap.css';
import FilterDropdown from "./FilterDropdown";

import Dashboard from './components/Dashboard/Dashboard';
import Expenses from './components/Expenses/Expenses';
import Incomes from './components/Incomes/Incomes';
import Investments from './components/Investments/Investments';
import MonthlyOverview from './components/MonthlyOverview/MonthlyOverview';
import {fetchAllExpenses, fetchUnreviewedExpenses, fetchSplitwiseExpenses} from './controllers/Expenses';
import {fetchAllIncomes, fetchUnreviewedIncomes} from './controllers/Incomes';
import {fetchExpenseCategories, fetchIncomeCategories} from './controllers/Categories';
import {fetchInvestments} from './controllers/Investments';

function App() {
  const [filter, setFilter] = useState("All Transactions");
  const [allExpensesData, setAllExpensesData] = useState([]);
  const [unreviewedExpensesData, setUnreviewedExpensesData] = useState([]);
  const [splitwiseExpensesData, setSplitwiseExpensesData] = useState([]);
  const [expenseCategories, setExpenseCategories] = useState([]);
  const [incomeCategories, setIncomeCategories] = useState([]);
  const [allIncomesData, setAllIncomesData] = useState([]);
  const [unreviewedIncomesData, setUnreviewedIncomesData] = useState([]);
  const [allInvestmentsData, setAllInvestmentsData] = useState([]);

  const [filteredAllExpensesData, setFilteredAllExpensesData] = useState([]);
  const [filteredUnreviewedExpensesData, setFilteredUnreviewedExpensesData] = useState([]);
  const [filteredSplitwiseExpensesData, setFilteredSplitwiseExpensesData] = useState([]);
  const [filteredAllIncomesData, setFilteredAllIncomesData] = useState([]);
  const [filteredUnreviewedIncomesData, setFilteredUnreviewedIncomesData] = useState([]);
  const [filteredAllInvestmentsData, setFilteredAllInvestmentsData] = useState([]);


  useEffect(() => {
    const loadData = async () => {
      const expensesData = await fetchAllExpenses();
      const unreviewedExpensesData = await fetchUnreviewedExpenses();
      const splitwiseExpensesData = await fetchSplitwiseExpenses();
      const expenseCategoriesData = await fetchExpenseCategories();
      const incomeCategoriesData = await fetchIncomeCategories();
      const incomesData = await fetchAllIncomes();
      const unreviewedIncomesData = await fetchUnreviewedIncomes();
      const investmentsData = await fetchInvestments();

      setAllExpensesData(expensesData);
      setUnreviewedExpensesData(unreviewedExpensesData);
      setSplitwiseExpensesData(splitwiseExpensesData);
      setExpenseCategories(expenseCategoriesData);
      setIncomeCategories(incomeCategoriesData);
      setAllIncomesData(incomesData);
      setUnreviewedIncomesData(unreviewedIncomesData);
      setAllInvestmentsData(investmentsData)
    };
    loadData();
  }, []);

  useEffect(() => {
    if (filter === "All Transactions") {
      // Show all data if filter is "All Transactions"
      setFilteredAllExpensesData(allExpensesData);
      setFilteredUnreviewedExpensesData(unreviewedExpensesData);
      setFilteredSplitwiseExpensesData(splitwiseExpensesData);
      setFilteredAllIncomesData(allIncomesData);
      setFilteredUnreviewedIncomesData(unreviewedIncomesData);
      setFilteredAllInvestmentsData(allInvestmentsData);
    } else {
      // Filter based on the exact year-month value
      setFilteredAllExpensesData(allExpensesData.filter((expense) => expense.date.startsWith(filter)));
      setFilteredUnreviewedExpensesData(unreviewedExpensesData.filter((expense) => expense.date.startsWith(filter)));
      setFilteredSplitwiseExpensesData(splitwiseExpensesData.filter((expense) => expense.AddedDate.startsWith(filter)));
      setFilteredAllIncomesData(allIncomesData.filter((income) => income.date.startsWith(filter)));
      setFilteredUnreviewedIncomesData(unreviewedIncomesData.filter((income) => income.date.startsWith(filter)));
      setFilteredAllInvestmentsData(allInvestmentsData.filter((investment) => investment.ActivityDate.startsWith(filter)));

    }
  }, [ allExpensesData, unreviewedExpensesData, splitwiseExpensesData, allIncomesData, unreviewedIncomesData, filter]);

  return (
    <Router>
      <div className="container-fluid">
        <div className="row">
          <div className="col-md-2">
            <Navigation />
          </div>
          <div className="col-md-10">
            <div className="content">
              <FilterDropdown setFilter={setFilter}/>
              <Routes>
                <Route path="/dashboard"
                  element={<Dashboard
                    filter={filter}
                    filteredAllExpensesData={filteredAllExpensesData}
                    filteredAllIncomesData={filteredAllIncomesData}
                    expenseCategories={expenseCategories}
                  />}
                />
                <Route path="/expenses"
                  element={<Expenses
                    expenseCategories={expenseCategories}
                    setExpenseCategories={setExpenseCategories}
                    filteredAllExpensesData={filteredAllExpensesData}
                    filteredUnreviewedExpensesData={filteredUnreviewedExpensesData}
                    setAllExpensesData={setAllExpensesData}
                    setUnreviewedExpensesData={setUnreviewedExpensesData}
                    filteredSplitwiseExpensesData={filteredSplitwiseExpensesData}
                  />}
                />
                <Route path="/incomes"
                element={<Incomes
                  incomeCategories={incomeCategories}
                  setIncomeCategories={setIncomeCategories}
                  filteredAllIncomesData={filteredAllIncomesData}
                  filteredUnreviewedIncomesData={filteredUnreviewedIncomesData}
                  setAllIncomesData={setAllIncomesData}
                  setUnreviewedIncomesData={setUnreviewedIncomesData}
                  />}
                />
                <Route path="/investments" element={<Investments filteredAllInvestmentsData={filteredAllInvestmentsData}/>} />
                <Route path="/monthlyoverview"
                  element={<MonthlyOverview
                    filteredAllExpensesData={filteredAllExpensesData.map(({ id, reviewed,category, ...filteredExpense }) => filteredExpense)}
                    filteredAllIncomesData={filteredAllIncomesData.map(({ id, reviewed,category, ...filteredExpense }) => filteredExpense)}/>} />
                <Route path="/" element={<Dashboard />} /> {/* Default route */}
              </Routes>
            </div>
          </div>
        </div>
      </div>
    </Router>
  );
}

export default App;
