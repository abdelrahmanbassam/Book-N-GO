import React, { useState } from 'react';
import style from './BookingDialog.module.css';
import { createBooking } from '../../api';

function BookingDialog({ hallId, onClose, onReserve }) {
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const handleReserve = async () => {
    if (!startTime || !endTime) {
      setErrorMessage('Please select start and end time');
      return;
    }

    try {
      const response = await createBooking(hallId, startTime, endTime);
      console.log('Reservation successful:', response);
      onReserve(startTime, endTime);
      onClose();
    } catch (error) {
      console.error('Failed to create booking:', error);
      setErrorMessage(error.response?.data?.message || 'Booking is not valid');
    }

  };

  return (
    <>
      <div className={style["dialog-overlay"]} onClick={onClose}></div>
      <div className={style["reserve-dialog"]}>
        <div className={style["dialog-content"]}>
          <h2>Reserve Spot {hallId}</h2>
          <div className={style["form-group"]}>
            <label>Start Time:</label>
            <input
              type="datetime-local"
              value={startTime}
              onChange={(e) => setStartTime(e.target.value)}
            />
          </div>
          <div className={style["form-group"]}>
            <label>End Time:</label>
            <input
              type="datetime-local"
              value={endTime}
              onChange={(e) => setEndTime(e.target.value)}
            />
          </div>
          {errorMessage && <p className={style["error-message"]}>{errorMessage}</p>}
          <button onClick={handleReserve} className={style["d-reserve-button"]}>Reserve</button>
          <button onClick={onClose} className={style["close-button"]}>Close</button>
        </div>
      </div>
    </>
  );
}

export default BookingDialog;