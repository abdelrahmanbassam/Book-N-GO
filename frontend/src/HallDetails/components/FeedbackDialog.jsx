import React from 'react';
import styles from './FeedbackDialog.module.css';

const FeedbackDialog = ({ rating, comment, onCommentChange, onSubmit, onClose }) => {
  return (
    <>
      <div className={styles["dialog-overlay"]} onClick={onClose}></div>
      <div className={styles["feedback-dialog"]}>
        <div className={styles["dialog-content"]}>
          <h2>Submit Feedback</h2>
          <p>Rating: {rating} stars</p>
          <textarea
            value={comment}
            onChange={onCommentChange}
            placeholder="Leave a comment"
          />
          <button onClick={onSubmit} className={styles["submit-button"]}>Submit</button>
          <button onClick={onClose} className={styles["close-button"]}>Close</button>
        </div>
      </div>
    </>
  );
};

export default FeedbackDialog;