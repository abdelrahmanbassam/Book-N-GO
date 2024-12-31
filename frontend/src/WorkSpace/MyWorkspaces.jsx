import React from "react";
import { Building2 } from "lucide-react";
import WorkspaceCard from "./components/WorkspaceCard";
import CreateWorkspaceCard from "./components/CreateWorkspaceCard";
import { Header } from "../components/Header";
import { getProviderWorkspaces } from "../api";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import WorkspaceDialog from "./components/WorkspaceDialog";
import { createWorkspace, updateWorkspaceWorkdays } from "../api";
import { set } from "date-fns";

export const MyWorkspaces = () => {
    const [workspaces, setWorkspaces] = useState([]);
    const [showDialog, setShowDialog] = useState(false);
    const navigate = useNavigate();
    useEffect(() => {
        getProviderWorkspaces()
            .then((data) => setWorkspaces(data))
            .catch((err) => console.error(err));
    }, []);

    const handleCreateWorkspace = () => {
        setShowDialog(true);
        console.log("Create workspace");
    };

    const handleSaveNewWorkspace = (newWorkspace, workdays) => {
        createWorkspace(newWorkspace)
            .then((data) => {
                console.log("Workspace created", data);
                newWorkspace = data;
                // console.log("Workdays", workdays);
                if (workdays.length > 0) {
                    const adjustedWorkdays = workdays.map((workday) => ({
                        ...workday,
                        startTime: `2024-01-01T${workday.startTime}`,
                        endTime: `2024-01-01T${workday.endTime}`,
                    }));
                    updateWorkspaceWorkdays(newWorkspace.id, adjustedWorkdays)
                        .then((data) => console.log("Workdays updated", data))
                        .catch((err) => console.error(err));
                }
                setWorkspaces([...workspaces, newWorkspace]);
            })
            .catch((err) => console.error(err));
        setShowDialog(false);
    };
    return (
        <div className="min-h-screen bg-primary text-white">
            <Header searchBar={true} />
            <div className="container mx-auto px-4 py-8">
                <div className="flex items-center gap-3 mb-8">
                    <Building2 className="w-8 h-8" />
                    <h1 className="text-3xl font-bold">Your Workspaces</h1>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                    <CreateWorkspaceCard onClick={handleCreateWorkspace} />
                    {workspaces.length === 0 && (
                        <div className="text-center text-lg col-span-4">
                            You don't have any workspaces yet
                        </div>
                    )}
                    {workspaces.length > 0 &&
                        workspaces.map((workspace) => (
                            <WorkspaceCard
                                key={workspace.id}
                                name={workspace.name}
                                rating={workspace.rating}
                                location={workspace.location}
                                imageUrl={
                                    workspace.imageUrl ||
                                    "/assets/WorkSpace.jpg"
                                }
                                onClick={() =>
                                    navigate(`/workspace/${workspace.id}`)
                                }
                            />
                        ))}
                </div>
            </div>
            <WorkspaceDialog
                open={showDialog}
                onClose={() => setShowDialog(false)}
                onSave={handleSaveNewWorkspace}
                title="Create a new workspace"
                workspaceData={{
                    name: "",
                    description: "",
                    location: {
                        departmentNumber: "",
                        street: "",
                        city: "",
                    },
                }}
                workdays={[]}
            />
        </div>
    );
};
