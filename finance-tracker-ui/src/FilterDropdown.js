import React, { useState } from "react";
import { Dropdown } from "react-bootstrap";

const handleFilterChange = (setFilter, setSelectedLabel, filterValue, label) => {
  setFilter(filterValue);
  setSelectedLabel(label); // Update the selected label
};

const FilterDropdown = ({ setFilter }) => {
  const [selectedLabel, setSelectedLabel] = useState("All Transactions");

  const filterOptions = [
    { label: "All Transactions", value: "All Transactions" },
    { label: "2025", value: "2025" },
    { label: "February 2026", value: "2026-02" },
    { label: "January 2026", value: "2026-01" },
    { label: "December 2025", value: "2025-12" },
    { label: "November 2025", value: "2025-11" },
    { label: "October 2025", value: "2025-10" },
    { label: "September 2025", value: "2025-09" },
    { label: "August 2025", value: "2025-08" },
    { label: "July 2025", value: "2025-07" },
    { label: "June 2025", value: "2025-06" },
    { label: "May 2025", value: "2025-05" },
    { label: "April 2025", value: "2025-04" },
    { label: "March 2025", value: "2025-03" },
    { label: "February 2025", value: "2025-02" },
    { label: "January 2025", value: "2025-01" },
    { label: "December 2024", value: "2024-12" },
    { label: "November 2024", value: "2024-11" },
    { label: "October 2024", value: "2024-10" },
    { label: "September 2024", value: "2024-09" },
  ];

  return (
    <div className="mt-3 d-flex justify-content-end">
      <Dropdown className="m-2">
        <Dropdown.Toggle variant="dark">{selectedLabel}</Dropdown.Toggle>
        <Dropdown.Menu>
          {filterOptions.map((filterOption) => (
            <Dropdown.Item
              key={filterOption.value}
              onClick={() =>
                handleFilterChange(
                  setFilter,
                  setSelectedLabel,
                  filterOption.value,
                  filterOption.label
                )
              }
            >
              {filterOption.label}
            </Dropdown.Item>
          ))}
        </Dropdown.Menu>
      </Dropdown>
    </div>
  );
};

export default FilterDropdown;