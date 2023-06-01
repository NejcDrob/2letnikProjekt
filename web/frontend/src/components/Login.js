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
    <form onSubmit={handleLogin}>
      {userContext.user ? <Navigate replace to="/" /> : ''}
      <div className="row g-2 align-items-center">
        <div className="col-auto">
          <input
            type="text"
            name="username"
            className="form-control"
            style={{ width: '150px' }}
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        <div className="col-auto">
          <input
            type="password"
            name="password"
            className="form-control"
            style={{ width: '150px' }}
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
      </div>
      <button type="submit" className="btn btn-primary mt-3">
        Log in
      </button>
      <div className="text-danger mt-2">{error}</div>
    </form>
  );
}

export default Login;
