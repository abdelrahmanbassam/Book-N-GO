import React from "react";
import { Building2 } from "lucide-react";
import WorkspaceCard from "./components/WorkspaceCard";
import CreateWorkspaceCard from "./components/CreateWorkspaceCard";
import { Header } from "../components/Header";
import { getProviderWorkspaces } from "../api";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { WorkspaceDialog } from "./components/WorkspaceDialog";
export const MyWorkspaces = () => {
    const [workspaces, setWorkspaces] = useState([]);
    const navigate = useNavigate();
    useEffect(() => {
        getProviderWorkspaces()
            .then((data) => setWorkspaces(data))
            .catch((err) => console.error(err));
    }, []);

    return (
        <div className="min-h-screen bg-primary text-white">
            <Header searchBar={true} />
            <div className="container mx-auto px-4 py-8">
                <div className="flex items-center gap-3 mb-8">
                    <Building2 className="w-8 h-8" />
                    <h1 className="text-3xl font-bold">Your Workspaces</h1>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                    <CreateWorkspaceCard
                        onClick={() => console.log("Creating new workspace")}
                    />
                    {workspaces.map((workspace) => (
                        <WorkspaceCard
                            key={workspace.id}
                            name={workspace.name}
                            rating={workspace.rating}
                            location={workspace.location}
                            imageUrl={
                                workspace.imageUrl || "/assets/WorkSpace.jpg"
                            }
                            onClick={() =>
                                navigate(`/workspace/${workspace.id}`)
                            }
                        />
                    ))}
                </div>
            </div>
        </div>
    );
};
