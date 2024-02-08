import { CartButtonStyle } from "../styles/Other.style"
function CartButton({title, action, displayName, onClick = null}) {

    const buttonClick = async e => {
        try {
            const response = await fetch(`/cha-movies/api/cart`, {
            method: 'POST',
            body: `item=${title}&action=${action}`,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        })

            if (response.status == 200) {
                alert("Action successful! ", action)
            }
            else {
                alert("action failed! ", action)
            }
        }
        catch (e) {
            console.log("Error when adding item to cart: ", error);
            alert("Successful ", action)
        }
    }
    return (
        <CartButtonStyle>
        <button onClick={() => {
            buttonClick()
            if (onClick) {onClick()}
            }}>{displayName}</button>
        </CartButtonStyle>
    )
    
}

export default CartButton