import styled from 'styled-components'

export const LoginStyle = styled.div` 
    max-width: 300px;
    margin: 50px auto;
    padding: 20px;
    border: 1px solid #ccc;
    height: 30vh;
    border-radius: 5px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    background-color: #f7f7f7;

    form {
        display: flex;
        flex-direction: column;
        gap: 5px;
        
        .info-box {
            width: 100%;
        }
        
        input {
            border: 1px solid grey;
            border-radius: 5px;
            font-size: 15px;
            height: 25px;
        }

        input[type="submit"] {
            
            background-color: #DEC20B;
        }
    }



`

export const CartButtonStyle = styled.div`
button {
    background-color: #4CAF50;
    color: #ffffff;            
    border: none;              
    padding: 12px 24px;       
    font-size: 18px;              
    border-radius: 5px;        
    transition: background-color 0.3s, transform 0.3s;  
    outline: none;             
    box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.15); 
    font-weight: 600;          
}
`

export const ActorLinkStyle = styled.div`
    white-space: nowarp;    
`

export const PageOptionStyle = styled.div`
    display: flex;
    gap: 40px;
    margin: 10px 0 10px 15px;

    h4 {
        font-size: 12px;
        margin: 0;
    }

    select {
        border-radius: 5px;
        background-color: #FFFFFF;
    }
`