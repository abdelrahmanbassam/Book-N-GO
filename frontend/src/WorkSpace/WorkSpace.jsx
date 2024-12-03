import { useState, useEffect } from 'react';
import { colors } from '../components/styles';
import { Header } from '../components/Header';
import { Rate } from "../components/Rate";
import { HallCard } from "./components/HallCard";

export const WorkSpace = () => {
    const [userName, setUserName] = useState('John Doe');
    const [userRating, setUserRating] = useState('4.6');
    const [userDescription, setUserDescription] = useState('I am a professional web developer with 5 years of experience.');
    const [profilePic, setProfilePic] = useState('');
    const [hallCards, setHallCards] = useState([ {
        id: 1,
        image: 'assets/profilePic.png',
        name: 'Hall 1',
        stars: 4,
        rate: 4.5
    },
        {
            id: 2,
            image: 'assets/profilePic.png',
            name: 'Hall 2',
            stars: 5,
            rate: 4.8
        },

    ]);


    useEffect(() => {
        const fetchUserDetails = async () => {
            try {
                const response = await fetch('https://backend-api.com/user-details');
                const data = await response.json();
                setUserName(data.name);
                setUserRating(data.rating);
                setUserDescription(data.description);
                setProfilePic(data.profilePic);
            } catch (error) {
                console.error('Error fetching user details:', error);
            }
        };

        const fetchHallCards = async () => {
            try {
                const response = await fetch('https://backend-api.com/hall-cards');
                const data = await response.json();
                setHallCards(data);
            } catch (error) {
                console.error('Error fetching hall cards:', error);
            }
        };

        fetchUserDetails();
        fetchHallCards();
    }, []);

    return (
        <>
            <div
                style={{
                    backgroundColor: colors.primary,
                    minHeight: '100vh',
                }}
                className={"bg-black"}
            >
                <Header searchBar={true} />
                <div className={"py-10 md:px-10 px-4"}>
                    <div className={"flex justify-between md:flex-row flex-col"}>
                        {/* profile pic */}
                        <div className={"lg:basis-1/6 basis-1/5 aspect-square md:rounded-full flex justify-center items-center"}>
                            <img
                                className={" w-full object-cover md:rounded-full aspect-square"}
                                src={profilePic || "assets/profilePic.png"}
                                alt={"profile_pic"}
                            />
                        </div>
                        {/* user info */}
                        <div className={"flex flex-col lg:basis-4/6 text-white my-4 gap-2"}>
                            <div className={"md:text-6xl text-4xl font-bold"}>{userName}</div>
                            <div className={"text-lg flex gap-10"}>Rating:
                                <Rate stars={Math.round(userRating)} rate={userRating}/>
                            </div>
                            <div>
                                <h1 className={"text-lg"}>Description:</h1>
                                <p className={"md:text-base text-sm"}>
                                    {userDescription}
                                </p>
                            </div>
                        </div>
                        {/* edit button */}
                        <div className={"flex justify-end"}>
                            <div
                                className={"flex my-4 justify-center items-center md:w-12 w-full h-12 cursor-pointer md:rounded-full"}
                                onClick={() => {
                                    // navigate to settings page
                                }}
                                style={{
                                    backgroundColor: colors.secondary1,
                                }}
                            >
                                <img
                                    className={"h-8 w-8 object-cover rounded"}
                                    src={"assets/Edit03.png"}
                                    alt={"edit_icon"}
                                />
                            </div>
                        </div>
                    </div>
                    <div className="relative flex items-center my-10">
                        <div className="flex-grow border-t border-white"></div>
                        <span className="mx-4 text-white text-2xl">Available Halls</span>
                        <div className="flex-grow border-t border-white"></div>
                    </div>
                    {/* cards container */}
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-10">
                        {hallCards.map((hall) => (
                            <HallCard key={hall.id} image={hall.image} Name={hall.name} stars={hall.stars}/>
                        ))}
                        <div className="flex flex-col">
                            <div
                                style={{
                                    borderColor: colors.secondary2,
                                    minHeight: "300px",
                                }}
                                className={"text-white border-white border-2 border-dashed flex justify-center items-center h-full"}
                            >
                                <div
                                    className={"aspect-square border-2 border-dashed rounded-full flex justify-center items-center hover:border-solid "}
                                    style={{
                                        borderColor: colors.secondary2,
                                        width: "50%",
                                    }}
                                >
                                    <img
                                        className={"p-10 cursor-pointer hover:scale-110"}
                                        src={"assets/plus.png"}
                                        alt={"plus_icon"}
                                        onClick={() => {
                                            // navigate to add hall page
                                        }}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};