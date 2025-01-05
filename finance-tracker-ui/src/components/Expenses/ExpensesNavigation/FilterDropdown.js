import React from "react";
import { Dropdown,Button } from "react-bootstrap";

const handleFilterChange = async (filterValue) => {
  console.log(filterValue)
};

const FilterDropdown = ({ filter, setFilter, activeTab }) => (
<div className="mt-3 d-flex justify-content-end">
  <Dropdown className="m-2 ">
    <Dropdown.Toggle variant="dark">{filter}</Dropdown.Toggle>
    <Dropdown.Menu>
      {[
        { label: "All Transactions", value: "All Transactions" },
        { label: "2025", value: "2025" },
        { label: "January 2025", value: "January" },
        { label: "February 2025", value: "February" },
        { label: "March 2025", value: "March" },
        { label: "April 2025", value: "April" },
        { label: "May 2025", value: "May" },
        { label: "June 2025", value: "June" },
        { label: "July 2025", value: "July" },
        { label: "August 2025", value: "August" },
        { label: "September 2025", value: "September" },
        { label: "October 2025", value: "October" },
        { label: "November 2025", value: "November" },
        { label: "December 2025", value: "December" },
      ].map((filterOption) => (
        <Dropdown.Item
          key={filterOption.value}
          onClick={() => handleFilterChange(filterOption.value)}
        >
          {filterOption.label}
        </Dropdown.Item>
      ))}
    </Dropdown.Menu>
    </Dropdown>
  </div>
);

export default FilterDropdown;
