

async function apiAddMovie(title,year,director,name,birthYear,genre) {
    try {
        let urlPrefix = import.meta.env.VITE_URL_PREFIX 
        let apiLink = `${urlPrefix}` + `/cha-movies/api/addmovie`
        const response = await fetch(apiLink, {
        method: 'POST',
        body: `title=${encodeURIComponent(title)}&year=${encodeURIComponent(year)}&director=${encodeURIComponent(director)}&name=${encodeURIComponent(name)}&birthYear=${encodeURIComponent(birthYear)}&genre=${encodeURIComponent(genre)}`,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        }})
        const data = await response.json();
    
        return data
    }
    catch (e) {
        console.log("Error when adding movie: ", e);
    }
}

export default apiAddMovie;