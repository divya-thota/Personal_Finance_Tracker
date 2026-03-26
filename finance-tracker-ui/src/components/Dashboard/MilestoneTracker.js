//import React, { useState } from 'react';
import React from 'react';
import { FaCheckCircle, FaRegCircle } from 'react-icons/fa';

// Sample milestone data with dates
const milestones = [
  { title: 'DNR Booking Amount', date: '11-26-2024', completed: true, amount: 'Rs. 5,00,000' },
  { title: 'DNR Booking Amount', date: '12-05-2024', completed: true, amount: 'Rs. 4,00,000'  },
  { title: '30 Days of Booking', date: '01-03-2025', completed: true, amount: 'Rs. 9,00,000'  },
  { title: 'Underground Services', date: '04-25-2025', completed: false, amount: 'Rs. 27,00,000'  },
  { title: 'Completion of Roads', date: '07-31-2025', completed: false, amount: 'Rs. 18,00,000'  },
  { title: 'Site Registration', date: '12-31-2025', completed: false, amount: 'Rs.27,00,000'  },
];

const MilestoneTracker = () => {
//  const [completedMilestones, setCompletedMilestones] = useState(
//    milestones.filter((milestone) => milestone.completed).length
//  );

  const handleMilestoneClick = (index) => {
    const updatedMilestones = [...milestones];
    updatedMilestones[index].completed = !updatedMilestones[index].completed;
//    setCompletedMilestones(updatedMilestones.filter((milestone) => milestone.completed).length);
  };

  return (
    <div className="col-md-8 mt-2">
      <div className="milestone-tracker-container">
        <h2 className="milestone-header">DNR Solace Plot #88</h2>
        <div className="timeline-container">
          {milestones.map((milestone, index) => (
            <React.Fragment key={index}>
              <div className="milestone-item">
                <div
                  className={`milestone-circle ${milestone.completed ? 'completed' : ''}`}
                  onClick={() => handleMilestoneClick(index)}
                >
                  {milestone.completed ? <FaCheckCircle /> : <FaRegCircle />}
                </div>
                <span className="milestone-label">{milestone.title}</span>
                <span className="milestone-date">{milestone.date}</span>
                <span className="milestone-amount">{milestone.amount}</span>
              </div>
            </React.Fragment>
          ))}
        </div>
        </div>
    </div>
  );
};

export default MilestoneTracker;