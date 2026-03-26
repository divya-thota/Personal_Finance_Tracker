import React from "react";
import { Table } from "react-bootstrap";
import '../../css/ViewTable.css';

const ViewTable = ({ data }) => {
  if (!data || data.length === 0) {
    return <div className="container mt-5">No data available</div>;
  }
  // Extract table headers dynamically
  const headers = Object.keys(data[0]);

  return (
    <div className="container mt-1">
      <Table hover className="viewTable">
        <thead>
          <tr>
            {headers.map((header) => (
              <th key={header}>{header}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((row, rowIndex) => (
            <tr key={rowIndex}>
              {headers.map((header) => (
                <td key={`${rowIndex}-${header}`}>{row[header]}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default ViewTable;