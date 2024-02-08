import React, {Component} from 'react';

async function getMetaData(table_name) {
    let urlPrefix = import.meta.env.VITE_URL_PREFIX 
    let apiLink = `${urlPrefix}` + "/cha-movies/api/meta?table_name="+table_name;
    try {
        const response = await fetch(apiLink, {
            method: "GET",
        });

        const data = await response.json();
        return data
    }
    catch (error) {
        console.error("Error when fetching meta data from ", apiLink);
    }

}


export default getMetaData;