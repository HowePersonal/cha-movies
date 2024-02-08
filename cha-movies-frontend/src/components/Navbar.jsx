import {useState, useEffect} from 'react'
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import apiAutoComplete from '../api/autocompleteAPI';
import _ from "lodash"


function Navbar() {
    const [inputTitle, setInputTitle] = useState("");
    const [selectedTitle, setSelectedTitle] = useState(null);
    const [displayTitle, setDisplayTitle] = useState("");
    const [autoComplete, setAutoComplete] = useState([]);

    useEffect(() => {
        if (inputTitle && inputTitle.length > 2) {
            console.log("Autocomplete initiated")
            if (localStorage.getItem(inputTitle) == null) {
                console.log("Calling backend to search for ", inputTitle)
                apiAutoComplete(inputTitle).then(data => {
                    if (data) {
                        const newArray = data.map(m => ({label: m.movie_title+" ("+m.movie_year+")", id: m.movie_id, title: m.movie_title}))
                        setAutoComplete(newArray)
                        localStorage.setItem(inputTitle, JSON.stringify(newArray))
                    }
                })
            }
            else {
                setAutoComplete(JSON.parse(localStorage.getItem(inputTitle)))
                console.log("Found cache result for query ", inputTitle)
            }

        }
        else {
            setAutoComplete([])
        }
    }, [inputTitle])

    const highlightChangeHandler = (e, value) => {
        if (value) {
            setSelectedTitle(value)
            setDisplayTitle(value.label); 
        }
       
    }

    const changeHandler = (e, value) => {
        if (value) { 
            if (e && e.type == "click") {
                window.location.href = `/cha-movies/movies/${value.id}`
            }
            
        }
    }

    const inputChangeHandler = (e, value) => {
        if (e && e.type == "change") {
            setInputTitle(value)
            setDisplayTitle(value.label);
        }
    }

    return (
        <Autocomplete
        freeSolo
        disablePortal
        id="combo-box-demo"
        value={displayTitle || inputTitle}
        isOptionEqualToValue={(option, value) => {
            if (value && option) {
                return option.label == value 
            } else {
                return true
            }
        }}

        

        onHighlightChange={highlightChangeHandler}
        
        onChange = {changeHandler}

        onInputChange={_.debounce(inputChangeHandler, 300)}

        onKeyDown={e => {
            if (selectedTitle && (e.key === 'Enter')) {
                e.defaultMuiPrevented = true;
                window.location.href = `/cha-movies/movies/${selectedTitle.id}`
            }
            else if (e.key === 'Enter') {
                e.defaultMuiPrevented = true;
                window.location.href = `/cha-movies/search?title=${inputTitle}`
            }
        }}
        options={autoComplete}
        sx={{ width: 300 }}
        renderInput={(params) => <TextField {...params} InputProps={{...params.InputProps, endAdornment: null}}/>}
        filterOptions={m => m}
        onBlur = {() => setAutoComplete([])}
      />

    )
}

export default Navbar;