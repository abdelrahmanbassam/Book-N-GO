import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import styles from './HeaderButtons.module.css';

export const HeaderButtons = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const isLoginPage = location.pathname === '/login';
  const isSignUpPage = location.pathname === '/signup';

  // console.log('Current Path:', location.pathname);
  // console.log('Is Login Page:', isLoginPage);
  // console.log('Is Sign Up Page:', isSignUpPage);

  return (
    <div className={styles.headerButtons}>
      <button 
        className={`${styles.headerButton} ${styles.loginBtn}`}
        onClick={() => navigate('/login')}
        disabled={isLoginPage}
      >
        LOGIN
      </button>
      <button 
        className={`${styles.headerButton} ${styles.signUpBtn}`}
        onClick={() => navigate('/signup')}
        disabled={isSignUpPage}
      >
        SIGN UP
      </button>
    </div>
  );
};