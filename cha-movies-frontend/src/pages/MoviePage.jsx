 import {useState, useEffect} from 'react'
import {useParams} from "react-router-dom";
import ActorLink from '../components/ActorLink';
import getSingleMovie from '../api/singleMovie';
import GenreLink from '../components/GenreLink';
import CartButton from '../components/CartButton';
import { MoviePageStyle } from '../styles/Movie.style';

function MoviePage() {
    const [movieObject, setMovieObject] = useState([]);
    const [actorList, setActorList] = useState([<></>]);
    const [genreList, setGenreList] = useState([<></>]);
    const {movieId} = useParams();

    let urlPrefix = import.meta.env.VITE_URL_PREFIX 
    let apiLink = `${urlPrefix}` + "/cha-movies/api/single-movie?id="+movieId

    useEffect(() => {
        getSingleMovie(apiLink).then(data => {
            setMovieObject(data[0])
        })
    }, [])

    useEffect(() => {
        if (movieObject.movie_stars) {
            let actorInfo = movieObject.movie_stars.split(',');
            const getActorList = actorInfo.map(info => {
                let infoSplit = info.split(':');
                return <ActorLink path = {'../actors/' + infoSplit[0]} id = {infoSplit[0]} name = {infoSplit[1]}/>
            })
            setActorList(getActorList);

        }
    }, [movieObject])

    useEffect(() => {
        if (movieObject.movie_genres) {
            let genreInfo = movieObject.movie_genres.split(',');
            const getGenreList = genreInfo.map(info => {
                let infoSplit = info.split(':');
                return <GenreLink id = {infoSplit[0]} genre = {infoSplit[1]}/>
            })
            setGenreList(getGenreList);
        }
    }, [movieObject])


    


    return (
            <>
            <a href={JSON.parse(window.localStorage.getItem("PrevURL")) || '/cha-movies/ranked'}><h3>{"Previous Page"}</h3></a>
            <MoviePageStyle>
            <p className="movie-title">{movieObject.movie_title}</p>
            <div className="movie-info">
            <p className="movie-year">Year: {movieObject.movie_year}</p>
            <p className="movie-director">Director: {movieObject.movie_director}</p>
            <p className="movie-rating">Rating: {movieObject.movie_rating}</p>
            </div>
            <div className="genre-list">{genreList}</div>
            <div className="actor-list">{actorList}</div>
            <CartButton title={movieObject.movie_title} action="purchase" displayName="Purchase"/>
            </MoviePageStyle>
            </>
    )
}

export default MoviePage