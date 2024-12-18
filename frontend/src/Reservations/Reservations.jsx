import { useState, useEffect } from 'react';
import { Header } from '../components/Header';
import {ReservationCard} from "./components/ReservationCard";
import {StatusFilterMenu} from "./components/StatusFilterMenu";

export const Reservations = () => {
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
            const response = await fetch('http://localhost:8080/reservations/client/1');
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
    const fetchUpdateStatus = async (reservationId, status) => {
        const action = status.toLocaleLowerCase().slice(0,-2)
        try {
            const response = await fetch(`http://localhost:8080/reservations/${action}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ reservationId })
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
        if (filter === 'all') {
            fetchReservations()
        } else {
            setReservations(reservations.filter(reservation => reservation.status === filter.toUpperCase()));
        }
        setSelectedFilter(filter);
    }

    return (
        <div className="min-h-[100vh] bg-primary">
            {/*<Header searchBar={true} />*/}
            <div className="flex flex-col md:mx-10 mx-4">
                <h1 className="text-3xl text-white my-8">Reservations</h1>
                <StatusFilterMenu  availableFilters={['all', 'pending', 'accepted', 'rejected', 'canceled']}  onFilterChange={(filter) => onFilterChange(filter)} selectedFilter={selectedFilter} />
                <div className="flex flex-col w-full ">
                    {reservations.map((reservation, index) => (
                        <ReservationCard
                            key={index}
                            reservation={reservation}
                            reservationId={reservation.id}
                            isProvider={false}
                            onAccept={() =>  setReservationStatus(reservation.id, 'ACCEPTED')}
                            onReject={() => setReservationStatus(reservation.id, 'REJECTED')}
                            onCancel={() => setReservationStatus(reservation.id, 'CANCELED')}
                        />
                    ))}
                </div>
            </div>
        </div>
    );
};
