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
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Simulating an API call
      const response = await mockLoginAPI(formData);
      if (!response.success) {
        setError('Username or password is incorrect');
        return;
      }
      console.log('Login successful:', response.data);
      setError('');
    } catch (err) {
      setError('An error occurred. Please try again.');
    }
  };

  const handleChange = (e) => {
    setError(''); // Clear error when user starts typing
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  // Mock API function - replace with actual API call
  const mockLoginAPI = async (data) => {
    // Simulating API delay
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    // Simulate failed login for demo
    if (data.username !== 'demo' || data.password !== 'password') {
      return { success: false };
    }
    
    return { success: true, data: { username: data.username } };
  };

  return (
    <div className={styles.container}>
      <Logo />
      <HeaderButtons />
      <div className={styles.decorativeShape} />
      
      <div className={styles.formContainer}>
        <h1 className={styles.title}>LOGIN</h1>
        <form onSubmit={handleSubmit}>
          {error && (
            <div className={styles.errorContainer}>
              <p className={styles.errorText}>{error}</p>
            </div>
          )}
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