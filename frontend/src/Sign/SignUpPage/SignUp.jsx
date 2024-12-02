import React, { useState } from 'react';
import { Logo } from '../components/Logo';
import { HeaderButtons } from '../components/HeaderButtons';
import { FormInput } from '../components/FormInput';
import styles from './SignUp.module.css';

export const SignUp = () => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    phoneNumber: '',
    password: '',
    accountType: 'customer'
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('SignUp form submitted:', formData);
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  return (
    <div className={styles.container}>
      <Logo />
      <HeaderButtons />
      <div className={styles.decorativeShape} />
      
      <div className={styles.formContainer}>
        <h1 className={styles.title}>SIGN UP!</h1>
        <form onSubmit={handleSubmit}>
          <FormInput
            type="text"
            name="username"
            placeholder="User Name"
            value={formData.username}
            onChange={handleChange}
            required
          />
          <FormInput
            type="email"
            name="email"
            placeholder="Email"
            value={formData.email}
            onChange={handleChange}
            required
          />
          <FormInput
            type="tel"
            name="phoneNumber"
            placeholder="Phone Number"
            value={formData.phoneNumber}
            onChange={handleChange}
            required
          />
          <FormInput
            type="password"
            name="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            required
          />
          
          <div className={styles.radioGroup}>
            <label className={styles.radioLabel}>
              <input
                type="radio"
                name="accountType"
                value="customer"
                checked={formData.accountType === 'customer'}
                onChange={handleChange}
              />
              Customer
            </label>
            <label className={styles.radioLabel}>
              <input
                type="radio"
                name="accountType"
                value="workspace"
                checked={formData.accountType === 'workspace'}
                onChange={handleChange}
              />
              Work Space
            </label>
          </div>

          <button type="submit" className={styles.button}>
            SIGN UP
          </button>
        </form>
      </div>
    </div>
  );
};