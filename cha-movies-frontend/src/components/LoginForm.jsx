import { LoginStyle } from '../styles/Other.style';

function LoginForm({submitHandler, setEmail, setPassword, recaptcha}) {
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
}

export default LoginForm;