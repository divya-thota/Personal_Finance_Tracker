import React, { useState } from "react";
import { Table, Button, Dropdown, DropdownButton, Form } from "react-bootstrap";
import { FaTrashAlt } from "react-icons/fa";

const categories = ["Food", "Transport", "Entertainment", "Bills", "Misc"];

const EditableTable = ({ handleUpdateSplit }) => {
  const [data, setData] = useState([
    {
      id: 1,
      checked: false,
      date: "2024-01-01",
      amount: "$ 1000",
      description: "Grocery shopping akhj adlh afhkj jkhbf",
      category: "Food",
    },
    {
      id: 2,
      checked: false,
      date: "2024-02-15",
      amount: "$ 50",
      description: "Uber ride",
      category: "Transport",
    },
  ]);

  const handleCheckboxChange = (index) => {
    const newData = [...data];
    newData[index].checked = !newData[index].checked;
    setData(newData);
  };

  const handleFieldChange = (e, field, index) => {
    const newData = [...data];
    newData[index][field] = e.target.value;
    setData(newData);
  };

  const handleCategoryChange = (category, index) => {
    const newData = [...data];
    newData[index].category = category;
    setData(newData);
  };

  // Handle app split button (you can extend this functionality)
  const handleAddSplit = (index) => {
    const amount = data[index].amount;
    const description = data[index].date + " " + data[index].description;
    handleUpdateSplit(amount, description); // Update splitState in Expenses
  };

  const handleDelete = (index) => {
    const newData = data.filter((_, i) => i !== index);
    setData(newData);
  };

  return (
    <div className="container mt-5">
      <Table size="sm">
        <thead>
          <tr>
            <th></th>
            <th>Date</th>
            <th>Amount</th>
            <th>Description</th>
            <th>Category</th>
            <th></th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {data.map((row, index) => (
            <tr key={row.id}>
              <td>
                <input
                  type="checkbox"
                  checked={row.checked}
                  onChange={() => handleCheckboxChange(index)}
                />
              </td>
              <td>
                <Form.Control
                  type="date"
                  value={row.date}
                  onChange={(e) => handleFieldChange(e, "date", index)}
                  className="form-control-sm date-field"
                />
              </td>
              <td>
                <Form.Control
                  type="text"
                  value={row.amount}
                  onChange={(e) => handleFieldChange(e, "amount", index)}
                  className="form-control-sm amount-field disable-border"
                />
              </td>
              <td>
                <Form.Control
                  type="text"
                  value={row.description}
                  onChange={(e) => handleFieldChange(e, "description", index)}
                  className="form-control-sm disable-border"
                />
              </td>
              <td>
                <DropdownButton
                  variant="light"
                  title={row.category}
                  onSelect={(selectedCategory) =>
                    handleCategoryChange(selectedCategory, index)
                  }
                  size="sm"
                  className="category-field"
                >
                  {categories.map((category, idx) => (
                    <Dropdown.Item key={idx} eventKey={category}>
                      {category}
                    </Dropdown.Item>
                  ))}
                </DropdownButton>
              </td>
              <td>
                <Button
                  variant="light"
                  onClick={() => handleAddSplit(index)}
                  size="sm"
                >
                  Add to Splitwise
                </Button>
              </td>
              <td>
                <Button
                  variant="light"
                  onClick={() => handleDelete(index)}
                  size="sm"
                >
                  <FaTrashAlt />
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default EditableTable;
