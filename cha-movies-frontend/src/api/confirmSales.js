

async function confirmSales(sale_id, movie_id) {
    try {
        let urlPrefix = import.meta.env.VITE_URL_PREFIX 
        let apiLink = `${urlPrefix}` + `/cha-movies/api/payment/confirmation`
        const response = await fetch(apiLink, {
        method: 'POST',
        body: `sale_id=${sale_id}&movie_id=${movie_id}`,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        }})
        const data = await response.json();
    
        return data
    }
    catch (e) {
        console.log("Error when adding item to cart: ", e);
    }
}

export default confirmSales;