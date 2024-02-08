

async function apiAddStar(name, birthYear) {
    try {
        let urlPrefix = import.meta.env.VITE_URL_PREFIX 
        let apiLink = `${urlPrefix}` + `/cha-movies/api/addstar`
        const response = await fetch(apiLink, {
        method: 'POST',
        body: `name=${name}&birthYear=${birthYear}`,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        }})
        const data = await response.json();
    
        return data
    }
    catch (e) {
        console.log("Error when adding star: ", e);
    }
}

export default apiAddStar;