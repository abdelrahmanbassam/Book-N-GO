import React from "react";
import { Star } from "lucide-react";

const WorkspaceCard = ({ name, rating, location, imageUrl, onClick }) => {
    return (
        <div
            onClick={onClick}
            className="bg-primary rounded-lg overflow-hidden shadow-lg hover:shadow-xl transition-shadow duration-300 cursor-pointer border border-white/10"
        >
            <div className="relative h-48">
                <img
                    src={imageUrl}
                    alt={name}
                    className="w-full h-full object-cover"
                />
            </div>
            <div className="p-4">
                <h2 className="text-2xl font-bold text-white mb-4">{name}</h2>
                <div className="flex items-center mb-3">
                    {[...Array(5)].map((_, i) => (
                        <Star
                            key={i}
                            className={`w-5 h-5 ${
                                i < rating
                                    ? "fill-yellow-400 text-yellow-400"
                                    : "text-gray-600"
                            }`}
                        />
                    ))}
                </div>
                <p className="text-gray-400">{`${location.departmentNumber}, ${location.street}, ${location.city}`}</p>
            </div>
        </div>
    );
};

export default WorkspaceCard;
