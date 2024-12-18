import client from "./client";

// Auth
export const login = async (email, password) => {
  const response = await client.post("/login", { email, password });
  window.localStorage.setItem("token", response.data.token);
  return response.data;
}

// Hall
export const fetchHalls = async (requestBody) => {
  try {
    const response = await client.post("/filterHalls", requestBody);
    return response.data;
  } catch (error) {
    console.error('Failed to fetch halls:', error);
    throw error;
  }
};

// Workspace

