import { render, screen } from '@testing-library/react';
import { vi } from 'vitest';
import App from './App';
vi.stubGlobal('fetch', vi.fn(() => Promise.resolve({ ok: true, json: () => Promise.resolve([]) })));
test('renders the operations dashboard', async () => {
  render(<App />);
  expect(screen.getByText('Financial overview')).toBeInTheDocument();
  expect(screen.getByText('New transfer')).toBeInTheDocument();
});
