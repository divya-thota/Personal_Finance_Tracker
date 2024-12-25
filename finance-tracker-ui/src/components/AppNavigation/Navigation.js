import React from 'react';
import { Link } from 'react-router-dom';
import { Collapse } from 'react-bootstrap';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { TABS } from './Tabs';
import '../../css/Navigation.css';

const Navigation = () => {
  const [openNavBar, setOpenNavBar] = React.useState(false);

  const toggleNav = () => setOpenNavBar(!openNavBar);

  return (
    <div className="d-grid flex-column shadow-lg bg-body-tertiary rounded nav-grid">
      <button className="btn m-3" onClick={toggleNav}>
        <i className="bi bi-list"></i> Menu
      </button>

      <Collapse in={openNavBar}>
        <div id="navTabs">
          <ul className="nav flex-column">
            {TABS.map((tab) => (
              <li className="nav-item" key={tab.name}>
                <Link
                  to={`/${tab.name.toLowerCase()}`}
                  className="nav-link"
                >
                  {tab.name}
                </Link>
              </li>
            ))}
          </ul>
        </div>
      </Collapse>
    </div>
  );
};

export default Navigation;