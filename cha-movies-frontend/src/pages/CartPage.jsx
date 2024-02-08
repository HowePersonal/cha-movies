import {useState, useEffect} from 'react'
import getCartItems from '../api/cartItems';
import CartOptions from '../components/CartOptions';
import {CartPageStyle, CartTDStyle} from '../styles/Page.style';

function Cart() {
    const [cartList, setCartList] = useState([]);
    const [buttonClicks, setButtonClicks] = useState(0);
    const [total, setTotal] = useState(0);
    
    const handleButtonClick = () => {
        setButtonClicks(prevClick => prevClick + 1);
    }

    useEffect(() => {
        getCartItems().then(data => {
            let temp_total = 0;
            if (data) {
                const cartItems = Object.entries(data).map(([key, value]) => 
                    {   
                        temp_total +=  1 * parseInt(value)

                        return (
                            <tr key={key}>
                            <td>{key}</td>

                            <td>
                            <CartTDStyle>
                            <CartOptions 
                                title={key} 
                                action="increase" 
                                displayName="+" 
                                onClick={handleButtonClick}
                            />
                            {value}
                            <CartOptions 
                                title={key} 
                                action="decrease" 
                                displayName="-" 
                                onClick={handleButtonClick}
                            />
                            </CartTDStyle>
                            </td>
                            <td>1</td>
                            <td>
                            <CartTDStyle>    
                            {1 * parseInt(value)}
                            <CartOptions 
                                title={key} 
                                action="delete" 
                                displayName="x" 
                                onClick={handleButtonClick}
                            />
                            </CartTDStyle>
                            </td>
                        </tr>
                        )
                    }
                    
                );
                setCartList(cartItems);
            }
            setTotal(temp_total);
        });
    }, [buttonClicks]);
    

    return (
        <CartPageStyle>
        <table>
        <tr>
           <th>Title</th> 
           <th>Quantity</th> 
           <th>Price</th> 
           <th>Total Price</th> 
        </tr>
        {cartList}
        </table>
        <h3 class="payment-total">Payment Total: {total}</h3>
        <a href={"/cha-movies/payment?total="+total} >Proceed to Payment</a>
        </CartPageStyle>
    )

}

export default Cart;