import client from "./client";

// Auth
export const login = async (email, password) => {
  const response = await client.post("/login", { email, password });
  return response.data;
}

// Hall

// Workspace

