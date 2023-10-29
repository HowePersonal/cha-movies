import {useState, useEffect} from 'react'

function LoginPage() {
    const [username, setUsername] = useState();
    const [password, setPassword] = useState();

    const submitHandler = async e => {
        e.preventDefault();
        
        try {
            
            const response = await fetch('http://localhost:8000/cha-movies/api/login', {
                method: 'POST',
                body: `username=${username}&password=${password}`,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                }
            })
            

        } catch (error) {
            console.log("Error when sending login info: ", error)
        }
    }


    return (
        <>
            <form name="login" onSubmit={submitHandler}>
                Username: <input type="text" name="username" onChange={e => setUsername(e.target.value)}/> <br/>
                Password: <input type="password" name="password" onChange={e => setPassword(e.target.value)}/> <br/>
                <input type="submit" value="Login" />
            </form>
        </>

    )
}

export default LoginPage;