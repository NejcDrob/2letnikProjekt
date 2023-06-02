import { useContext, useState } from 'react';
import { UserContext } from '../userContext';
import { Navigate } from 'react-router-dom';


function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const userContext = useContext(UserContext);

  async function handleLogin(e) {
    e.preventDefault();
    const res = await fetch('http://localhost:3001/users/login', {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: username,
        password: password,
      }),
    });
    const data = await res.json();
    if (data._id !== undefined) {
      userContext.setUserContext(data);
    } else {
      setUsername('');
      setPassword('');
      setError('Invalid username or password');
    }
  }

  return (
    <div className="login-container">
      {userContext.user ? <Navigate replace to="/" /> : ''}
      <div className="box-container">
        <form onSubmit={handleLogin} className="login-form">
          <input
            type="text"
            name="username"
            className="form-control"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <input
            type="password"
            name="password"
            className="form-control"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <button type="submit" className="btn btn-primary">
            Log in
          </button>
          <div className="text-danger">{error}</div>
        </form>
      </div>
    </div>
  );
}

export default Login;
