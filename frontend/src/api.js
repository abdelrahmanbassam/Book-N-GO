import client from "./client";

// Auth
export const login = async (email, password) => {
  const response = await client.post("/auth/login", { email, password });
  window.localStorage.setItem("token", response.data.token);
  console.log("TOkennnnnnnnnnnnnn");
  console.log(response.data.token);
  console.log(window.localStorage.getItem('token'));
  return response.data;
}

// Hall
export const getHallData = async (workspaceId, id) => {
  const response = await client.get(`/workspace/${workspaceId}/halls/${id}`);
  return response.data;
}

// Workspace


// Booking
export const schedules = async (id) => {
  const response = await client.get(`/bookings/hall/schedule/${id}`);
  return response.data;
}