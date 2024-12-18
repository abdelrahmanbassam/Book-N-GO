import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FormInput } from '../components/FormInput';
import { HeaderButtons } from '../components/HeaderButtons';
import { Logo } from '../components/Logo';
import { login } from '../../api';
import styles from './Login.module.css';

export const Login = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = await login(formData.email, formData.password);


      if (!data.token) {
        setError(data.message || 'Email or password is incorrect');
        return;
      }  
      window.localStorage.setItem('token', data.token);
      console.log('Login successful:', data);
      setError('');
      navigate('/WorkSpace', { state: { email: formData.email } }); // Navigate with state
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

  const handleGoogleSignIn = () => {
    const accountType = formData.accountType;
    window.location.href = `http://localhost:8080/oauth2/authorization/google`;
  };

  // Mock API function - replace with actual API call
  const mockLoginAPI = async (data) => {
    // Simulating API delay
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    // Simulate failed login for demo
    if (data.email !== 'demo@example.com' || data.password !== 'password') {
      return { success: false };
    }
    
    return { success: true, data: { email: data.email } };
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
            type="email"
            name="email"
            placeholder="Email"
            value={formData.email}
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
            <div className={styles.googleSignInContainer}>
              <button className={`${styles.button} ${styles.googleSignInButton}`} onClick={handleGoogleSignIn}>
                Sign in with Google
              </button>
            </div>
        </form>
      </div>
    </div>
  );
};