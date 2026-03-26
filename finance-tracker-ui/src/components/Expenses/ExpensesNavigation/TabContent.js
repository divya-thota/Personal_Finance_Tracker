import React, { useState, useEffect } from "react";
import EditableTable from '../EditableTable';
import ViewTable from '../../ViewTable/ViewTable';
import { fetchAllExpenses, fetchUnreviewedExpenses } from '../../../controllers/Expenses';

const TabContent = ({ activeTab,
  handleUpdateSplit,
  expenseCategories,
  setExpenseCategories,
  filteredAllExpensesData,
  filteredUnreviewedExpensesData,
  setAllExpensesData,
  setUnreviewedExpensesData,
  filteredSplitwiseExpensesData
   }) => {

  useEffect(() => {
    const loadData = async () => {
      const expensesData = await fetchAllExpenses();
      const unreviewedExpensesData = await fetchUnreviewedExpenses();
      setAllExpensesData(expensesData);
      setUnreviewedExpensesData(unreviewedExpensesData);
    };
    loadData();
  }, [activeTab]);

  // Handle record updates
  const handleUpdateData = (updatedExpense) => {
    setAllExpensesData((prevData) =>
      prevData.map((expense) =>
        expense.id === updatedExpense.id ? updatedExpense : expense
      )
    );
    setUnreviewedExpensesData((prevData) =>
      prevData.map((expense) =>
        expense.id === updatedExpense.id ? updatedExpense : expense
      )
    );
  };

  switch (activeTab) {
    case "AllExpenses":
      return (
        <EditableTable
          handleUpdateSplit={handleUpdateSplit}
          data={filteredAllExpensesData}
          setData={setAllExpensesData}
          categories={expenseCategories}
          setCategories={setExpenseCategories}
          handleUpdateData={handleUpdateData} // Pass the handler for updates
        />
      );
    case "UnreviewedExpenses":
      return (
        <EditableTable
          handleUpdateSplit={handleUpdateSplit}
          data={filteredUnreviewedExpensesData}
          setData={setUnreviewedExpensesData}
          categories={expenseCategories}
          setCategories={setExpenseCategories}
          handleUpdateData={handleUpdateData} // Pass the handler for updates
        />
      );
    case "SplitwiseExpenses":
      return <ViewTable data={filteredSplitwiseExpensesData} />;
    default:
      return <div>Showing All Expenses</div>;
  }
};

export default TabContent;