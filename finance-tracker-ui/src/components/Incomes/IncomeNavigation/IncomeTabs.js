import React, { useState } from "react";
import TabNavigation from "./TabNavigation";
import TabContent from "./TabContent";
import { Button } from "react-bootstrap";

const IncomeTabs = ({ incomeCategories,
                      setIncomeCategories,
                      filteredAllIncomesData,
                      filteredUnreviewedIncomesData,
                      setAllIncomesData,
                      setUnreviewedIncomesData }) => {
  const [activeTab, setActiveTab] = useState("AllIncomes");

  return (
    <div>
      <TabNavigation activeTab={activeTab} setActiveTab={setActiveTab} />
      <div className="tab-content mt-3">
        <TabContent
          activeTab={activeTab}
          incomeCategories={incomeCategories}
          setIncomeCategories={setIncomeCategories}
          filteredAllIncomesData={filteredAllIncomesData}
          filteredUnreviewedIncomesData={filteredUnreviewedIncomesData}
          setAllIncomesData={setAllIncomesData}
          setUnreviewedIncomesData={setUnreviewedIncomesData}
        />
      </div>
    </div>
  );
};

export default IncomeTabs;
