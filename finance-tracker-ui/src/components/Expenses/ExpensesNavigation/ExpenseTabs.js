import React, { useState } from "react";
import TabNavigation from "./TabNavigation";
import FilterDropdown from "./FilterDropdown";
import TabContent from "./TabContent";

const ExpenseTabs = ({ handleUpdateSplit }) => {
  const [activeTab, setActiveTab] = useState("AllExpenses");
  const [filter, setFilter] = useState("All Transactions");

  return (
    <div>
      <TabNavigation activeTab={activeTab} setActiveTab={setActiveTab} />
      <FilterDropdown filter={filter} setFilter={setFilter} />
      <div className="tab-content mt-3">
        <TabContent
          activeTab={activeTab}
          filter={filter}
          handleUpdateSplit={handleUpdateSplit}
        />
      </div>
    </div>
  );
};

export default ExpenseTabs;
