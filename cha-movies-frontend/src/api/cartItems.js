
async function getCartItems() {
    try {
        let urlPrefix = import.meta.env.VITE_URL_PREFIX 
        let apiLink = `${urlPrefix}` + `/cha-movies/api/cart`
        const response = await fetch(apiLink, {
        method: 'GET',
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

export default getCartItems;