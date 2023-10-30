import {useState, useEffect} from 'react'
import {useParams} from "react-router-dom";
import getSingleMovie from '../api/singleMovie.js';
import MovieBanner from '../components/MovieBanner.jsx';
import Pagination from '../pagination/Pagination.jsx';
import sortMovieData from '../sort/sortMovieData.js';
import Nmovies from '../pagination/Nmovies.jsx';
import SortTable from '../sort/SortTable.jsx';

function GenrePage() {
    const [movieList, setMovieList] = useState([]);
    const {genreId} = useParams();

    //Pagination variables
    const [page_num, set_page_num] = useState(JSON.parse(window.localStorage.getItem("PrevPage") || 1));
    const [moviePerPage, setMoviePerPage] = useState(JSON.parse(window.localStorage.getItem("MoviesPerPage")) || 10);
    const lastMovieIndex = page_num * moviePerPage;
    const firstMovieIndex = lastMovieIndex - moviePerPage;
    const [totalMovies, setTotalMovies] = useState(0);

    //sorting
    const [sortRating, setSortRating] = useState(JSON.parse(window.localStorage.getItem("RatingOrder")) || "ASC")
    const [sortTitle, setSortTitle] = useState(JSON.parse(window.localStorage.getItem("TitleOrder")) || "ASC")
    const [sortOrder, setSortOrder] = useState(JSON.parse(window.localStorage.getItem("SortOrder")) || "title")

    useEffect(() => {
        getSingleMovie("http://localhost:8000/cha-movies/api/single-genre?id="+genreId).then(data => {
            data = sortMovieData(data, sortOrder, sortTitle, sortRating);
            setTotalMovies(data.length)

            const currentMovieData = data.slice(firstMovieIndex, lastMovieIndex)

            setMovieList(currentMovieData)

        })
    }, [page_num, moviePerPage, sortTitle, sortRating, sortOrder])

    const movieBanners = movieList.map(movieObject => 
        <MovieBanner movieObject = {movieObject}/>
    )

    useEffect(() => {
        window.localStorage.setItem("PrevPage", JSON.stringify(page_num))
        window.localStorage.setItem("PrevURL", JSON.stringify(window.location.href))
        window.localStorage.setItem("MoviesPerPage", JSON.stringify(moviePerPage))
        window.localStorage.setItem("SortOrder", JSON.stringify(sortOrder))
        window.localStorage.setItem("TitleOrder", JSON.stringify(sortTitle))
        window.localStorage.setItem("RatingOrder", JSON.stringify(sortRating))
    })

    return (
        <>
        <div>
            <Nmovies
                setMoviePerPage={setMoviePerPage}
                currentMoviePerPage={moviePerPage}
            />
        </div>
        <div>
            <SortTable
                setSortRating={setSortRating}
                setSortTitle={setSortTitle}
                setSortOrder={setSortOrder}
            />
        </div>
        <table>
            <tr>
               <th>Title</th> 
               <th>Year</th> 
               <th>Director</th> 
               <th>Rating</th> 
               <th>Stars</th> 
               <th>Genres</th> 
            </tr>
            {movieBanners}
        </table>
        <div>
            <Pagination
                totalMovies={totalMovies}
                moviesPerPage={moviePerPage}
                setCurrentPage={set_page_num}
                currentPage={page_num}
            />
        </div>
        </>
    )
}

export default GenrePage;

