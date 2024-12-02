import React, { useState } from 'react';
import { Logo } from '../components/Logo';
import { HeaderButtons } from '../components/HeaderButtons';
import { FormInput } from '../components/FormInput';
import styles from './Login.module.css';

export const Login = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Login form submitted:', formData);
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
        <h1 className={styles.title}>LOGIN</h1>
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
            type="password"
            name="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            required
          />
          
          <a href="/forgot-password" className={styles.forgotPassword}>
            Forgot Password?
          </a>

          <button type="submit" className={styles.button}>
            LOGIN
          </button>
        </form>
      </div>
    </div>
  );
};