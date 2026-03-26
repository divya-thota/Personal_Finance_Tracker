import React, { useState } from 'react';
import ExpenseTabs from './ExpensesNavigation/ExpenseTabs';
import SplitwiseExpense from './Splitwise/SplitwiseExpense';
import '../../css/Expenses.css';

const Expenses = ({
  expenseCategories,
  setExpenseCategories,
  filteredAllExpensesData,
  filteredUnreviewedExpensesData,
  setAllExpensesData,
  setUnreviewedExpensesData,
  filteredSplitwiseExpensesData}) => {
  const [splitState, setSplitState] = useState({
    amount: '',
    description: '',
  });

  const handleUpdateSplit = (amount, description) => {
    setSplitState({
      amount,
      description,
    });
  };

  return (
    <div className="container">
      <div className="row">
        <div className="col-md-8 card mt-3 expense-tabs">
          <ExpenseTabs
            handleUpdateSplit={handleUpdateSplit}
            expenseCategories={expenseCategories}
            setExpenseCategories={setExpenseCategories}
            filteredAllExpensesData={filteredAllExpensesData}
            filteredUnreviewedExpensesData={filteredUnreviewedExpensesData}
            setAllExpensesData={setAllExpensesData}
            setUnreviewedExpensesData={setUnreviewedExpensesData}
            filteredSplitwiseExpensesData={filteredSplitwiseExpensesData} />
        </div>
        <div className="col-md-3 card mt-3 ms-2">
          <SplitwiseExpense splitState={splitState} />
        </div>
      </div>
    </div>
  );
};

export default Expenses;
