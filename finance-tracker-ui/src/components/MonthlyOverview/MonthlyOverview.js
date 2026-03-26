
import React, { useState, useEffect } from "react";
import ViewTable from '../ViewTable/ViewTable';

const MonthlyOverview = ({filteredAllExpensesData, filteredAllIncomesData}) => {
  return (
    <div className="container">
      <div className="row">
        <div className="col-md-6 card mt-3">
          <h5 className="text-center">Expenses</h5>
          <ViewTable data={filteredAllExpensesData}/>
        </div>
        <div className="col-md-6 card mt-3">
          <h5 className="heading text-center">Incomes</h5>
          <ViewTable data={filteredAllIncomesData}/>
        </div>
      </div>
    </div>
  );
};

export default MonthlyOverview;
