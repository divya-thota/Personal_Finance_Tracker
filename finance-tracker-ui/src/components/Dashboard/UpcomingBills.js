import React, { useState } from "react";

const UpcomingBills = () => {
  const [bills, setBills] = useState([
    { id: 1, name: "Rent", dueDate: "2025-04-01", amount: 1165, status: "Pending" },
    { id: 2, name: "Amex Payment", dueDate: "2025-04-07", amount: 500, status: "Pending" },
    { id: 3, name: "Discover Payment", dueDate: "2025-04-07", amount: 500, status: "Pending" },
    { id: 4, name: "Internet Bill", dueDate: "2025-04-11", amount: 50, status: "Pending" },
  ]);

  const markAsPaid = (id) => {
    setBills((prevBills) =>
      prevBills.map((bill) =>
        bill.id === id ? { ...bill, status: "Paid" } : bill
      )
    );
  };

  return (
    <ul className="list-group" style={{fontSize:"14px"}}>
      {bills
        .sort((a, b) => new Date(a.dueDate) - new Date(b.dueDate))
        .map((bill) => (
          <li
            key={bill.id}
            className="list-group-item d-flex justify-content-between align-items-center">
            <div>
              <p className="m-0">{bill.name}</p>
              <p className="m-0 text-muted"> {bill.dueDate}</p>
              <p className="m-0 text-muted">Amount: ${bill.amount}</p>
            </div>
            <div>
              {bill.status !== "Paid" ? (
                <button className="btn btn-sm btn-success" onClick={() => markAsPaid(bill.id)}>
                  ✅ Pay
                </button>
              ) : (
                <span className="badge bg-success">Paid</span>
              )}
            </div>
          </li>
        ))}
    </ul>
  );
};

export default UpcomingBills;