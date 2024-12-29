import { useState, useEffect } from 'react';
import { SearchBar } from './SearchBar';
import PropTypes from 'prop-types';
import Logo from '../assets/Logo.png';
import ProfilePic from '../assets/profilePic.png';

export const Header = (props) => {
    const [searchTerm, setSearchTerm] = useState('');
    const [searchCategory, setSearchCategory] = useState('all');
    const [isSwitchOn, setIsSwitchOn] = useState(false);

    const handleSearch = () => {
        console.log(`Searching for ${searchTerm} in category ${searchCategory}`);
    };

    const toggleSwitch = () => {
        setIsSwitchOn(!isSwitchOn);
    };

    return (
        <>
            <header
                className={"bg-primary h-[10vh] py-6 md:px-10 px-4 flex justify-between items-center gap-4 md:gap-0"}
            >
                {/*logo*/}
                <div className={"h-full w-fit md:basis-1/6 "}>
                    <img src={Logo} alt={"logo"} className={"h-full"} />
                </div>
                {/*search bar*/}
                {/* <div className={"flex md:basis-1/2 basis-5/12 items-center"}>
                    {props.searchBar ? <SearchBar /> : null}
                </div> */}
                {/*profile*/}
                <div className={"flex lg:basis-1/6 justify-end md:gap-4 items-center"}>
                    {/*switch button*/}
                    <label className="md:inline-flex hidden relative  items-center cursor-pointer">
                        <input type="checkbox" checked={isSwitchOn} onChange={toggleSwitch} className="sr-only peer" />
                        <div
                            className="w-11 h-6 border-2 border-secondary1 rounded-full peer-checked:after:translate-x-full
                                after:absolute after:top-0.5 after:left-[2px] after:bg-[#FF9944] after:border-[#FF9944]
                                after:border after:rounded-full after:h-5 after:w-5 after:transition-all"
                        ></div>
                    </label>
                    {/*profile picture*/}
                    <img
                        src={ProfilePic}
                        alt={"user"}
                        className={"w-10 aspect-square rounded-full md:ml-4 cursor-pointer"}
                        onClick={() => {
                            // navigate to profile page
                        }}
                    />
                </div>
            </header>
        </>
    );
};

Header.propTypes = {
  searchBar: PropTypes.bool,
};

Header.defaultProps = {
  searchBar: true,
};