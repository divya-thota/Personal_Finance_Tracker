import React, { useState, useEffect } from "react";
import { Form, Button } from "react-bootstrap";
import splitwiseLogo from "../../../assets/splitwise-logo.png";
import AddParticipants from "./AddParticipants";

const SplitwiseExpense = ({ splitState }) => {
  const [amount, setAmount] = useState(splitState.amount);
  const [description, setDescription] = useState(splitState.description);
  const [participants, setParticipants] = useState([]);

  useEffect(() => {
    setAmount(splitState.amount);
    setDescription(splitState.description);
  }, [splitState]);

  const handleSubmit = (e) => {
    e.preventDefault();
    alert(
      `Expense added: ${amount}, Split between: ${participants.join(", ")}`
    );
    setAmount("");
    setDescription("");
    setParticipants([]);
  };

  return (
    <div className="container mt-5">
      <img
        src={splitwiseLogo}
        alt="Splitwise Logo"
        className="splitwise-logo"
      />
      <p className="mt-3">Add an Expense</p>
      <Form onSubmit={handleSubmit}>
        <AddParticipants />
        <Form.Group controlId="description">
          <Form.Control
            type="text"
            placeholder="Enter description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
          />
        </Form.Group>
        <Form.Group controlId="amount">
          <Form.Control
            type="number"
            placeholder="Enter amount"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            required
          />
        </Form.Group>

        <Button variant="dark" type="submit" className="mb-3">
          Add Expense
        </Button>
      </Form>
    </div>
  );
};

export default SplitwiseExpense;
