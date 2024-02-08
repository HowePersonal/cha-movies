import React, {Component} from 'react';


async function getAllTableNames() {
    let urlPrefix = import.meta.env.VITE_URL_PREFIX 
    let apiLink = `${urlPrefix}` + "/cha-movies/api/table";
    try {
        const response = await fetch(apiLink, {
            method: "GET"
        });
        const data = await response.json();
        return data
    }
    catch (error) {
        console.error("Error when fetching table names from ", apiLink);
    }

}


export default getAllTableNames;