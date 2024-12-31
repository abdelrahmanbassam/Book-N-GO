import client from "./client";

// Auth
export const login = async (email, password) => {
    const response = await client.post("/auth/login", { email, password });
    window.localStorage.setItem("token", response.data.token);
    console.log("TOkennnnnnnnnnnnnn");
    console.log(response.data.token);
    console.log(window.localStorage.getItem("token"));
    return response.data;
};

export const info = async () => {
    const response = await client.get("/auth/get-user-info");
    return response.data;
};

// Hall
export const createHall = async (workspaceId, hall) => {
    const response = await client.post(`workspace/${workspaceId}/halls`, hall);
    return response.data;
};
export const fetchHalls = async (requestBody) => {
    try {
        const response = await client.post("/filterHalls", requestBody);
        return response.data;
    } catch (error) {
        console.error("Failed to fetch halls:", error);
        throw error;
    }
};

export const getHallData = async (workspaceId, id) => {
    const response = await client.get(`/workspace/${workspaceId}/halls/${id}`);
    return response.data;
};

export const getAminities = async () => {
    const response = await client.get("/aminities");
    return response.data;
}

// Workspace
export const getProviderWorkspaces = async () => {
    const response = await client.get("/workspaces/provider");
    console.log(response.data);
    return response.data;
};

export const createWorkspace = async (workspace) => {
    const response = await client.post("/workspaces", workspace);
    return response.data;
};

export const updateWorkspaceWorkdays = async (workspaceId, workdays) => {
    const response = await client.put(
        `/workspace/${workspaceId}/workdays`,
        workdays
    );
    return response.data;
};

// Booking
export const schedules = async (id, startDateTime) => {
    const response = await client.get(`/bookings/hall/${id}/schedule`, {
        params: { startTime: startDateTime },
    });
    return response.data;
};
export const availability = async (id, startDateTime) => {
    const response = await client.get(`/bookings/hall/${id}/availability`, {
        params: { startTime: startDateTime },
    });
    return response.data;
};
