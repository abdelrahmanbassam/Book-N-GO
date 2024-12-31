import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FormInput } from "../components/FormInput";
import styles from "./SelectRole.module.css";

export const SelectRole = () => {
    const [role, setRole] = useState("");
    const [phone, setPhone] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const token = new URLSearchParams(window.location.search).get("token");
    localStorage.setItem("token", token);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!role || !phone || !password) {
            setError("Please fill in all fields");
            return;
        }

        try {
            const response = await fetch(
                "http://localhost:8080/auth/update-role",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`,
                    },
                    body: JSON.stringify({ role, phone, password }),
                }
            );

            if (!response.ok) {
                setError("Failed to update role");
                return;
            }

            if (role === "ADMIN") {
                window.location.href = "http://localhost:8080/admin";
            } else if (role === "PROVIDER") {
                navigate("/myWorkspaces");
            } else if (role === "CLIENT") {
                navigate("/hallsList");
            }
        } catch (err) {
            setError("An error occurred. Please try again.");
            navigate("/login");
        }
    };

    return (
        <div>
            <div className={styles.container}>
                <div className={styles.decorativeShape} />

                <div className={styles.formContainer}>
                    <h1 className={styles.title}>Select Your Role</h1>
                    {error && (
                        <div className={styles.errorContainer}>
                            <p className={styles.errorText}>{error}</p>
                        </div>
                    )}
                    <form onSubmit={handleSubmit}>
                        <div className={styles.radioGroup}>
                            <label className={styles.radioLabel}>
                                <input
                                    type="radio"
                                    name="role"
                                    value="CLIENT"
                                    checked={role === "CLIENT"}
                                    onChange={(e) => setRole(e.target.value)}
                                />
                                CLIENT
                            </label>
                            <label className={styles.radioLabel}>
                                <input
                                    type="radio"
                                    name="role"
                                    value="PROVIDER"
                                    checked={role === "PROVIDER"}
                                    onChange={(e) => setRole(e.target.value)}
                                />
                                PROVIDER
                            </label>
                        </div>
                        <FormInput
                            type="tel"
                            name="phone"
                            placeholder="Phone Number"
                            value={phone}
                            onChange={(e) => setPhone(e.target.value)}
                            required
                        />
                        <FormInput
                            type="password"
                            name="password"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                        <div className={styles.formButtons}>
                            <button type="submit" className={styles.button}>
                                Submit
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};