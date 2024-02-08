import {useState, useEffect} from 'react'
import apiAddMovie from '../api/AddMovieAPI';


function AddMovie() {
    const [title, setTitle] = useState("");
    const [director, setDirector] = useState("");
    const [stars, setStars] = useState("");
    const [year, setYear] = useState("");
    const [dob, setDoB] = useState("");
    const [genre, setGenre] = useState("");


    const submitHandler = async e => {
        e.preventDefault();
        apiAddMovie(title,year,director,stars,dob,genre).then(data => {

            if (data.status == "success") {
                alert(data["message"])
            }
            else if (data.status == "in database"){
                alert(data["message"])
            }
            else {
                alert("Incorrect information")
            }
        })
    }


    return (
        <>
            <a href={"/cha-movies/_dashboard"} >Dashboard</a>
            <form name="search" onSubmit={submitHandler}>
                <div><input type="text" placeholder = "Title" name="title" onChange={e => setTitle(e.target.value)}/></div> 
                <div><input type="text" placeholder = "Year" name="year" onChange={e => setYear(e.target.value)}/></div>
                <div><input type="text" placeholder = "Director" name="director" onChange={e => setDirector(e.target.value)}/> </div>
                <div><input type="text" placeholder = "Star" name="stars" onChange={e => setStars(e.target.value)}/></div>
                <div><input type="text" placeholder = "Year of Birth" name="dob" onChange={e => setDoB(e.target.value)}/></div>
                <div><input type="text" placeholder = "Genre" name="genre" onChange={e => setGenre(e.target.value)}/></div>
                <input type="submit" value="Add" />
            </form>
        </>

    )
}

export default AddMovie;