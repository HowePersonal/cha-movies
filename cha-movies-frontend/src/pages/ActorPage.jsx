import {useState, useEffect} from 'react'
import {useParams} from "react-router-dom";
import MovieLink from '../components/MovieLink';
import getSingleActor from '../api/singleActor'
import { ActorPageStyle } from '../styles/Actor.style';

function ActorPage() {
    const [actorObject, setActorObject] = useState([]);
    const [movieList, setMovieList] = useState([]);
    const {actorId} = useParams()

    useEffect(() => {
        getSingleActor(actorId).then(data => {
            setActorObject(data[0])
        })
    }, [])

    useEffect(() => {
        if (actorObject.star_movies) {
            let movieInfo = actorObject.star_movies.split(',');
            const getMovieList = movieInfo.map(info => {
                let infoSplit = info.split(';');
                return <MovieLink path = {'../movies/' + infoSplit[0]} id = {infoSplit[0]} name = {infoSplit[1]}/>
            })
            setMovieList(getMovieList);
        }
    }, [actorObject])



    return (
        <>
            <a href={JSON.parse(window.localStorage.getItem("PrevURL")) || '/cha-movies/ranked'}><h3>{"Previous Page"}</h3></a>
            <ActorPageStyle>
            <p className="actor-name">{actorObject.star_name}</p>
            <p className="actor-info">{actorObject.star_dob}</p>
            <p className="actor-movies">{movieList}</p>
            </ActorPageStyle>
        </>
    )
}

export default ActorPage