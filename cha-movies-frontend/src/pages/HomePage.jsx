import {useState, useEffect} from 'react'
import getAllGenres from '../api/allGenres'
import getAllTitles from '../api/allTitles';
import GenreLink from '../components/GenreLink'
import {Home, Selections} from '../styles/Home.style'
import TitleLink from '../components/TitleLink';

function HomePage() {
    const [genres, setGenres] = useState([])
    const [titles, setTitles] = useState([])
    

    useEffect(() => {
        getAllGenres().then(genreData => {
            setGenres(genreData)
        }).catch(error => {
            console.log("Error when getting all genres")
        })
    }, [])

    useEffect(() => {
        getAllTitles().then(titleData => {
            setTitles(titleData)
        }).catch(error => {
            console.log("Error when getting all titles")
        })
    }, [])

    const genreLinks = genres.map(genreObject => 
        <GenreLink id = {genreObject["id"]}
                   genre = {genreObject["genre"]}/>)

    const titleLinks = titles.map(titleObject => 
    <TitleLink title = {titleObject}/>)

    return (
        <Home>
            <br/>
            <Selections>
                {titleLinks}
            </Selections>
            <br/>
            <Selections>
                {genreLinks}
            </Selections>
        </Home>
        
    )
}

export default HomePage