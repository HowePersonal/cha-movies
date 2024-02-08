import {useState, useEffect} from 'react'
import getCartItems from '../api/cartItems';
import confirmSales from '../api/confirmSales'
import { CartPageStyle } from '../styles/Page.style';

function ConfirmationPage() {
    const [dataList, setDataList] = useState([]);
    const [cartList, setCartList] = useState([]);
    const [total, setTotal] = useState(0);
    
    const sale_id = Math.floor(13560 + Math.random() * 9000);

    useEffect(() => {
        getCartItems().then(data => {
            let temp_total = 0;
            if (data) {
                setDataList(data);
                const cartItems = Object.entries(data).map(([key, value]) => 
                    {   
                        temp_total +=  1 * parseInt(value)


                        return (
                            <tr key={key}>
                            <td>{sale_id}</td>
                            <td>{key}</td>

                            <td>{value}</td>

                            <td>1</td>
                            <td>{1 * parseInt(value)}</td>
                        </tr>
                        )
                    }
                    
                );
                setCartList(cartItems);
            }
            setTotal(temp_total);
        });
    }, [total]);

    useEffect(() => {
        if (dataList) {
            const cartItems = Object.entries(dataList).map(([key, value]) => 
            {
                confirmSales(sale_id, key);
            })
        }
    }, [dataList])
    

    return (
        <CartPageStyle>
        <table>
        <tr>
           <th>Sale ID</th>
           <th>Title</th> 
           <th>Quantity</th> 
           <th>Price</th> 
           <th>Total Price</th> 
        </tr>
        {cartList}
        </table>
        <h3 class="payment-total">Payment Total: {total}</h3>
        </CartPageStyle>
    )

}

export default ConfirmationPage;