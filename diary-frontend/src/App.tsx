import React from 'react';
import { useAuth } from "react-oidc-context";
import DiaryEntries from './components/features/DiaryEntries';
import { Container, Button, Spinner, Alert } from 'react-bootstrap';

function App() {
  const auth = useAuth();

  if (auth.isLoading) {
    return (
      <Container className="mt-5 text-center">
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Loading...</span>
        </Spinner>
      </Container>
    );
  }

  if (auth.error) {
    return (
      <Container className="mt-5">
        <Alert variant="danger">
          Oops... {auth.error.message}
        </Alert>
      </Container>
    );
  }

  if (auth.isAuthenticated) {
    return (
      <Container className="mt-5">
        <div className="d-flex justify-content-between align-items-center mb-4">
            <h1>My Diary</h1>
            <div>
                <span className="me-3">Hello, {auth.user?.profile.sub}</span>
                <Button variant="outline-danger" onClick={() => auth.removeUser()}>
                    Log out
                </Button>
            </div>
        </div>
        <DiaryEntries token={auth.user?.access_token || ''} />
      </Container>
    );
  }

  return (
    <Container className="mt-5 text-center">
        <h1>Welcome to Diary App</h1>
        <Button variant="primary" className="mt-3" onClick={() => auth.signinRedirect()}>
            Log in
        </Button>
    </Container>
  );
}

export default App;
