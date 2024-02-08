import {useState, useEffect} from 'react'
import { LoginStyle } from '../styles/Other.style';
import React from 'react';
import ReCAPTCHA from "react-google-recaptcha";

function ELoginPage() {
    const [email, setEmail] = useState();
    const [password, setPassword] = useState();
    const [recaptchaResponse, setRecaptchaResponse] = useState();
    const recaptchaRef = React.createRef();
    
    const recaptcha = <ReCAPTCHA
                    ref={recaptchaRef}
                    sitekey="6Lcx-fooAAAAANNpZopdRDN4POr7t89fMBc2yxHP"
                    onChange={() => {
                        const recaptchaValue = recaptchaRef.current.getValue();
                        setRecaptchaResponse(recaptchaValue)
                    }}
                  />

    useEffect(() => {
        var checkout = document.getElementById('checkout-button');
        if (checkout != null) {checkout.style.display = 'none';}
    }, [])


    const submitHandler = async e => {
        e.preventDefault();
        let urlPrefix = import.meta.env.VITE_URL_PREFIX 
        let apiLink = `${urlPrefix}` + '/cha-movies/api/elogin'
        recaptchaRef.current.reset();
        try {
            const response = await fetch(apiLink, {
                method: 'POST',
                body: `email=${email}&password=${password}&g-recaptcha-response=${recaptchaResponse}`,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                }
            })
            
            if (response.status == 200) {
                window.location.href = "/cha-movies/_dashboard"
            }
            else {
                let data = await response.json()
                alert(data["errorMessage"])
            }

        } catch (error) {
            console.log("Error when sending login info: ", error)
        }
    }


    return (
        <LoginStyle>
            <form name="login" onSubmit={submitHandler}>
                Email: <input type="text" name="email" onChange={e => setEmail(e.target.value)}/> 
                <br/>
                
                Password: <input type="password" name="password" onChange={e => setPassword(e.target.value)}/> 
                <br/>
                {recaptcha}
                <input type="submit" value="Login" />
            </form>
        </LoginStyle>

    )
}

export default ELoginPage;