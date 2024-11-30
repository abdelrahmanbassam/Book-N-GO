import logo from './logo.svg';
import './App.css';
import {
  BrowserRouter as Router,
  Switch,
  Route,
} from "react-router-dom";

function App() {
  return (
    <Router>
      <Switch>
        <Route path='/'>
          {/* <HomePage/> TODO */}
        </Route>
        <Route path='/workspace'>
          {/* <WorkSpace/> TODO */}
        </Route>
        <Route path='/hall'>
          {/* <Hall/> TODO */}
        </Route>
        <Route path='/login'>
          {/* <Login/> TODO */}
        </Route>

      </Switch>
    </Router>
  );
}

export default App;
