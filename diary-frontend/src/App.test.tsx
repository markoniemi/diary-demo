import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import App from './App.tsx';
import { AuthProvider } from "react-oidc-context";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const queryClient = new QueryClient();

describe('App', () => {
  it('renders login button when not authenticated', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <AuthProvider>
          <App />
        </AuthProvider>
      </QueryClientProvider>
    );
    const linkElement = screen.getByText(/Log in/i);
    expect(linkElement).toBeInTheDocument();
  });
});
