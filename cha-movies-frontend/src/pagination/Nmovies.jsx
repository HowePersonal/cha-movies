import React, {useState} from 'react';

function Nmovies({setMoviePerPage, currentMoviePerPage}){

    const options = [
        {label: "10", value: 10},
        {label: "25", value: 25},
        {label: "50", value: 50},
        {label: "100", value: 100}
    ]

    return (
        <div className="dropdown-movies">
            <h4>Movies Per Page</h4>
            <select className="form-select" onChange={e => setMoviePerPage(e.target.value)} value={currentMoviePerPage}>
                {options.map(option => (
                    <option value={option.value}>{option.label}</option>
                ))}
            </select>
        </div>
    );
}

export default Nmovies;