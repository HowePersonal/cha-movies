import React, {useState, useEffect} from 'react'
import getTopMovies from '../api/topMovies.js'
import MovieBanner from '../components/MovieBanner.jsx'
import Pagination from "../pagination/Pagination.jsx";
import sortMovieData from '../sort/sortMovieData.js';
import MovieTable from '../components/MovieTable.jsx';
import PageOptions from '../pagination/PageOptions.jsx';

function HomePage() {
    const [topMovieList, setTopMovieList] = useState([]);
    const prevPage = JSON.parse(window.localStorage.getItem("PrevPage") || 1) 
    const [page_num, set_page_num] = useState(prevPage);
    const [moviePerPage, setMoviePerPage] = useState(JSON.parse(window.localStorage.getItem("MoviesPerPage")) || 10) 
    const lastMovieIndex = page_num * moviePerPage;
    const firstMovieIndex = lastMovieIndex - moviePerPage;
    const [totalMovies, setTotalMovies] = useState(0);

    //sorting
    const [sortRating, setSortRating] = useState(JSON.parse(window.localStorage.getItem("RatingOrder")) || "ASC")
    const [sortTitle, setSortTitle] = useState(JSON.parse(window.localStorage.getItem("TitleOrder")) || "ASC")
    const [sortOrder, setSortOrder] = useState(JSON.parse(window.localStorage.getItem("SortOrder")) || "title")

    useEffect(() => {
        getTopMovies().then(movieData => {
            movieData = sortMovieData(movieData, sortOrder, sortTitle, sortRating);
  
            setTotalMovies(movieData.length)
            const currentMovieData = movieData.slice(firstMovieIndex, lastMovieIndex)
            
            setTopMovieList(currentMovieData)

        }).catch(error => {
            console.log("Error when getting top movies")
        })
    }, [page_num, moviePerPage, sortTitle, sortRating, sortOrder])


    const movieBanners = topMovieList.map(movieObject => 
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

// sorting: https://www.material-react-table.com/docs/guides/sorting
    return (
        <>
        <PageOptions setMoviePerPage={setMoviePerPage} currentMoviePerPage={moviePerPage} 
                        setSortRating={setSortRating} setSortTitle={setSortTitle} setSortOrder={setSortOrder} />
        
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

export default HomePage