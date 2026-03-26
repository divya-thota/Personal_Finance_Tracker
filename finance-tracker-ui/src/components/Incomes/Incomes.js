import React, { useState } from 'react';
import IncomeTabs from './IncomeNavigation/IncomeTabs';

const Incomes = ({ incomeCategories,
                   setIncomeCategories,
                   filteredAllIncomesData,
                   filteredUnreviewedIncomesData,
                   setAllIncomesData,
                   setUnreviewedIncomesData }) => {

  return (
    <div className="container">
      <div className="row">
        <div className="col-md-8 card mt-3 expense-tabs">
          <IncomeTabs incomeCategories={incomeCategories}
                      setIncomeCategories={setIncomeCategories}
                      filteredAllIncomesData={filteredAllIncomesData}
                      filteredUnreviewedIncomesData={filteredUnreviewedIncomesData}
                      setAllIncomesData={setAllIncomesData}
                      setUnreviewedIncomesData={setUnreviewedIncomesData}/>
        </div>
        <div className="col-md-3 card mt-3 ms-2">
          sidebar
        </div>
      </div>
    </div>
  );
};

export default Incomes;
