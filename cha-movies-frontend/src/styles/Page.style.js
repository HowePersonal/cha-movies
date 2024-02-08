import styled from 'styled-components'

export const CartTDStyle = styled.div`
    display: flex;
    align-items: center;
`

export const CartPageStyle = styled.div`
    table {
        width: 80%;
        border-collapse: collapse;
        margin: 20px 0;
    }

    table, th, td {
        border: 1px solid #dcdcdc;
    }

    th, td {
        padding: 8px 12px;
    }
    
    th {
        background-color: #f7f7f7;
    }
`

export const CartPageButtonStyle = styled.div`
    button {
        background-color: black;
        color: #ffffff;            
        border: none;              
        padding: 5px 8px;       
        font-size: 10px;              
        border-radius: 3px;        
        outline: none;                     
    }
`

export const PaymentPageStyle = styled.div`
    form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        width: 500px;
        margin: 50px auto;
        padding: 20px;
        border: 1px solid #ccc;
        border-radius: 5px;
    }

`