import React, { useState } from "react";
import TabNavigation from "./TabNavigation";
import TabContent from "./TabContent";

const ExpenseTabs = ({ handleUpdateSplit,
                       expenseCategories,
                       setExpenseCategories,
                       filteredAllExpensesData,
                       filteredUnreviewedExpensesData,
                       setAllExpensesData,
                       setUnreviewedExpensesData,
                       filteredSplitwiseExpensesData}) => {
  const [activeTab, setActiveTab] = useState("AllExpenses");

  return (
    <div>
      <TabNavigation activeTab={activeTab} setActiveTab={setActiveTab} />
      <div className="tab-content mt-3">
        <TabContent
          handleUpdateSplit={handleUpdateSplit}
          activeTab={activeTab}
          expenseCategories={expenseCategories}
          setExpenseCategories={setExpenseCategories}
          filteredAllExpensesData={filteredAllExpensesData}
          filteredUnreviewedExpensesData={filteredUnreviewedExpensesData}
          setAllExpensesData={setAllExpensesData}
          setUnreviewedExpensesData={setUnreviewedExpensesData}
          filteredSplitwiseExpensesData={filteredSplitwiseExpensesData}
        />
      </div>
    </div>
  );
};

export default ExpenseTabs;
