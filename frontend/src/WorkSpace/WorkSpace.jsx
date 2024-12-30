import { useState, useEffect } from "react";
import { Header } from "../components/Header";
import { HallCard } from "./components/HallCard";
import Rating from "@mui/material/Rating";
import { useLocation, useParams, useNavigate } from "react-router-dom";
import EditWorkspaceDialog from "./components/EditWorkspaceDialog";
import HallImage from "../assets/Alexandria-Library.png";
import HallDialog from "./components/HallDialog";
import { is } from "date-fns/locale";
export const WorkSpace = () => {
    const { workspaceId } = useParams(); // Get the workspace ID from the URL params
    const location = useLocation();
    const navigate = useNavigate(); // Initialize useNavigate hook for redirection

    const [workspaceName, setWorkspaceName] = useState("Workspace Name");
    const [workspaceRating, setWorkspaceRating] = useState(4.2);
    const [workspaceDescription, setWorkspaceDescription] = useState(
        "Workspace Description"
    );
    const [profilePic, setProfilePic] = useState("");
    const [workspaceLocation, setWorkspaceLocation] = useState("");
    const [hallCards, setHallCards] = useState([]);
    const [workdays, setWorkdays] = useState([]);
    const [openEditDialog, setOpenEditDialog] = useState(false); // State for dialog visibility
    const [workspaceData, setWorkspaceData] = useState(null); // State to store fetched workspace data
    const [isProvider, setIsProvider] = useState(false);
    const [openNewDialog, setOpenNewDialog] = useState(false);
    const getFetchHeaders = () => {
        return {
            Authorization: `Bearer ${window.localStorage.getItem("token")}`,
            "Content-Type": "application/json",
        };
    };
    // Fetch workspace details and hall cards
    useEffect(() => {
        const isWorkspaceProvider = async () => {
            try {
                const token = window.localStorage.getItem("token");
                const response = await fetch(
                    `http://localhost:8080/workspaces/${workspaceId}/provider`,
                    {
                        headers: getFetchHeaders(),
                    }
                );
                const data = await response.json();
                setIsProvider(data);
                console.log("isProvider", data);
            } catch (error) {
                console.error("Error fetching provider data:", error);
            }
        };

        const fetchWorkspaceDetails = async () => {
            try {
                console.log(window.localStorage.getItem("token"));
                const token = window.localStorage.getItem("token");
                const response = await fetch(
                    `http://localhost:8080/workspaces/${workspaceId}`,
                    {
                        headers: getFetchHeaders(),
                    }
                );
                const data = await response.json();
                setWorkspaceName(data.name);
                setWorkspaceRating(data.rating);
                setWorkspaceDescription(data.description);
                setWorkspaceLocation(
                    `${data.location.departmentNumber}, ${data.location.street}, ${data.location.city}`
                );
                setProfilePic(data.profilePic);
                setWorkspaceData(data); // Store workspace data for editing
            } catch (error) {
                console.error("Error fetching workspace details:", error);
            }
        };

        const fetchHallCards = async () => {
            try {
                const token = window.localStorage.getItem("token");
                const response = await fetch(
                    `http://localhost:8080/workspace/${workspaceId}/halls`,
                    {
                        headers: getFetchHeaders(),
                    }
                );
                const data = await response.json();
                setHallCards(
                    data.map((hall) => ({
                        id: hall.id,
                        image: hall.image,
                        name: hall.name,
                        stars: hall.rating, // Assuming hall data includes a rating field
                        rate: hall.ratePerHour, // Assuming your backend provides ratePerHour for the hall
                    }))
                );
            } catch (error) {
                console.error("Error fetching hall cards:", error);
            }
        };

        const fetchWorkdays = async () => {
            try {
                const response = await fetch(
                    `http://localhost:8080/workspace/${workspaceId}/workdays`,
                    {
                        headers: getFetchHeaders(),
                    }
                );
                const data = await response.json();
                // sort workdays by week day
                const days = [
                    "SATURDAY",
                    "SUNDAY",
                    "MONDAY",
                    "TUESDAY",
                    "WEDNESDAY",
                    "THURSDAY",
                    "FRIDAY",
                ];
                data.sort(
                    (a, b) => days.indexOf(a.weekDay) - days.indexOf(b.weekDay)
                );
                data.forEach((workday) => delete workday.workspace);
                setWorkdays(data);
            } catch (error) {
                console.error("Error fetching workdays:", error);
            }
        };
        isWorkspaceProvider();
        fetchWorkspaceDetails();
        fetchHallCards();
        fetchWorkdays();
    }, [workspaceId]);

    // Open the edit dialog
    const handleEditClick = () => {
        setOpenEditDialog(true);
    };

    // Close the edit dialog
    const handleCloseDialog = () => {
        setOpenEditDialog(false);
    };

    // Handle form submission to update workspace
    const handleSubmitEdit = async (updatedData, updatedWorkdays) => {
        try {
            const updatedWorkspace = {
                ...workspaceData,
                name: updatedData.name,
                description: updatedData.description,
                location: updatedData.location,
            };
            // Remove provider field from workspace data
            delete updatedWorkspace.provider;
            console.log(updatedWorkdays);
            // Update workspace data on the server
            const response = await fetch(
                `http://localhost:8080/workspaces/${workspaceId}`,
                {
                    method: "PUT",
                    headers: getFetchHeaders(),
                    body: JSON.stringify(updatedWorkspace),
                }
            );

            if (response.ok) {
                const data = await response.json();
                setWorkspaceData(data);
                setWorkspaceName(data.name);
                setWorkspaceRating(data.rating);
                setWorkspaceDescription(data.description);
                setWorkspaceLocation(
                    `${data.location.departmentNumber}, ${data.location.street}, ${data.location.city}`
                );
                setOpenEditDialog(false); // Close dialog after successful update
            }

            const response2 = await fetch(
                `http://localhost:8080/workspace/${workspaceId}/workdays`,
                {
                    method: "PUT",
                    headers: getFetchHeaders(),
                    body: JSON.stringify(updatedWorkdays),
                }
            );

            console.log(JSON.stringify(updatedWorkdays));

            if (response2.ok) {
                const data = await response2.json();
                // sort workdays by week day
                const days = [
                    "SATURDAY",
                    "SUNDAY",
                    "MONDAY",
                    "TUESDAY",
                    "WEDNESDAY",
                    "THURSDAY",
                    "FRIDAY",
                ];
                data.sort(
                    (a, b) => days.indexOf(a.weekDay) - days.indexOf(b.weekDay)
                );
                data.forEach((workday) => delete workday.workspace);
                setWorkdays(data);
            }
        } catch (error) {
            console.error("Error updating workspace:", error);
        }
    };

    return (
        <div className="min-h-[100vh] bg-primary">
            <Header searchBar={true} />
            <div className="py-10 md:px-10 px-4">
                <div className="flex justify-between md:flex-row flex-col">
                    {/* Profile Pic */}
                    <div className="lg:basis-1/4 basis-1/4 aspect-square md:rounded-full flex justify-center items-center">
                        <img
                            className="w-full object-cover md:rounded-full aspect-square"
                            src={profilePic || "/assets/WorkSpace.jpg"}
                            alt="profile_pic"
                        />
                    </div>
                    {/* Workspace Info */}
                    <div className="flex flex-col lg:basis-4/6 text-white my-4 gap-2">
                        <div className="md:text-6xl text-4xl font-bold">
                            {workspaceName}
                        </div>
                        <div className="text-lg flex gap-10 font-bold">
                            Rating:{" "}
                            <Rating
                                name="read-only"
                                value={Math.round(workspaceRating)}
                                readOnly
                            />
                        </div>
                        <div className="text-lg flex gap-10">
                            <strong>Location: </strong>
                            <p className="md:text-base text-sm">
                                {workspaceLocation}
                            </p>
                        </div>
                        <div>
                            <h1 className="text-lg font-bold">Description:</h1>
                            <p className="md:text-base text-sm pl-10">
                                {workspaceDescription}
                            </p>
                        </div>
                        {/* Workdays Section */}
                        <div>
                            <h1 className="text-lg font-bold">Workdays:</h1>
                            <div className="text-sm pl-10">
                                {workdays.length === 0 ? (
                                    <div>
                                        No workdays available for this
                                        workspace.
                                    </div>
                                ) : (
                                    workdays.map((workday) => (
                                        <div
                                            key={workday.id}
                                            className="rounded-lg my-2 text-white flex justify-between items-center w-2/3  p-2 border border-white border-opacity-40"
                                        >
                                            <div>
                                                <span className="font-bold">
                                                    {workday.weekDay}
                                                </span>
                                            </div>
                                            <div>
                                                <span className="text-lg">
                                                    {new Date(
                                                        `1970-01-01T${workday.startTime}`
                                                    ).toLocaleTimeString([], {
                                                        hour: "2-digit",
                                                        minute: "2-digit",
                                                    })}{" "}
                                                    -{" "}
                                                    {new Date(
                                                        `1970-01-01T${workday.endTime}`
                                                    ).toLocaleTimeString([], {
                                                        hour: "2-digit",
                                                        minute: "2-digit",
                                                    })}
                                                </span>
                                            </div>
                                        </div>
                                    ))
                                )}
                            </div>
                        </div>
                    </div>
                    {/* Edit Button */}
                    <div className="flex justify-end">
                        {isProvider && (
                            <div
                                className="flex my-4 justify-center items-center md:w-12 w-full h-12 cursor-pointer md:rounded-full bg-secondary1"
                                onClick={handleEditClick} // Open the dialog
                            >
                                <img
                                    className="h-8 w-8 object-cover rounded"
                                    src="/assets/Edit03.png"
                                    alt="edit_icon"
                                />
                            </div>
                        )}
                    </div>
                </div>

                {/* Hall Cards */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-10">
                    {hallCards.map((hall) => (
                        <div
                            key={hall.id}
                            className="relative transition-transform duration-300 transform hover:scale-105 cursor-pointer"
                            onClick={() => navigate(`/hall/${hall.id}`)}
                        >
                            <HallCard
                                image={hall.image}
                                Name={hall.name}
                                stars={hall.stars}
                            />
                        </div>
                    ))}

                    {/* Add Hall Button */}
                    {isProvider && (
                        <div className="flex flex-col min-h-[300px] border-secondary2 hover:scale-110 transition-transform duration-300 cursor-pointer">
                            <div className="text-white border-secondary2 border-2 border-dashed flex justify-center items-center h-full hover:border-solid">
                                <div className="w-[50%] aspect-square border-2 border-dashed rounded-full flex justify-center items-center hover:border-solid border-secondary2">
                                    <img
                                        className="p-10 cursor-pointer hover:scale-110 transition-transform duration-300"
                                        src="/assets/plus.png"
                                        alt="plus_icon"
                                        onClick={() => setOpenNewDialog(true)} // Open the dialog
                                    />
                                </div>
                            </div>
                        </div>
                    )}
                </div>
            </div>
            {/* Add Hall Dialog */}
            <HallDialog
                open={openNewDialog}
                setOpen={setOpenNewDialog}
                workspaceId={workspaceId}
            />
            {/* Edit Workspace Dialog */}
            {workspaceData && (
                <EditWorkspaceDialog
                    open={openEditDialog}
                    onClose={handleCloseDialog}
                    onSave={handleSubmitEdit} // Passing 'handleSubmitEdit' as 'onSave'
                    workspaceData={workspaceData}
                    workdays={workdays}
                />
            )}
        </div>
    );
};
