import React, { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { info, login } from "../../api";
import { Header } from "../../components/Header";
import { UserContext } from "../../UserContext";
import { FormInput } from "../components/FormInput";
import styles from "./Login.module.css";

export const Login = () => {
    const [formData, setFormData] = useState({
        email: "",
        password: "",
    });
    const [error, setError] = useState("");
    const { user, setUser } = useContext(UserContext);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const data = await login(formData.email, formData.password);

            if (!data.token) {
                setError(data.message || "Email or password is incorrect");
                return;
            }

            localStorage.setItem("token", data.token);

            console.log("Login successful:", data);

            const user = await info();
            setUser(user);

            console.log("User data:", user);

            if (user.role === "ADMIN") {
                window.location.href = "http://localhost:8080/admin";
            } else if (user.role === "PROVIDER") {
                navigate("/myWorkspaces");
            } else if (user.role === "CLIENT") {
                navigate("/hallsList");
            }
        } catch (err) {
            setError("An error occurred. Please try again.");
        }
    };

    const handleChange = (e) => {
        setError(""); // Clear error when user starts typing
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

                        {/* <a
                            href="/forgot-password"
                            className={styles.forgotPassword}
                        >
                            Forgot Password?
                        </a> */}

                        <div className={styles.formButtons}>
                            <button type="submit" className={styles.button}>
                                LOG IN
                            </button>
                            <button
                                className={`${styles.button} ${styles.googleSignInButton}`}
                                onClick={handleGoogleSignIn}
                            >
                                GOOGLE LOG IN
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </>
    );
};
