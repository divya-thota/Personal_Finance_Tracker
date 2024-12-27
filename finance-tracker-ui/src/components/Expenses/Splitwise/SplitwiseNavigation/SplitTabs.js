
import React from 'react';
import Nav from 'react-bootstrap/Nav';

const SplitTabs = ({ activeSplitTab, setActiveSplitTab }) => {
  return (
    <Nav className="justify-content-center" activeKey={activeSplitTab} onSelect={(tab) => setActiveSplitTab(tab)}>
      <Nav.Item>
        <Nav.Link eventKey="SplitEqually"><i className="bi bi-card-list"></i></Nav.Link>
      </Nav.Item>
      <Nav.Item>
        <Nav.Link eventKey="SplitShares"><i className="bi bi-bar-chart"></i></Nav.Link>
      </Nav.Item>
    </Nav>
  );
};

export default SplitTabs;
