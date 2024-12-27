import React from "react";
import { ListGroup, Form, InputGroup } from "react-bootstrap";

const SplitTabContent = ({ activeSplitTab, participants, handleCheckboxChange, handleAmountChange}) => {

  const hasGroup = participants.some(participant => participant.type === 'group');

  participants = hasGroup
  ? [...new Map(
      participants.reduce((acc, participant) => {
        if (participant.type === 'group') {
          // Include both the group AND its participants
          return [...acc, participant, ...participant.participants];
        }
        return [...acc, participant];
      }, []).map(p => [p.id, p])
    ).values()]
  : participants.length > 0 ? [...participants, {id: 22705915, name: 'Divya Thota', type: 'friend'}]: [];

  switch (activeSplitTab) {
    case "SplitEqually":
      return (
        <ListGroup>
          {participants.map((friend) => (
            friend.type === 'friend' && (
              <ListGroup.Item
                key={friend.id} // Ensure unique key
                className="d-flex justify-content-between align-items-center"
              >
                <span>{friend.name}</span>
                <Form.Check
                  type="checkbox"
                  id={friend.id}
                  onChange={(e) => handleCheckboxChange(friend.id, e.target.checked)}
                />
              </ListGroup.Item>
            )
          ))}
        </ListGroup>
      );
    case "SplitShares":
        return (
          <ListGroup>
            {participants.map((friend) => (
            friend.type === 'friend' && (
              <ListGroup.Item
                key={friend.id} // Ensure unique key
                className="d-flex justify-content-between align-items-center"
              >
              <Form.Label column sm="7" size="sm">{friend.name}</Form.Label>
              <InputGroup size="sm">
                <InputGroup.Text >$</InputGroup.Text>
                <Form.Control aria-label="Enter Amount" onChange={(e) => handleAmountChange(friend.id, e.target.value)}/>
              </InputGroup>
              </ListGroup.Item>
              )
             ))}
           </ListGroup>
         );
    default:
      return <div>Showing List of all members</div>;
  }
};

export default SplitTabContent;