import React from 'react';
import { Clock } from 'lucide-react';


export function TimeDisplay(timeSlot) {
    const start = new Date(timeSlot.startTime);
    const end = new Date(timeSlot.endTime);

    const duration = calculateDuration(start,end);

    return (
        <div className="flex items-center text-gray-600">
            <Clock size={18} className="mr-2" />
            <div>
                <span className="font-medium">{formatTimeRange(start,end)}</span>
                <span className="text-sm text-gray-500 ml-2">({formatDuration(duration)})</span>
            </div>
        </div>
    );
}

// 12 hours format (AM and PM)
function formatTimeRange(start,end) {
    return `${start.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} - ${end.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`;
}

function calculateDuration(start, end) {
    return (end.getTime() - start.getTime()) / (1000 * 60); // Duration in minutes
}

function formatDuration(minutes) {
    const hours = Math.floor(minutes / 60);
    const remainingMinutes = minutes % 60;

    if (hours === 0) {
        return `${remainingMinutes}min`;
    } else if (remainingMinutes === 0) {
        return `${hours}h`;
    }
    return `${hours}h ${remainingMinutes}min`;
}