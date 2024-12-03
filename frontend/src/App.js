import logo from './logo.svg';
import './App.css';
import {
  BrowserRouter as Router,
  Routes,
  Route,
} from "react-router-dom";
import {WorkSpace} from "./WorkSpace/WorkSpace";

function App() {
  return (
    <Router>
      <Routes>
        <Route path='/'>
          {/* <HomePage/> TODO */}
        </Route>
        <Route path='/workspace' element={<WorkSpace/>}>
          {/* <WorkSpace/> TODO */}
        </Route>
        <Route path='/hall'>
          {/* <Hall/> TODO */}
        </Route>
        <Route path='/login'>
          {/* <Login/> TODO */}
        </Route>

      </Routes>
    </Router>
  );
}

export default App;
