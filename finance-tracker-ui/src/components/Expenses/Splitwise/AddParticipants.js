import React, { useState, useEffect } from "react";
import { Form, Dropdown, Badge } from "react-bootstrap";
import { FaTimes } from "react-icons/fa";

const AddParticipants = ({ setParticipants, participants }) => {
  const [query, setQuery] = useState("");
  const [options, setOptions] = useState([]);
  const [filteredOptions, setFilteredOptions] = useState([]);
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);

  // Fetch friends from the API
  useEffect(() => {
    const fetchFriends = async () => {
      try {
        const response = await fetch("/api/groups_friends");
        const groupFriends = await response.json(); // Assuming this returns an array of objects with `name` and `id`
        setOptions(groupFriends); // Set the options with objects containing id and name
      } catch (error) {
        console.error("Error fetching friends:", error);
      }
    };

    fetchFriends();
  }, []);

  const handleInputChange = (event) => {
    const value = event.target.value;
    setQuery(value);

    if (value) {
      // Filter options by matching the `name` property of the objects
      const filtered = options.filter((option) =>
        option.name.toLowerCase().includes(value.toLowerCase()) // Filter by name
      );
      setFilteredOptions(filtered);
      setIsDropdownOpen(true);
    } else {
      setFilteredOptions([]);
      setIsDropdownOpen(false);
    }
  };

  const handleItemSelect = (item) => {
    if (!participants.some((selected) => selected.id === item.id)) {
      setParticipants([...participants, { id: item.id,
        name: item.name,
        type: item.type,
        participants: item.participants }]);
    }
    setQuery("");
    setIsDropdownOpen(false);
  };

  const handleRemoveItem = (itemId) => {
    // Remove the selected participant by ID
    setParticipants(participants.filter((participant) => participant.id !== itemId));
  };

  return (
    <Form.Group controlId="multiSelectInput">
      <Form.Control
        type="text"
        value={query}
        onChange={handleInputChange}
        placeholder="Enter Names"
        className="m-2"
      />
      {isDropdownOpen && filteredOptions.length > 0 && (
        <Dropdown.Menu show>
          {filteredOptions.map((option) => (
            <Dropdown.Item
              key={option.id} // Use `id` as the unique key
              as="button"
              onClick={() => handleItemSelect(option)} // Pass the whole object to `handleItemSelect`
            >
              {option.name} {/* Display name */}
            </Dropdown.Item>
          ))}
        </Dropdown.Menu>
      )}
      <div className="d-flex flex-wrap mb-2">
        {participants.map((item, index) => {
          // Find the name corresponding to the selected ID
          const selectedItem = options.find((option) => option.id === item.id);
          return (
            <Badge key={index} pill className="m-1 badge-primary">
              {selectedItem ? selectedItem.name : "Unknown"} {/* Display the name */}
              <FaTimes
                className="ms-1 cursor-pointer"
                onClick={() => handleRemoveItem(item.id)} // Remove by ID
              />
            </Badge>
          );
        })}
      </div>
    </Form.Group>
  );
};

export default AddParticipants;
