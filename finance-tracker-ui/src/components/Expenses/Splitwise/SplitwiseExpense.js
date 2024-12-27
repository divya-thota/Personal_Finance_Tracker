import React, { useState, useEffect } from "react";
import { Form, Button } from "react-bootstrap";
import splitwiseLogo from "../../../assets/splitwise-logo.png";
import AddParticipants from "./AddParticipants";
import SplitTabs from "./SplitwiseNavigation/SplitTabs";
import SplitTabContent from "./SplitwiseNavigation/SplitTabContent";

const SplitwiseExpense = ({ splitState }) => {
  const [amount, setAmount] = useState(splitState.amount);
  const [description, setDescription] = useState(splitState.description);
  const [participants, setParticipants] = useState([]);
  const [activeSplitTab, setActiveSplitTab] = useState("SplitEqually");
  const [isEquallyClicked, setIsEquallyClicked] = useState(false); // State to track visibility
  const [checkedList, setCheckedList] = useState([]);
  const [shareAmount, setShareAmount] = useState([]);

  const handleEquallyClick = () => {
    setIsEquallyClicked((prev) => !prev); // Toggle the state
  };

  const handleCheckboxChange = (id, isChecked) => {
    if (isChecked) {
      setCheckedList(prevList => {
        const updatedList = [...prevList.filter(item => item.id !== id), { id, isChecked }];
        return updatedList;
      });
    } else {
      setCheckedList(prevList => {
        const updatedList = prevList.filter(item => item.id !== id);
        return updatedList;
      });
    }
  };

  const handleAmountChange = (id, amount) => {
    setShareAmount(prevShareAmount => {
      const updatedShareAmount = prevShareAmount.map(item =>
        item.id === id ? { ...item, amount } : item
      );
      //Check if an item with the given ID exists in updatedShareAmount
      if (!updatedShareAmount.some(item => item.id === id)) {
        //If there is no item with the given ID, create one
        updatedShareAmount.push({id, amount})
      }
      return updatedShareAmount;
    });
  };

  // To populate the input fields
  useEffect(() => {
    setAmount(splitState.amount);
    setDescription(splitState.description);
  }, [splitState]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("/api/create_expense", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          amount,
          description,
          participants,
          isEquallyClicked,
          activeSplitTab,
          checkedList,
          shareAmount
        }),
      });

      if (response.ok) {
        alert("Expense added successfully!");
        setAmount("");
        setDescription("");
        setParticipants([]);
        setIsEquallyClicked(false);
        setActiveSplitTab("SplitEqually");
        setCheckedList([]);
        setShareAmount([]);
      } else {
        const errorData = await response.json();
        alert(`Error adding expense: ${errorData.message}`);
      }
    } catch (error) {
      console.error("Error adding expense:", error);
    }
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
        <AddParticipants setParticipants={setParticipants} participants={participants}/>
        <Form.Group controlId="description">
          <Form.Control
            type="text"
            placeholder="Enter description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
            className="m-2"
          />
        </Form.Group>
        <Form.Group controlId="amount">
          <Form.Control
            type="text"
            placeholder="Enter amount"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            required
            className="m-2"
          />
        </Form.Group>

        <p className="m-2 split-equally">Paid by you and split &nbsp;
        <Button variant="outline-secondary" size="sm" onClick={handleEquallyClick}>equally</Button>
        </p>
        {isEquallyClicked && (
          <>
            <SplitTabs
              activeSplitTab={activeSplitTab}
              setActiveSplitTab={setActiveSplitTab}
            />
            <SplitTabContent activeSplitTab={activeSplitTab}
              participants={participants}
              handleCheckboxChange={handleCheckboxChange}
              handleAmountChange={handleAmountChange}/>
          </>
        )}
        <Button variant="dark" type="submit" className="m-2 mb-2">
          Add Expense
        </Button>
      </Form>
    </div>
  );
};

export default SplitwiseExpense;
