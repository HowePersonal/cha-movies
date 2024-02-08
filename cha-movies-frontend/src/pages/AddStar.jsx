import {useState, useEffect} from 'react'
import apiAddStar from '../api/AddStarAPI';


function AddStar() {
    const [stars, setStars] = useState("");
    const [dob, setDoB] = useState("");


    const submitHandler = async e => {
        e.preventDefault();
        apiAddStar(stars, dob).then(data => {

            if (data.status == "success") {
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
                <div><input type="text" placeholder = "Star" name="stars" onChange={e => setStars(e.target.value)}/></div>
                <div><input type="text" placeholder = "Year of Birth" name="dob" onChange={e => setDoB(e.target.value)}/></div>
                <input type="submit" value="Add" />
            </form>
        </>

    )
}

export default AddStar;