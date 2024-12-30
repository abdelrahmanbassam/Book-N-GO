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

      localStorage.setItem('token', data.token);

      console.log('Login successful:', data);

      const user = await fetch('http://localhost:8080/auth/get-user-info', {
        headers: {
          'Authorization': 'Bearer ' + data.token
        }
      });
      const userData = await user.json();

      console.log('User data:', userData);

      if (userData.role === 'ADMIN') {
        navigate('/admin');
      } else if (userData.role === 'PROVIDER') {
        navigate('/hallOwner');
      } else if (userData.role === 'CLIENT') {
        navigate('/hallsList');
      }
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
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
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


          <div className={styles.formButtons}>
            <button type="submit" className={styles.button}>
              LOGIN
            </button>
            <button className={`${styles.button} ${styles.googleSignInButton}`} onClick={handleGoogleSignIn}>
              Log in with Google
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};