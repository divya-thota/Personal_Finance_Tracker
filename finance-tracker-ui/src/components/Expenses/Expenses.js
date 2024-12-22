import React, { useState } from 'react';
import ExpenseTabs from './ExpenseTabs';
import SplitwiseExpense from './SplitwiseExpense';
import '../../css/Expenses.css';

const Expenses = () => {
  const [splitState, setSplitState] = useState({
    amount: '',
    description: '',
  });

  // Function to update splitState from EditableTable
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
          <ExpenseTabs handleUpdateSplit={handleUpdateSplit} />
        </div>
        <div className="col-md-3 card mt-3 ms-2">
          <SplitwiseExpense splitState={splitState} />
        </div>
      </div>
    </div>
  );
};

export default Expenses;
