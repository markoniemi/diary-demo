import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.tsx';
import { AuthProvider, AuthProviderProps } from "react-oidc-context";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const oidcConfig: AuthProviderProps = {
  authority: "http://localhost:9000",
  client_id: "diary-client",
  redirect_uri: window.location.origin + "/login/oauth2/code/diary-client",
  post_logout_redirect_uri: window.location.origin,
  scope: "openid profile diary.read diary.write",
  onSigninCallback: (_user) => {
      window.history.replaceState({}, document.title, window.location.pathname);
  }
};

const queryClient = new QueryClient();

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);
root.render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <AuthProvider {...oidcConfig}>
        <App />
      </AuthProvider>
    </QueryClientProvider>
  </React.StrictMode>
);
