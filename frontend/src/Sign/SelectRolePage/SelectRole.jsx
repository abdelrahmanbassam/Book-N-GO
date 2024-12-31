import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./SelectRole.module.css";

export const SelectRole = () => {
    const [role, setRole] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const token = new URLSearchParams(window.location.search).get("token");
    localStorage.setItem("token", token);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!role) {
            setError("Please select a role");
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
                    body: JSON.stringify({ role }),
                }
            );

            if (!response.ok) {
                setError("Failed to update role");
                return;
            }

            if (role === "ADMIN") {
                window.location.href = "http://localhost:8080/admin";
            } else if (role === "PROVIDER") {
                navigate("/hallOwner");
            } else if (role === "CLIENT") {
                navigate("/hallsList");
            }
        } catch (err) {
            setError("An error occurred. Please try again.");
            navigate("/login");
        }
    };

    return (
        <div className={styles.container}>
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
                <button type="submit" className={styles.button}>
                    Submit
                </button>
            </form>
        </div>
    );
};
