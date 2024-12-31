import React from 'react';
import { Clock, Calendar, User, FileText } from 'lucide-react';
import PropTypes from "prop-types";
import {HallCard} from "../../WorkSpace/components/HallCard";
import {TimeDisplay} from "./TimeDisplay";
import {Button} from "@mui/material";



export const ReservationCard =(props) =>{
    const statusColors = {
        PENDING: 'bg-yellow-100 text-yellow-800',
        CONFIRMED: 'bg-green-100 text-green-800',
        REJECTED: 'bg-red-100 text-red-800',
        CANCELED: 'bg-gray-100 text-gray-800',
    };

    return (
        <div className="bg-white rounded-lg shadow-md p-6  my-2">
            <div className="flex justify-between items-start mb-4">
                <div>
                    <h3 className="text-lg font-semibold text-primary">{props.reservation.hallName}</h3>
                    <div className="flex items-center text-gray-600 mt-2">
                        <User size={18} className="mr-2" />
                        <span>{props.reservation.clientName}</span>
                    </div>
                </div>
                <span className={`px-3 py-1 rounded-full text-sm font-medium ${statusColors[props.reservation.status.toUpperCase()]}`}>
                    {props.reservation.status.charAt(0) + props.reservation.status.slice(1).toLowerCase()}
        </span>
            </div>
            <div className="grid grid-cols-2 gap-4 mb-4">
                <div className="flex items-center text-gray-600">
                    <Calendar size={18} className="mr-2" />
                    <span>{ new Date(props.reservation.startTime).toLocaleDateString()}</span>
                </div>
                <TimeDisplay startTime={props.reservation.startTime} endTime={props.reservation.endTime} />
            </div>

            {props.reservation.hallDescription && (
                <div className="flex items-start text-gray-600 mb-4">
                    <FileText size={18} className="mr-2 mt-1" />
                    <p className="text-sm">{props.reservation.hallDescription}</p>
                </div>
            )}
            {props.isProvider && props.reservation.status === 'PENDING' && (
                <div className="flex space-x-3 mt-4">
                    <button
                        onClick={props.onAccept}
                        className="flex-1 bg-green-600 text-white py-2 px-4 rounded-md hover:bg-green-700 transition-colors"
                    >
                        Accept
                    </button>
                    <button
                        onClick={props.onReject}
                        className="flex-1 bg-red-600 text-white py-2 px-4 rounded-md hover:bg-red-700 transition-colors"
                    >
                        Reject
                    </button>
                </div>
            )}
            {!props.isProvider && props.reservation.status === 'PENDING' && (
                <div className="flex space-x-3 mt-4">
                    <button
                        onClick={props.onCancel}
                        className="flex-1 bg-red-600 text-white py-2 px-4 rounded-md hover:bg-red-700 transition-colors"
                    >
                        Cancel
                    </button>
                </div>
            )}
        </div>
    );
}
ReservationCard.propTypes = {
    reservation: PropTypes.object,
    isProvider: PropTypes.bool,
    onAccept: PropTypes.func,
    onReject: PropTypes.func,
}