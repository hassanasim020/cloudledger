import { useEffect, useMemo, useState } from 'react';
import { Activity, ArrowRight, Building2, CircleDollarSign, Cloud, RefreshCw, ShieldCheck } from 'lucide-react';
import { api } from './api';
import type { Account, Transaction } from './types';

const money = (value: number, currency = 'PKR') => new Intl.NumberFormat('en-PK', {
  style: 'currency', currency, maximumFractionDigits: 0
}).format(value);

export default function App() {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [form, setForm] = useState({ sourceAccountId: '', destinationAccountId: '', amount: '' });

  const load = async () => {
    setLoading(true); setError('');
    try {
      const [accountData, transactionData] = await Promise.all([api.accounts(), api.transactions()]);
      setAccounts(accountData); setTransactions(transactionData);
      setForm(f => ({ ...f, sourceAccountId: f.sourceAccountId || accountData[0]?.id || '', destinationAccountId: f.destinationAccountId || accountData[1]?.id || '' }));
    } catch (e) { setError(e instanceof Error ? e.message : 'Unable to load dashboard'); }
    finally { setLoading(false); }
  };
  useEffect(() => { void load(); }, []);

  const totalBalance = useMemo(() => accounts.reduce((sum, item) => sum + Number(item.balance), 0), [accounts]);
  const submit = async (event: React.FormEvent) => {
    event.preventDefault(); setError('');
    try {
      await api.transfer({ sourceAccountId: form.sourceAccountId, destinationAccountId: form.destinationAccountId, amount: Number(form.amount), currency: 'PKR' });
      setForm(f => ({ ...f, amount: '' })); await load();
    } catch (e) { setError(e instanceof Error ? e.message : 'Transfer failed'); }
  };

  return <div className="app-shell">
    <aside>
      <div className="brand"><span><Cloud size={22} /></span>CloudLedger</div>
      <nav><a className="active"><Activity size={18}/>Overview</a><a><Building2 size={18}/>Accounts</a><a><CircleDollarSign size={18}/>Transactions</a></nav>
      <div className="security"><ShieldCheck size={22}/><div><strong>Secure by design</strong><small>Containerized services</small></div></div>
    </aside>
    <main>
      <header><div><p className="eyebrow">OPERATIONS CONSOLE</p><h1>Financial overview</h1><p>Monitor accounts and execute internal transfers.</p></div><button className="icon-button" onClick={() => void load()} aria-label="Refresh"><RefreshCw size={18}/></button></header>
      {error && <div className="alert">{error}</div>}
      <section className="metrics">
        <article><span>Total balance</span><strong>{money(totalBalance)}</strong><small>Across {accounts.length} active accounts</small></article>
        <article><span>Transactions</span><strong>{transactions.length}</strong><small>Recorded in the ledger</small></article>
        <article><span>System status</span><strong className="healthy"><i/>Operational</strong><small>API and database healthy</small></article>
      </section>
      <section className="grid">
        <article className="panel accounts"><div className="panel-head"><div><h2>Accounts</h2><p>Managed corporate accounts</p></div></div>
          {loading ? <p>Loading accounts…</p> : accounts.map(a => <div className="account" key={a.id}><div className="avatar">{a.ownerName[0]}</div><div><strong>{a.ownerName}</strong><small>{a.accountNumber}</small></div><div className="balance"><strong>{money(Number(a.balance), a.currency)}</strong><small>{a.status}</small></div></div>)}
        </article>
        <article className="panel transfer"><div className="panel-head"><div><h2>New transfer</h2><p>Move funds between internal accounts</p></div></div>
          <form onSubmit={submit}><label>From<select value={form.sourceAccountId} onChange={e => setForm({...form, sourceAccountId: e.target.value})}>{accounts.map(a => <option key={a.id} value={a.id}>{a.ownerName}</option>)}</select></label>
          <label>To<select value={form.destinationAccountId} onChange={e => setForm({...form, destinationAccountId: e.target.value})}>{accounts.map(a => <option key={a.id} value={a.id}>{a.ownerName}</option>)}</select></label>
          <label>Amount (PKR)<input type="number" min="1" required placeholder="25,000" value={form.amount} onChange={e => setForm({...form, amount: e.target.value})}/></label>
          <button type="submit">Submit transfer <ArrowRight size={17}/></button></form>
        </article>
      </section>
      <section className="panel ledger"><div className="panel-head"><div><h2>Recent transactions</h2><p>Latest ledger activity</p></div></div>
        <div className="table-wrap"><table><thead><tr><th>Reference</th><th>Date</th><th>Amount</th><th>Status</th></tr></thead><tbody>{transactions.map(t => <tr key={t.id}><td><code>{t.reference}</code></td><td>{new Date(t.createdAt).toLocaleString()}</td><td>{money(Number(t.amount), t.currency)}</td><td><span className="status">{t.status}</span></td></tr>)}</tbody></table></div>
      </section>
      <footer>CloudLedger • Portfolio project by Muhammad Hassan Asim</footer>
    </main>
  </div>;
}

