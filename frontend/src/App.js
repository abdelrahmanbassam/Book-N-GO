import logo from './logo.svg';
import './App.css';
import {
  BrowserRouter as Router,
  Routes,
  Route,
} from "react-router-dom";
import { HallDetails } from './HallDetails/HallDetails';
import { SignUp } from './Sign/SignUpPage/SignUp';
import { Login } from './Sign/LoginPage/Login';
function App() {
  return (
    <Router>
      <Routes>
        {/* <Route path='/' element={<HomePage/>}/> */}
        {/* <Route path='/workspace' element={<WorkSpace/>}/> */}
        {<Route path='/hall' element={<HallDetails/>}/>}
        {<Route path='/login' element={<Login/>}/>}
        {<Route path='/signup' element={<SignUp/>}/>}
      </Routes>
    </Router>
  );
}

export default App;
