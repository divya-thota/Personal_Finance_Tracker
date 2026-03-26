
import React from 'react';
import ViewTable from '../ViewTable/ViewTable';

const Investments = ({filteredAllInvestmentsData}) => {
  return (
    <div className="container">
      <div className="row">
        <div className="card mt-3">
          <h5 className="text-center">Investments</h5>
          <ViewTable data={filteredAllInvestmentsData}/>
        </div>
      </div>
    </div>
  );
};

export default Investments;
