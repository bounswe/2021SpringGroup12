import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

// Testing library JEST => for more info https://github.com/testing-library/jest-dom
test('renders learn react link', () => {
  render(<App />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});
