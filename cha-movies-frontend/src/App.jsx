import {BrowserRouter, Routes, Route} from 'react-router-dom';
import React, {Component} from 'react';
import HomePage from './pages/HomePage';
import TopMoviesPage from './pages/TopMoviesPage'
import MoviePage from './pages/MoviePage'
import ActorPage from './pages/ActorPage'
import LoginPage from './pages/LoginPage'
import GenrePage from './pages/GenrePage'
import TitlePage from './pages/TitlePage'
import Navbar from './components/Navbar'
import SearchPage from './pages/SearchPage';
import CartPage from './pages/CartPage';
import { SiteStyle, NavbarStyle } from './styles/Site.style';
import PaymentPage from './pages/PaymentPage';
import ConfirmationPage from './pages/ConfirmationPage';
import DashBoard from './pages/DashBoard';
import AddMovie from './pages/AddMoviePage';
import AddStar from './pages/AddStar';
import ELoginPage from './pages/eLoginPage';

function App() {
    return (
        <SiteStyle>

        <NavbarStyle>
        <a href="/cha-movies/" className="homeLink">Home</a>
        <Navbar />
        <a href="/cha-movies/ranked">Ranked Movies</a>
        <a href="/cha-movies/login">Login</a>
        <a href="/cha-movies/cart" id="checkout-button" >Checkout</a>
        </NavbarStyle>


        <BrowserRouter>
            <Routes>
                <Route path='/cha-movies' element = {<HomePage/>} />
                <Route path='/cha-movies/payment/confirmation' element={<ConfirmationPage/>} />
                <Route path='/cha-movies/payment' element={<PaymentPage/>} />
                <Route path='/cha-movies/Cart' element = {<CartPage/>} />
                <Route path='/cha-movies/search' element = {<SearchPage />} />
                <Route path='/cha-movies/genre/:genreId' element = {<GenrePage />}/>
                <Route path='/cha-movies/title/:title' element = {<TitlePage />}/>
                <Route path='/cha-movies/ranked' element = {<TopMoviesPage/>} />
                <Route path='/cha-movies/movies/:movieId' element = {<MoviePage/>} />
                <Route path='/cha-movies/actors/:actorId' element = {<ActorPage/>} />
                <Route path='/cha-movies/Login' element = {<LoginPage/>} />
                <Route path='/cha-movies/_dashboard' element = {<DashBoard/>} />
                <Route path='/cha-movies/_dashboard/addmovie' element = {<AddMovie/>} />
                <Route path='/cha-movies/_dashboard/addstar' element = {<AddStar/>} />
                <Route path='/cha-movies/elogin' element = {<ELoginPage/>} />
            </Routes>
        </BrowserRouter>

        </SiteStyle>
    )
}

export default App