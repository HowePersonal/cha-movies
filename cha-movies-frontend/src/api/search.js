
async function search(title, director, stars, year) {
    try {

        let urlPrefix = import.meta.env.VITE_URL_PREFIX 
        let apiLink = `${urlPrefix}` + `/cha-movies/api/search?title=${title}`
        const response = await fetch(apiLink, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        })
    
        const data = await response.json();
        return data
    } catch (error) {
        console.log("Error when sending search query: ", error)
    }


}

export default search;