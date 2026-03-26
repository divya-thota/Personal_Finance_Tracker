import React, { useState, useEffect } from "react";
import { Table, Button, Dropdown, DropdownButton, Form } from "react-bootstrap";
import { FaTrashAlt } from "react-icons/fa";


const EditableTable = ({ handleUpdateSplit, data,  setData, categories, setCategories}) => {

  const handleFieldChange = async(e, field, index) => {
    const newValue = field === "reviewed" ? e.target.checked : (
        field === "amount"
          ? parseFloat(e.target.value.replace("$ ", ""))
          : field === "category"
          ? parseInt(e)
          : e.target.value
      );
    const expenseId = data[index].id;
    try {
      const response = await fetch(`/database/update_expense/${expenseId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json", },
        body: JSON.stringify({ [field]: newValue }), // Dynamic update based on the field
      });
      if (response.ok) {
        const newData = [...data];
        newData[index][field] = field === "amount"? `$ ${newValue}` : newValue;
        setData(newData);
      } else {
        console.error(`Failed to update expense: ${response.statusText}`);
      }
    } catch (error) {
      console.error(`Error updating ${field}:`, error);
    }
  };

  const handleAddSplit = (index) => {
    const amount = parseFloat(data[index].amount.replace(/[^0-9.-]+/g, ""));
    const description = `${data[index].date} ${data[index].description}`;
    handleUpdateSplit(amount, description);
  };

  const handleDelete = async(index) => {
    const expenseId = data[index].id;
     // Get the expense ID from the data
    try {
      const response = await fetch(`database/update_expense/${expenseId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json", },
        body: JSON.stringify({ ["is_deleted"]: true }),
      });
      if (response.ok) {
        // Remove the deleted item from the local state
        const newData = data.filter((_, i) => i !== index);
        setData(newData);
      } else {
        const errorMessage = await response.text();
        console.error(`Failed to delete expense: ${errorMessage}`);
      }
    } catch (error) {
      console.error("Error deleting expense:", error);
    }
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
                  checked={row.reviewed}
                  onChange={(e) => handleFieldChange(e, "reviewed", index)}
                />
              </td>
              <td>
                <Form.Control
                  type="date"
                  defaultValue={row.date}
                  onBlur={(e) => handleFieldChange(e, "date", index)}
                  className="form-control-sm date-field"
                />
              </td>
              <td>
                <Form.Control
                  type="text"
                  defaultValue={row.amount}
                  onBlur={(e) => handleFieldChange(e, "amount", index)}
                  className="form-control-sm amount-field disable-border"
                />
              </td>
              <td>
                <Form.Control
                  type="text"
                  defaultValue={row.description}
                  onBlur={(e) => handleFieldChange(e, "description", index)}
                  className="form-control-sm disable-border"
                />
              </td>
              <td>
                <DropdownButton
                  variant="light"
                  title={
                    categories.find((category) => category.key === row.category)
                      ?.value || "Select Category"
                  }
                  onSelect={(selectedCategoryId) =>
                    handleFieldChange(selectedCategoryId,"category", index)
                  }
                  size="sm"
                  className="category-field"
                >
                  {categories.map((category) => (
                    <Dropdown.Item key={category.key} eventKey={category.key}>
                      {category.value}
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