import React, { useState } from "react";
import { Form, Dropdown, Badge } from "react-bootstrap";
import { FaTimes } from "react-icons/fa"; // For removing selected items

const AddParticipants = () => {
  const [query, setQuery] = useState(""); // User's input query
  const [filteredOptions, setFilteredOptions] = useState([]); // Filtered options based on query
  const [selectedItems, setSelectedItems] = useState([]); // Array of selected items
  const [isDropdownOpen, setIsDropdownOpen] = useState(false); // Controls visibility of the dropdown

  const options = ["Amrita", "Riddhi", "Virgil","Teju", "65 N point Dr"];

  // Handle user input change
  const handleInputChange = (event) => {
    const value = event.target.value;
    setQuery(value);

    if (value) {
      const filtered = options.filter((option) =>
        option.toLowerCase().includes(value.toLowerCase())
      );
      setFilteredOptions(filtered);
      setIsDropdownOpen(true);
    } else {
      setFilteredOptions([]);
      setIsDropdownOpen(false);
    }
  };

  // Handle item selection
  const handleItemSelect = (item) => {
    if (!selectedItems.includes(item)) {
      setSelectedItems([...selectedItems, item]); // Add item to the selected list
    }
    setQuery(""); // Clear the input after selection
    setIsDropdownOpen(false); // Close dropdown
  };

  // Remove selected item
  const handleRemoveItem = (item) => {
    setSelectedItems(selectedItems.filter((i) => i !== item));
  };

  return (
        <Form.Group controlId="multiSelectInput">
          {/* Input field */}
          <Form.Control
            type="text"
            value={query}
            onChange={handleInputChange}
            placeholder="Enter Names"
          />

          {/* Dropdown Menu for suggestions */}
          {isDropdownOpen && filteredOptions.length > 0 && (
            <Dropdown.Menu show>
              {filteredOptions.map((option, index) => (
                <Dropdown.Item
                  key={index}
                  as="button"
                  onClick={() => handleItemSelect(option)}
                >
                  {option}
                </Dropdown.Item>
              ))}
            </Dropdown.Menu>
          )}

          {/* Display selected items as badges */}
          <div className="d-flex flex-wrap mb-2">
            {selectedItems.map((item, index) => (
              <Badge key={index} pill className="m-1 badge-primary">
                {item}
                <FaTimes
                  className="ms-1 cursor-pointer"
                  onClick={() => handleRemoveItem(item)}
                />
              </Badge>
            ))}
          </div>
        </Form.Group>
  );
};

export default AddParticipants;
