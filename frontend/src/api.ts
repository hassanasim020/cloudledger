import type { Account, Transaction } from './types';
const BASE = import.meta.env.VITE_API_URL || '/api/v1';
async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${BASE}${path}`, { headers: { 'Content-Type': 'application/json' }, ...init });
  if (!response.ok) {
    const body = await response.json().catch(() => ({ message: 'Request failed' }));
    throw new Error(body.message || 'Request failed');
  }
  return response.json();
}
export const api = {
  accounts: () => request<Account[]>('/accounts'),
  transactions: () => request<Transaction[]>('/transactions'),
  transfer: (payload: { sourceAccountId: string; destinationAccountId: string; amount: number; currency: string }) =>
    request<Transaction>('/transactions/transfer', { method: 'POST', body: JSON.stringify(payload) })
};

