import {useState, useEffect} from 'react'
import checkPayment from '../api/checkPayment';
import { useLocation } from 'react-router-dom';
import { PaymentPageStyle } from '../styles/Page.style';

function PaymentPage() {
    const [creditNum, setCreditNum] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [expirationDate, setExpirationDate] = useState("");

    const location = useLocation();
    const query = new URLSearchParams(location.search)
    const payment_total = query.get("total")

    const submitHandler = async e => {
        e.preventDefault();
        const payment_form = document.getElementById("payment-form");
        payment_form.reset();
        checkPayment(creditNum, firstName, lastName, expirationDate).then(data => {

            if (data.status == "success") {
                window.location.href = '/cha-movies/payment/confirmation';
            }
            else {
                alert("Wrong payment information")
            }
        })

    } 
 
    return (
        <PaymentPageStyle>
        <form name="payment" id="payment-form" onSubmit={submitHandler}>
            <div>Credit Card Number: <input type="text" name="credit_num" onChange={e => setCreditNum(e.target.value)}/> </div>
            <div>First Name: <input type="text" name="first_name" onChange={e => setFirstName(e.target.value)}/> </div>
            <div>Last Name: <input type="text" name="last_name" onChange={e => setLastName(e.target.value)}/> </div>
            <div>Expiration Date: <input type="date" name="expiration_date" onChange={e => setExpirationDate(e.target.value)}/> </div>
            <h3 class="payment-total">Payment Total: {payment_total}</h3>
            <input type="submit" value="Place Order" />
        </form>
        </PaymentPageStyle>
    )

}

export default PaymentPage;