import {
  Route,
  BrowserRouter as Router,
  Routes,
} from "react-router-dom";
import './App.css';
import { HallDetails } from './HallDetails/HallDetails';
import { Login } from './Sign/LoginPage/Login';

import {WorkSpace} from "./WorkSpace/WorkSpace";
import { Navigate } from 'react-router-dom';

import { HallsList} from './HallsList&Filter/HallsListPage/HallsList';

function App() {
  return (
    <Router>
      <Routes>
        <Route path='/' element={<Navigate to="/login" />} />
        {/* <Route path='/' element={<HomePage/>}/> */}
        { <Route path='/workspace' element={<WorkSpace/>}/> }
        { <Route path='/workspace/:workspaceId' element={<WorkSpace/>}/> }
        {/* {<Route path='/hall' element={<HallDetails/>}/>} */}
        {<Route path='/login' element={<Login/>}/>}
        {<Route path='/signup' element={<SignUp/>}/>}
        <Route path='/workspace/:workspaceId/hall/:id' element={<HallDetails/>}/>
        {/* <Route path='/login' element={<LoginPage/>}/> */}
        <Route path='/hallsList' element={<HallsList/>}/>
      </Routes>
    </Router>
  );
}

export default App;
