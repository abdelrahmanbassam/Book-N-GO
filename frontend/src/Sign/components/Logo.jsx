import React from 'react';
import styles from './Logo.module.css';
import logoImage from '../../assets/Logo.png'; // Adjust the path as needed

export const Logo = () => (
  <div className={styles.Logo}>
    <img src={logoImage} alt="Logo" className={styles.logoImage} />
    {/* <span className={styles.logoText}>Book'nGo</span> */}
  </div>
);
