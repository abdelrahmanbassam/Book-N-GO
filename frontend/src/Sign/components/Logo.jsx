import React from 'react';
import { BookIcon } from 'lucide-react';
import styles from './Logo.module.css';

export const Logo = () => (
  <div className={styles.logo}>
    <BookIcon size={24} color="#FF9944" />
    <span className={styles.logoText}>Book'nGo</span>
  </div>
);