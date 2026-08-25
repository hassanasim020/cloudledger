export type Account = {
  id: string; ownerName: string; accountNumber: string; balance: number;
  currency: string; status: 'ACTIVE' | 'FROZEN' | 'CLOSED'; createdAt: string;
};
export type Transaction = {
  id: string; sourceAccountId: string; destinationAccountId: string; amount: number;
  currency: string; status: 'COMPLETED' | 'FAILED'; reference: string; createdAt: string;
};

