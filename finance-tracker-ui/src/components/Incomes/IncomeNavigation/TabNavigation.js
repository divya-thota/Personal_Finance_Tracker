import React from "react";
import { Nav } from "react-bootstrap";

const TabNavigation = ({ activeTab, setActiveTab }) => (
  <Nav variant="tabs" activeKey={activeTab} onSelect={(tab) => setActiveTab(tab)}>
    <Nav.Item>
      <Nav.Link eventKey="AllIncomes">All Incomes</Nav.Link>
    </Nav.Item>
    <Nav.Item>
      <Nav.Link eventKey="UnreviewedIncomes">Unreviewed Incomes</Nav.Link>
    </Nav.Item>
  </Nav>
);

export default TabNavigation;
