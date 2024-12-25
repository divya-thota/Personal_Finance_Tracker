import React from "react";
import { Dropdown } from "react-bootstrap";

const FilterDropdown = ({ filter, setFilter }) => (
  <Dropdown className="mt-3 d-flex justify-content-end">
    <Dropdown.Toggle variant="dark">{filter}</Dropdown.Toggle>
    <Dropdown.Menu>
      <Dropdown.Item onClick={() => setFilter("All Transactions")}>
        All Transactions
      </Dropdown.Item>
      <Dropdown.Item onClick={() => setFilter("2025")}>2025</Dropdown.Item>
      <Dropdown.Item onClick={() => setFilter("January")}>January 2025</Dropdown.Item>
      <Dropdown.Item onClick={() => setFilter("February")}>February 2025</Dropdown.Item>
      <Dropdown.Item onClick={() => setFilter("March")}>March 2025</Dropdown.Item>
      <Dropdown.Item onClick={() => setFilter("April")}>April 2025</Dropdown.Item>
      <Dropdown.Item onClick={() => setFilter("May")}>May 2025</Dropdown.Item>
      <Dropdown.Item onClick={() => setFilter("June")}>June 2025</Dropdown.Item>
      <Dropdown.Item onClick={() => setFilter("July")}>July 2025</Dropdown.Item>
      <Dropdown.Item onClick={() => setFilter("August")}>August 2025</Dropdown.Item>
      <Dropdown.Item onClick={() => setFilter("September")}>September 2025</Dropdown.Item>
      <Dropdown.Item onClick={() => setFilter("October")}>October 2025</Dropdown.Item>
      <Dropdown.Item onClick={() => setFilter("November")}>November 2025</Dropdown.Item>
      <Dropdown.Item onClick={() => setFilter("December")}>December 2025</Dropdown.Item>
    </Dropdown.Menu>
  </Dropdown>
);

export default FilterDropdown;
