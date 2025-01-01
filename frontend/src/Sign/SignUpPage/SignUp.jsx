import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Header } from "../../components/Header";
import { FormInput } from "../components/FormInput";
import styles from "./SignUp.module.css";

export const SignUp = () => {
    const [formData, setFormData] = useState({
        username: "",
        email: "",
        phone: "",
        password: "",
        accountType: "CLIENT",
    });
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        // Check if the password is too short
        if (formData.password.length < 6) {
            setError("Password must be at least 6 characters long");
            return;
        }
        try {
            const body = {
                email: formData.email,
                password: formData.password,
                phone: formData.phone,
                name: formData.username,
                role: formData.accountType,
            };

            const response = await fetch("http://localhost:8080/auth/signup", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(body),
            });

            if (!response.ok) {
                setError("Username already exists");
                return;
            }

            setError("");
            navigate("/login");
        } catch (err) {
            setError("An error occurred. Please try again.");
        }
    };

    const handleChange = (e) => {
        setError("");
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    };

    const handleGoogleSignIn = () => {
        window.location.href =
            "http://localhost:8080/oauth2/authorization/google";
    };

    return (
        <>
            <Header />
            <div className={styles.container}>
                <div className={styles.formContainer}>
                    <h1 className={styles.title}>SIGN UP!</h1>
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
                            type="email"
                            name="email"
                            placeholder="Email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                        />
                        <FormInput
                            type="tel"
                            name="phone"
                            placeholder="Phone Number"
                            value={formData.phone}
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
                                    value="CLIENT"
                                    checked={formData.accountType === "CLIENT"}
                                    onChange={handleChange}
                                />
                                CLIENT
                            </label>
                            <label className={styles.radioLabel}>
                                <input
                                    type="radio"
                                    name="accountType"
                                    value="PROVIDER"
                                    checked={
                                        formData.accountType === "PROVIDER"
                                    }
                                    onChange={handleChange}
                                />
                                PROVIDER
                            </label>
                        </div>

                        <div className={styles.formButtons}>
                            <button type="submit" className={styles.button}>
                                SIGN UP
                            </button>
                            <button
                                className={`${styles.button}`}
                                onClick={handleGoogleSignIn}
                            >
                                GOOGLE SIGN UP
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </>
    );
};
