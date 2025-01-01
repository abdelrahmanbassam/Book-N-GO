import PropTypes from 'prop-types';
import { useContext } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Logo from '../assets/Logo.png';
import ProfilePic from '../assets/profilePic.png';
import { UserContext } from "../UserContext";
import './Header.css';

export const Header = (props) => {
    const { user, setUser } = useContext(UserContext);
    const navigate = useNavigate();
    const location = useLocation();
    const isLoginPage = location.pathname === '/login';
    const isSignUpPage = location.pathname === '/signup';

    const handleLogout = () => {
        localStorage.removeItem('token');
        setUser(null);
        navigate('/');
    };

    return (
        <>
            <header className="header md:header-md">
                <div className="logo-container md:logo-container-md">
                    <img
                        src={Logo}
                        alt="logo"
                        className="logo"
                        onClick={() => {
                            // navigate('/');
                        }}
                    />
                </div>
                {user  != null && user.role === 'CLIENT' && (
                    <div className="navigationButtons">
                        <button className="button"
                            onClick={() => {
                                navigate('/hallsList');
                            }}
                        >Search</button>
                        <button className="button"
                            onClick={() => {
                                navigate('/reservations');
                            }}
                        >Bookings</button>
                        {/* <button className="button"
                            onClick={() => {
                                // navigate
                            }}
                        >Work Spaces</button> */}
                    </div>
                )}
                {user != null && user.role === 'PROVIDER' && (
                    <div className="navigationButtons">
                        <button className="button"
                            onClick={() => {
                                navigate('/myWorkspaces');
                            }}
                        >My Workspaces</button>
                        <button className="button"
                            onClick={() => {
                                navigate('/reservations');
                            }}
                        >Bookings</button>
                    </div>
                )}
                {user === null && (
                    <div>
                        <button
                            className={`logout-button ${isLoginPage ? 'disabled' : ''}`}
                            onClick={() => navigate('/login')}
                            disabled={isLoginPage}
                        >
                            Log In
                        </button>
                        <button
                            className={`logout-button ${isSignUpPage ? 'disabled' : ''}`}
                            onClick={() => navigate('/signup')}
                            disabled={isSignUpPage}
                        >
                            Sign Up
                        </button>
                    </div>
                )}
                {user != null && (
                    <div className="profile-container md:profile-container-md">
                        <img
                            src={ProfilePic}
                            alt="user"
                            className="profile-pic"
                            onClick={() => {
                                // navigate to profile page
                            }}
                        />
                        <button onClick={handleLogout} className="logout-button">
                            Log Out
                        </button>
                    </div>
                )}

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