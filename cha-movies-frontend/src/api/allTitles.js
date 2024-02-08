import React, {Component} from 'react';


async function getAllTitles() {
    let urlPrefix = import.meta.env.VITE_URL_PREFIX 
    let apiLink = `${urlPrefix}` + "/cha-movies/api/title";
    try {
        const response = await fetch(apiLink, {
            method: "GET"
            
        });
        const data = await response.json();
        return data
    }
    catch (error) {
        console.error("Error when fetching titles from ", apiLink);
    }

}


export default getAllTitles;