import React from "react";
import { Nav } from "react-bootstrap";

const TabNavigation = ({ activeTab, setActiveTab }) => (
  <Nav variant="tabs" activeKey={activeTab} onSelect={(tab) => setActiveTab(tab)}>
    <Nav.Item>
      <Nav.Link eventKey="AllExpenses">All Expenses</Nav.Link>
    </Nav.Item>
    <Nav.Item>
      <Nav.Link eventKey="UnreviewedExpenses">Unreviewed Expenses</Nav.Link>
    </Nav.Item>
    <Nav.Item>
      <Nav.Link eventKey="SplitwiseExpenses">Splitwise Expenses</Nav.Link>
    </Nav.Item>
  </Nav>
);

export default TabNavigation;
