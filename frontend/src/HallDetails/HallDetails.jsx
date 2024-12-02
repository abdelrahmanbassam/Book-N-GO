import React from "react";
import image from "../assets/Alexandria-Library.png";
import styles from "./HallDetails.module.css";
import Rating from '@mui/material/Rating';
import StarIcon from '@mui/icons-material/Star';
import { Carousel } from "@material-tailwind/react";

export const HallDetails = () => {
  return (
    <div className={styles["hall-details__wrapper"]}>
      <div className={styles["hall-details__container"]}>
        <div className={styles["hall-details__preview"]}>
          {/* <Carousel className="rounded-xl">
            <img src={image} alt="Hall Preview"/>
          </Carousel> */}
          <h1>Alexandria bibliotheca Great Hall</h1>
          <button>Book Now!</button>
        </div>
        <div className={styles["hall-details__info"]}>
          <div className={styles["hall-details__info__rating"]}>
            <h2>Ratings:</h2>
            <Rating 
              name="hall-rating" 
              value={3} 
              opacity={1} 
              icon={<StarIcon fontSize="inherit" />}
              emptyIcon={<StarIcon fontSize="inherit" sx={{color: "white"}} />}
            />
          </div>
          <div className={styles["hall-details__info__description"]}>
            <h2>Description:</h2>
            <p>
              The Great Hall of the Library of Alexandria in Alexandria, Egypt, was the largest and most famous part of the Library of Alexandria, which was part of the research institution called the Musaeum. The Musaeum was a part of the Royal Library of Alexandria, an institution that was part of the Museum of Alexandria. The Museum was a place of learning in ancient Alexandria, and many great thinkers worked there.
            </p>
          </div>
          <div className={styles["hall-details__info__comments"]}>
            <h2>Comments:</h2>
          </div>
        </div>
      </div>
    </div>
  );
};
