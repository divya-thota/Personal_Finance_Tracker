  const fetchExpenseCategories = async () => {
    const response = await fetch("/database/categories/Expense");
    const categories = await response.json();
    return categories.map((category) => ({
      key: category.category_id,
      value: category.name,
    }));
  };

  const fetchIncomeCategories = async () => {
      const response = await fetch("/database/categories/Income");
      const categories = await response.json();
      return categories.map((category) => ({
        key: category.category_id,
        value: category.name,
      }));
    };

  export { fetchExpenseCategories, fetchIncomeCategories };