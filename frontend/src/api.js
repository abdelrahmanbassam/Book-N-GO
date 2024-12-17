import client from "./client";

// Auth
export const login = async (email, password) => {
  const response = await client.post("/login", { email, password });
  window.localStorage.setItem("token", response.data.token);
  return response.data;
}

// Hall
export const createHall = async (workspaceId, hall) => {
  const response = await client.post(`workspace/${workspaceId}/halls`, hall);
  return response.data;
}


// Workspace

