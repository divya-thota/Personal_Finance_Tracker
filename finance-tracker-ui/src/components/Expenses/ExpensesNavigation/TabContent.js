import React, { useState, useEffect } from "react";
import EditableTable from '../EditableTable';

const TabContent = ({ activeTab, filter, handleUpdateSplit }) => {
  const [data, setData] = useState([]);
  const [categories, setCategories] = useState([]);
  const [filteredData, setFilteredData] = useState([]);

  const fetchCategories = async () => {
    const response = await fetch("/database/categories");
    const data = await response.json();
    return data.map((category) => ({
      key: category.category_id,
      value: category.name,
    }));
  };

  const fetchExpenses = async () => {
    const response = await fetch("/database/expenses");
    const data = await response.json();
    return data?.map((expense) => ({
      id: expense.expense_id,
      reviewed: expense.reviewed,
      date: expense.expense_date,
      amount: `$ ${expense.amount}`,
      description: expense.description,
      category: expense.category_id?.category_id || null,
    }));
  };

  useEffect(() => {
    const loadData = async () => {
      const categoriesData = await fetchCategories();
      const expensesData = await fetchExpenses();
      setCategories(categoriesData);
      setData(expensesData);
    };
    loadData();
  }, []);

  // Update filtered data based on active tab
  useEffect(() => {
    if (activeTab === "UnreviewedExpenses") {
      setFilteredData(data.filter((expense) => !expense.reviewed));
    } else {
      setFilteredData(data);
    }
  }, [activeTab, data]);

  // Handle record updates
  const handleUpdateData = (updatedExpense) => {
    setData((prevData) =>
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
          data={data}
          setData={setData}
          categories={categories}
          setCategories={setCategories}
          handleUpdateData={handleUpdateData} // Pass the handler for updates
        />
      );
    case "UnreviewedExpenses":
      return (
        <EditableTable
          handleUpdateSplit={handleUpdateSplit}
          data={filteredData}
          setData={setData}
          categories={categories}
          setCategories={setCategories}
          handleUpdateData={handleUpdateData} // Pass the handler for updates
        />
      );
    case "SplitwiseExpenses":
      return <div>Showing {filter} in Splitwise Expenses</div>;
    default:
      return <div>Showing {filter} in All Expenses</div>;
  }
};

export default TabContent;