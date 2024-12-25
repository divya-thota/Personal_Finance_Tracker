import React from "react";
import EditableTable from '../EditableTable';

const TabContent = ({ activeTab, filter, handleUpdateSplit }) => {
  switch (activeTab) {
    case "AllExpenses":
      return <EditableTable handleUpdateSplit={handleUpdateSplit} />;
    case "UnreviewedExpenses":
      return <div>Showing {filter} in Unreviewed Expenses</div>;
    case "SplitwiseExpenses":
        return <div>Showing {filter} in Splitwise Expenses</div>;
    default:
      return <div>Showing {filter} in All Expenses</div>;
  }
};

export default TabContent;
