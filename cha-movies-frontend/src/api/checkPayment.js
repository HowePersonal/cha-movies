

async function checkPayment(credit_num, first_name, last_name, expiration_date) {
    try {
        let urlPrefix = import.meta.env.VITE_URL_PREFIX 
        let apiLink = `${urlPrefix}` + `/cha-movies/api/payment?credit_num=${credit_num}&first_name=${first_name}&last_name=${last_name}&expiration_date=${expiration_date}`
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

export default checkPayment;