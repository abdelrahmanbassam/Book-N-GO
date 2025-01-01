import StarIcon from '@mui/icons-material/Star';
import Rating from '@mui/material/Rating';
import axios from 'axios';
import React, { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getHallData } from "../api";
import image from "../assets/Alexandria-Library.png";
import { Header } from "../components/Header";
import { UserContext } from '../UserContext';
import BookingDialog from "./components/BookingDialog";
import FeedbackDialog from "./components/FeedbackDialog";
import WeekCalender from "./components/WeekCalender";
import styles from "./HallDetails.module.css";

export const HallDetails = () => {
  const { id, workspaceId } = useParams();
  const [startDate, setStartDate] = useState(new Date());
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [isFeedbackDialogOpen, setIsFeedbackDialogOpen] = useState(false);
  const [rating, setRating] = useState(0);
  const [comment, setComment] = useState('');
  const { user, setUser } = useContext(UserContext);
  const [feedback, setFeedback] = useState([]);

  const [data, setData] = useState({
    "name": "Alexandria bibliotheca Great Hall",
    "description": "The Great Hall of the Library of Alexandria in Alexandria, Egypt, was the largest and most famous part of the Library of Alexandria, which was part of the research institution called the Musaeum. The Musaeum was a part of the Royal Library of Alexandria, an institution that was part of the Museum of Alexandria. The Museum was a place of learning in ancient Alexandria, and many great thinkers worked there.",
    "rating": 3,
    "feedbacks": []
  });

  useEffect(() => {
    getHallData(workspaceId, id).then(data => setData(data)).catch(error => console.error('Error fetching hall data:', error));
  }, [id, workspaceId]);

  const handleRatingChange = (event, newRating) => {
    setRating(newRating);
    setIsFeedbackDialogOpen(true);
  };

  const handleCommentChange = (event) => {
    setComment(event.target.value);
  };

  const handleSubmitFeedback = async () => {
    try {
      const userId = user.id;
      const response = await axios.post(
        `http://localhost:8080/workspace/${workspaceId}/halls/${id}/feedback/add?userId=${userId}`,
        {
          rating,
          comment
        },
        {
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        }
      );
      if (response.status === 200) {
        alert('Feedback submitted successfully');
        console.log('Feedback response:', response.data); // Log the Hall JSON response
      } else {
        alert('Failed to submit feedback');
      }
    } catch (error) {
      console.error('Error submitting feedback:', error);
      alert('Error submitting feedback');
    }
    setIsFeedbackDialogOpen(false);
  };

  const handleBookNowClick = () => {
    setIsDialogOpen(true);
  };

  const handleDialogClose = () => {
    setIsDialogOpen(false);
  };

  const handleReserve = (startTime, endTime) => {
    console.log(`Reserved from ${startTime} to ${endTime}`);
    setIsDialogOpen(false);
  };

  const handleFeedbackDialogClose = () => {
    setIsFeedbackDialogOpen(false);
  };

  return (
    <>
      <Header />
      <div className={styles["hall-details__container"]}>
        <div className={styles["hall-details__preview"]}>
          <img src={image} alt="Hall Preview" />
          <h1>{data['name']}</h1>
          <button onClick={handleBookNowClick}>Book Now!</button>
        </div>
        <div className={styles["hall-details__info"]}>
          <div className={styles["hall-details__info__rating"]}>
            <h2>Ratings:</h2>
            <div>
              <Rating
                name="hall-rating"
                value={rating}
                onChange={handleRatingChange}
                icon={<StarIcon fontSize="inherit" />}
                emptyIcon={<StarIcon fontSize="inherit" sx={{ color: "white" }} />}
              />
            </div>
          </div>
          <div className={styles["hall-details__info__description"]}>
            <h2>Description:</h2>
            <p>{data['description']}</p>
          </div>
          <div className={styles["hall-details__info__schedule"]}>
            <h2>Schedules:</h2>
            <div className={styles["hall-details__info__schedule__pagination"]}>
              <button onClick={() => setStartDate(new Date(startDate.setDate(startDate.getDate() - 3)))}>Previous</button>
              <button onClick={() => setStartDate(new Date(startDate.setDate(startDate.getDate() + 3)))}>Next</button>
            </div>
            <WeekCalender hallId={id} startDate={startDate} />
          </div>
          <div className={styles["hall-details__info__feedback"]}>
            <h2>Feedback:</h2>
            <ul>
              {data['feedbacks'].map((feedback, index) => (
                // add feedback.rating as stars
                <div className={styles["hall-details__info__feedback__item"]}>
                <div className={styles["hall-details__info__feedback__user"]}>
                {feedback.user.name.split(' ').map(word => word.charAt(0).toUpperCase()).join('')}
                </div>
                <div>
                  <Rating name="read-only" value={feedback.rating} readOnly />
                  <li key={index} className={styles["hall-details__info__feedback__comment"]}>{feedback.content}</li>
                </div>
                </div>
              ))}
            </ul>
          </div>
        </div>
      </div>
      {isDialogOpen && (
        <BookingDialog
          hallId={id}
          onClose={handleDialogClose}
          onReserve={handleReserve}
        />
      )}
      {isFeedbackDialogOpen && (
        <FeedbackDialog
          rating={rating}
          comment={comment}
          onCommentChange={handleCommentChange}
          onSubmit={handleSubmitFeedback}
          onClose={handleFeedbackDialogClose}
        />
      )}
    </>
  );
};