import {useState, useEffect} from 'react';
import { useLocation } from 'react-router-dom';
import search from '../api/search';
import MovieBanner from '../components/MovieBanner';
import Pagination from '../pagination/Pagination';
import sortMovieData from '../sort/sortMovieData';
import MovieTable from '../components/MovieTable';
import PageOptions from '../pagination/PageOptions';
import { PageOptionStyle } from "../styles/Other.style";
import Nmovies from '../pagination/Nmovies.jsx';
import SortTable from '../sort/SortTable.jsx';

function SearchPage() {
    const [movieList, setMovieList] = useState([]);

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
    

    const location = useLocation();
    const query = new URLSearchParams(location.search)
    const title = query.get('title');

    useEffect(() => {
        search(title).then(data => {
            data = sortMovieData(data, sortOrder, sortTitle, sortRating);
            setTotalMovies(data.length)

            const currentMovieData = data.slice(firstMovieIndex, lastMovieIndex)

            setMovieList(currentMovieData)
        }).catch(error => {

            console.log("Error when searching for movies", error)
        })
    }, [page_num, moviePerPage, sortTitle, sortRating, sortOrder])

    useEffect(() => {
        window.localStorage.setItem("PrevPage", JSON.stringify(page_num))
        window.localStorage.setItem("PrevURL", JSON.stringify(window.location.href))
        window.localStorage.setItem("MoviesPerPage", JSON.stringify(moviePerPage))
        window.localStorage.setItem("SortOrder", JSON.stringify(sortOrder))
        window.localStorage.setItem("TitleOrder", JSON.stringify(sortTitle))
        window.localStorage.setItem("RatingOrder", JSON.stringify(sortRating))
    })
    

    const movieBanners = movieList.map(movieObject => 
        <MovieBanner movieObject = {movieObject}/>
    )





    return (
        <>
                <PageOptionStyle>
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
        </PageOptionStyle>

        <MovieTable movieBanners={movieBanners}/>
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

export default SearchPage