
const fetchAllExpenses = async () => {
  const response = await fetch("/database/all_expenses");
  const allExpenses = await response.json();
  return allExpenses?.map((expense) => ({
    id: expense.expense_id,
    reviewed: expense.reviewed,
    date: expense.expense_date,
    amount: `$ ${expense.amount}`,
    description: expense.description,
    category: expense.category_id?.category_id || null,
  }));
};

const fetchUnreviewedExpenses = async () => {
  const response = await fetch("/database/unreviewed_expenses");
  const unreviewedExpenses = await response.json();
  return unreviewedExpenses?.map((expense) => ({
    id: expense.expense_id,
    reviewed: expense.reviewed,
    date: expense.expense_date,
    amount: `$ ${expense.amount}`,
    description: expense.description,
    category: expense.category_id?.category_id || null,
  }));
};

const fetchSplitwiseExpenses = async () => {
  const response = await fetch("/api/expenses");
  const splitwiseExpenses = await response.json();
  return splitwiseExpenses?.map((expense) => ({
    AddedDate: expense.date.slice(0,10),
    PayerName: expense.payer_name,
    Amount: `$ ${expense.amount}`,
    Description: expense.description,
    OwedShare: expense.owed_share,
  }));
};

export { fetchAllExpenses, fetchUnreviewedExpenses, fetchSplitwiseExpenses };