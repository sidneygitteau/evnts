import { BrowserRouter, Route, Routes } from 'react-router-dom';

import Accueil from './pages/Accueil/Accueil';

import AllSalons from './pages/AllSalons/AllSalons';

import Profil from './pages/Profil/Profil';




function App() {


    return (
      <BrowserRouter>
        <Routes>
            <Route path="/" element={<Accueil/>}/>
            <Route path="/salons" element={<AllSalons/>}/>
            <Route path="/profile" element={<Profil/>}/>
        </Routes>
      </BrowserRouter>
    );
  }
  
  export default App;