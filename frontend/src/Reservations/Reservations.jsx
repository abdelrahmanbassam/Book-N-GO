import { useState, useEffect } from 'react';
import { Header } from '../components/Header';
import {ReservationCard} from "./components/ReservationCard";
import {StatusFilterMenu} from "./components/StatusFilterMenu";

export const Reservations = () => {
    const Status = Object.freeze({
        PENDING: 'PENDING',
        CONFIRMED: 'CONFIRMED',
        REJECTED: 'REJECTED'
    });
    const role = JSON.parse(localStorage.getItem('role'));
    const [selectedFilter, setSelectedFilter] = useState('all');
    const [isProvider, setIsProvider] = useState(role === 'PROVIDER');
    const [reservations, setReservations] = useState([]);
    //fetching reservations
    const fetchReservations = async () => {
        const token = window.localStorage.getItem("token");
        try {
            const response = await fetch('http://localhost:8080/reservations/all', {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json'

                    }
                });
            const data = await response.json();
            console.log('Reservations fetched:', data);
            setReservations(data);
        } catch (error) {
            console.error('Error fetching reservations:', error);
        }
    };
    useEffect(() => {
        fetchReservations();
    }, []);

    //fetching update status
    const fetchUpdateStatus = async (reservationId, action) => {
        try {
            const response = await fetch(`http://localhost:8080/reservations/updateStatus`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    bookingId: reservationId,
                    status: action
                })
            });
            if (response.ok) {
                console.log('Reservation accepted successfully');
            } else if (response.status === 400) {
                console.error('Bad request');
            } else {
                console.error('Server error:', response.statusText);
            }
        } catch (error) {
            console.error('An error occurred:', error);
        }

    }

    // fetch get reservation by status
    const fetchGetReservationByStatus = async (status) => {
        const token = window.localStorage.getItem("token");
        try {
            const response = await fetch(`http://localhost:8080/reservations/status/${status}`,{
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'

                }
            });
            const data = await response.json();
            console.log(response)
            console.log('Reservations fetched:', data);
            setReservations(data);
        } catch (error) {
            console.error('Error fetching reservations:', error);
        }
    };
    // set reservation status
    function setReservationStatus(reservationId, status) {
        const updatedReservations = reservations.map(reservation => {
            if (reservation.id === reservationId) {
                console.log('Reservation status updated:', reservationId, status);
                return {...reservation, status};
            }
            return reservation;
        });
        setReservations(updatedReservations);
        fetchUpdateStatus(reservationId, status);
    }
    // on filter change
    function onFilterChange(filter) {
        console.log('Filter changed:', filter);
        setSelectedFilter(filter);
        if (filter === 'all') {
            fetchReservations();
        } else {
            fetchGetReservationByStatus(filter.toUpperCase());
        }
    }

    return (
        <div className="min-h-[100vh] bg-primary">
            <Header searchBar={true} />
            <div className="flex flex-col md:mx-10 mx-4 py-4">
                <h1 className="text-3xl text-white my-8">Reservations</h1>
                <StatusFilterMenu  availableFilters={['all', 'pending', 'confirmed', 'rejected', 'canceled']}  onFilterChange={(filter) => onFilterChange(filter)} selectedFilter={selectedFilter} />
                <div className="flex flex-col w-full ">
                    {reservations.map((reservation, index) => (
                        <ReservationCard
                            key={index}
                            reservation={reservation}
                            reservationId={reservation.id}
                            isProvider={isProvider}
                            onAccept={() =>  setReservationStatus(reservation.id, 'CONFIRMED')}
                            onReject={() => setReservationStatus(reservation.id, 'REJECTED')}
                            onCancel={() => setReservationStatus(reservation.id, 'CANCELED')}
                        />
                    ))}
                </div>
            </div>
        </div>
    );
};
