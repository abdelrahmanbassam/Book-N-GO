// import React from 'react';
import React, { useState, useEffect } from 'react';

import AmenitiesFilter from './AmenitiesFilter';
import RatingFilter from './RatingFilter';
import { useHalls } from '../context/HallContext';
import styles from './Filters.module.css';

const Filters = () => {
  const { filters, updateFilters, fetchData, resetFilter ,updatePage} = useHalls();

  const handleApplyFilters = () => {
    updatePage(1);
    fetchData();
  };
  const handleRestFilters = () => {
    updatePage(1);
    resetFilter();
    fetchData();
  };
  return (
    <div className={styles.container}>
      <AmenitiesFilter
        selected={filters.aminities}
        onChange={(aminities) => updateFilters({ aminities })}
      />
      {/* <SizeFilter 
        min={filters.minSize}
        max={filters.maxSize}
        onChange={(size) => updateFilters(size)}
      /> */}
      <RatingFilter
        rating={filters.rating}
        onChange={(rating) => updateFilters({ rating })}
      />
      <button
        onClick={handleApplyFilters}
        className={styles.applyButton}
      >
        Apply
      </button>
      <button
        onClick={handleRestFilters}
        className={styles.applyButton}
      >
        Reset
      </button>

    </div>
  );
};

export default Filters;