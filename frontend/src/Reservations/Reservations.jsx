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
    const [selectedFilter, setSelectedFilter] = useState('all');
    const [reservations, setReservations] = useState([
        {
            id: 1,
            hall: 'hall 21',
            clientName: 'John Doe',
            status: 'pending',
            date: '2022-10-10',
            startTime: '10:00 AM',
            endTime: '11:00 AM',
            notes: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam nec rhoncus mi.'
        },
        {
            id: 2,
            hall: 'hall 22',
            clientName: 'Jane Doe',
            status: 'pending',
            date: '2022-10-11',
            startTime: '11:00 AM',
            endTime: '12:00 PM',
            notes: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam nec rhoncus mi.'
        }
    ]);
    //fetching reservations
    const fetchReservations = async () => {
        try {
            const response = await fetch('http://localhost:8080/reservations/all');
            const data = await response.json();
            console.log(response)
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
        try {
            const response = await fetch(`http://localhost:8080/reservations/status/${status}`);
            const data = await response.json();
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
                            isProvider={true}
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
