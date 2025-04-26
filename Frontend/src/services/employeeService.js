const API_BASE = 'http://localhost:8080/tamalesHr/employees';

export const getEmployees = async () => {
  const res = await fetch(API_BASE);
  return res.json();
};

export const createEmployee = async (data) => {
  const res = await fetch(API_BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });
  return res.json();
};