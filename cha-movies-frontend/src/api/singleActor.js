import React, {Component} from 'react';

async function getSingleActor(id) {
    let urlPrefix = import.meta.env.VITE_URL_PREFIX 
    let apiLink = `${urlPrefix}` + "/cha-movies/api/single-star?id="+id;
    try {
        const response = await fetch(apiLink, {
            method: "GET",
        });

        const data = await response.json();
        return data
    }
    catch (error) {
        console.error("Error when fetching top movies from ", apiLink);
    }

}


export default getSingleActor;