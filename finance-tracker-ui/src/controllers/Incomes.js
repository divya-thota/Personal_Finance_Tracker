const fetchAllIncomes = async () => {
  const response = await fetch("/database/all_incomes");
  const data = await response.json();
  return data?.map((income) => ({
    id: income.income_id,
    reviewed: income.reviewed,
    date: income.income_date,
    amount: `$ ${income.amount}`,
    description: income.description,
    category: income.category_id?.category_id || null,
  }));
};

const fetchUnreviewedIncomes = async () => {
  const response = await fetch("/database/unreviewed_incomes");
  const unreviewedIncomes = await response.json();
  return unreviewedIncomes?.map((income) => ({
    id: income.income_id,
    reviewed: income.reviewed,
    date: income.income_date,
    amount: `$ ${income.amount}`,
    description: income.description,
    category: income.category_id?.category_id || null,
  }));
};

export { fetchAllIncomes, fetchUnreviewedIncomes };