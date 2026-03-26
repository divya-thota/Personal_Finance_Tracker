import React, { useState, useEffect } from "react";
import IncomeTable from '../IncomeTable';
import {fetchAllIncomes, fetchUnreviewedIncomes} from '../../../controllers/Incomes';

const TabContent = ({ activeTab,
  incomeCategories,
  setIncomeCategories,
  filteredAllIncomesData,
  filteredUnreviewedIncomesData,
  setAllIncomesData,
  setUnreviewedIncomesData}) => {

  useEffect(() => {
    const loadData = async () => {
      const incomesData = await fetchAllIncomes();
      const unreviewedIncomesData = await fetchUnreviewedIncomes();
      setAllIncomesData(incomesData);
      setUnreviewedIncomesData(unreviewedIncomesData);
    };
    loadData();
  }, [activeTab]);

  // Handle record updates
  const handleUpdateData = (updatedIncome) => {
    setAllIncomesData((prevData) =>
      prevData.map((income) =>
        income.id === updatedIncome.id ? updatedIncome : income
      )
    );
    setUnreviewedIncomesData((prevData) =>
      prevData.map((income) =>
        income.id === updatedIncome.id ? updatedIncome : income
      )
    );
  };

  switch (activeTab) {
    case "AllIncomes":
      return (
        <IncomeTable
          data={filteredAllIncomesData}
          setData={setAllIncomesData}
          categories={incomeCategories}
          setCategories={setIncomeCategories}
          handleUpdateData={handleUpdateData} // Pass the handler for updates
        />
      );
    case "UnreviewedIncomes":
      return (
        <IncomeTable
          data={filteredUnreviewedIncomesData}
          setData={setUnreviewedIncomesData}
          categories={incomeCategories}
          setCategories={setIncomeCategories}
          handleUpdateData={handleUpdateData} // Pass the handler for updates
        />
      );
    default:
      return <div>Showing All Incomes</div>;
  }
};

export default TabContent;