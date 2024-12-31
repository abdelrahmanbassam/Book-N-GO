import React, { useEffect, useState } from "react";
import image from "../assets/Alexandria-Library.png";
import styles from "./HallDetails.module.css";
import Rating from '@mui/material/Rating';
import StarIcon from '@mui/icons-material/Star';
import { useParams } from "react-router-dom";
import { Header } from "../components/Header";
import { getHallData } from "../api";
import WeekCalender from "./components/WeekCalender";
import BookingDialog from "./components/BookingDialog"; // Ensure this path is correct

export const HallDetails = () => {
  const { id, workspaceId } = useParams();
  const [startDate, setStartDate] = useState(new Date());
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  const [data, setData] = useState({
    "name": "Alexandria bibliotheca Great Hall",
    "description": "The Great Hall of the Library of Alexandria in Alexandria, Egypt, was the largest and most famous part of the Library of Alexandria, which was part of the research institution called the Musaeum. The Musaeum was a part of the Royal Library of Alexandria, an institution that was part of the Museum of Alexandria. The Museum was a place of learning in ancient Alexandria, and many great thinkers worked there.",
    "rating": 3,
    "comments": [
      "This hall is great!",
      "I love this hall!",
      "This hall is amazing!"
    ]
  });

  useEffect(() => {
    getHallData(workspaceId, id).then(data => setData(data));
  }, [id]);

  const handleBookNowClick = () => {
    setIsDialogOpen(true);
  };

  const handleDialogClose = () => {
    setIsDialogOpen(false);
  };

  const handleReserve = (startTime, endTime) => {
    // Handle reservation logic here
    console.log(`Reserved from ${startTime} to ${endTime}`);
    setIsDialogOpen(false);
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
            <Rating
              name="hall-rating"
              value={data['rating']}
              opacity={1}
              icon={<StarIcon fontSize="inherit" />}
              emptyIcon={<StarIcon fontSize="inherit" sx={{ color: "white" }} />}
            />
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
        </div>
      </div>
      {isDialogOpen && (
        <BookingDialog
          hallId={id}
          onClose={handleDialogClose}
          onReserve={handleReserve}
        />
      )}
    </>
  );
};