import React from 'react';
import styles from './FormInput.module.css';

export const FormInput = ({ type, name, placeholder, value, onChange, required }) => (
  <input
    type={type}
    name={name}
    placeholder={placeholder}
    className={styles.input}
    value={value}
    onChange={onChange}
    required={required}
  />
);