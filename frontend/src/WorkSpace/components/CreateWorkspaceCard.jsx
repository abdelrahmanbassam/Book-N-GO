import React from "react";
import { Plus } from "lucide-react";

const CreateWorkspaceCard = ({ onClick }) => {
    return (
        <div
            onClick={onClick}
            className="bg-primary rounded-lg overflow-hidden border border-white/10 hover:border-white/20 transition-all h-full cursor-pointer flex flex-col items-center justify-center p-8 text-center"
        >
            <Plus className="w-8 h-8 text-white/60 mb-2" />
            <h3 className="text-lg font-medium text-white/80">
                Create New Workspace
            </h3>
            <p className="text-sm text-white/60 mt-1">
                Add your workspace to our community
            </p>
        </div>
    );
};

export default CreateWorkspaceCard;
