export const fetchHalls = async (requestBody) => {
    try {
        const token = window.localStorage.getItem("token");
        const response = await fetch(
            "http://localhost:8080/workspace/1/filterHalls",
            {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(requestBody),
            }
        );

        if (!response.ok) {
            throw new Error("Network response was not ok");
        }
        const data = await response.json();
        // console.log('Fetched halls:', data);

        return data;
    } catch (error) {
        console.error("Failed to fetch halls:", error);
        throw error;
    }
};
