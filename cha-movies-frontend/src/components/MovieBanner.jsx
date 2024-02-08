import ActorLink from '../components/ActorLink';
import GenreLink from '../components/GenreLink';
import CartButton from './CartButton';
import {MovieBannerStyles} from '../styles/Movie.style';

function MovieBanner(movieObject) {
    let movie = movieObject.movieObject;
    let actorList = <></>
    if (movie.movie_stars != null) {
        let actorInfo = movie.movie_stars.split(',');
        actorList = actorInfo.map(info => {
            if (info != null) {
                let infoSplit = info.split(':');
                return <ActorLink id = {infoSplit[0]} name = {infoSplit[1]}/>
            }
            let infoSplit = info.split(':');
            return <></>
        })
    }


    let genreInfo = movie.movie_genres.split(',');
    const genreList = genreInfo.map(info => {
        let infoSplit = info.split(':');
        return <GenreLink id = {infoSplit[0]} genre = {infoSplit[1]}/>
    })

 
    return (
        <tr key={movie.movie_id}>
            <td><a href={"/cha-movies/movies/"+movie.movie_id}>{movie.movie_title}</a></td>
            <td>{movie.movie_year}</td>
            <td>{movie.movie_director}</td>
            <td>{movie.movie_rating}</td>
            <td>{actorList}</td>
            <td>{genreList}</td>
            <td><CartButton class ="CartButton" title = {movie.movie_title} action = "purchase" displayName="Purchase"/>   </td> 
        </tr>
        
    )
}

export default MovieBanner;