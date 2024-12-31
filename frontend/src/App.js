import {
    Navigate,
    Route,
    BrowserRouter as Router,
    Routes,
} from "react-router-dom";
import "./App.css";
import { HallDetails } from "./HallDetails/HallDetails";
import { Login } from "./Sign/LoginPage/Login";
import { SignUp } from "./Sign/SignUpPage/SignUp";

import { WorkSpace } from "./WorkSpace/WorkSpace";
import { SelectRole } from "./Sign/SelectRolePage/SelectRole";
import { HallsList } from "./HallsList&Filter/HallsListPage/HallsList";
import { MyWorkspaces } from "./WorkSpace/MyWorkspaces";
import { UserContext } from "./UserContext";
import { Reservations } from "./Reservations/Reservations";
import { useEffect, useState } from "react";
import { info } from "./api";

function App() {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            info()
                .then((user) => setUser(user))
                .catch(() => localStorage.removeItem("token"));
        }
    }, []);

    return (
        <Router>
            <UserContext.Provider value={{ user, setUser }}>
                <Routes>
                    <Route path="/" element={<Navigate to="/login" />} />
                    {/* <Route path='/' element={<HomePage/>}/> */}
                    {<Route path="/workspace" element={<WorkSpace />} />}
                    {
                        <Route
                            path="/workspace/:workspaceId"
                            element={<WorkSpace />}
                        />
                    }
                    {/* {<Route path='/hall' element={<HallDetails/>}/>} */}
                    {<Route path="/login" element={<Login />} />}
                    {<Route path="/signup" element={<SignUp />} />}
                    <Route
                        path="/workspace/:workspaceId/hall/:id"
                        element={<HallDetails />}
                    />
                    {<Route path="/select-role" element={<SelectRole />} />}
                    {/* <Route path='/login' element={<LoginPage/>}/> */}
                    <Route path="/hallsList" element={<HallsList />} />
                    {/* {user && user.role === "manager" && ( */}
                    <Route path="/myWorkspaces" element={<MyWorkspaces />} />
                    {/* )} */}
                    {<Route path="/reservations" element={<Reservations />} />}
                </Routes>
            </UserContext.Provider>
        </Router>
    );
}

export default App;
