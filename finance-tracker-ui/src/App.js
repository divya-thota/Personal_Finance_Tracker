import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navigation from './components/AppNavigation/Navigation';
import 'bootstrap/dist/css/bootstrap.css';

// Import the components for each route
import Dashboard from './components/Dashboard/Dashboard';
import Expenses from './components/Expenses/Expenses';
import Investments from './components/Investments/Investments';

function App() {
  return (
    <Router>
      <div className="container-fluid">
        <div className="row">
          <div className="col-md-2">
            <Navigation />
          </div>
          <div className="col-md-10">
            <div className="content">
              <Routes>
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/expenses" element={<Expenses />} />
                <Route path="/investments" element={<Investments />} />
                <Route path="/" element={<Dashboard />} /> {/* Default route */}
              </Routes>
            </div>
          </div>
        </div>
      </div>
    </Router>
  );
}

export default App;
