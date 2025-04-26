// src/pages/Home.jsx
import React from 'react';
import { Link } from 'react-router-dom';

export default function Home() {
  return (
    <>
      <header className="text-center py-20">
        <h1 className="text-4xl font-extrabold">Welcome to the HR Portal</h1>
        <p className="mt-4 text-lg text-gray-600">
          Manage employees, departments, salaries &amp; more.
        </p>
        <div className="mt-8">
          <Link
            to="/dashboard"
            className="px-6 py-3 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700"
          >
            Go to Dashboard
          </Link>
        </div>
      </header>

      <section className="max-w-4xl mx-auto grid md:grid-cols-2 gap-8 p-4">
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-2xl font-bold mb-2">Employee Management</h2>
          <p className="text-gray-600">
            View, add, edit or remove employee records.
          </p>
          <Link to="/employees" className="text-indigo-600 mt-4 inline-block">
            Manage Employees →
          </Link>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-2xl font-bold mb-2">Department Management</h2>
          <p className="text-gray-600">
            Assign employees to departments and managers.
          </p>
          <Link to="/departments" className="text-indigo-600 mt-4 inline-block">
            Manage Departments →
          </Link>
        </div>
        {/* add cards for salaries, titles, etc. */}
      </section>
    </>
  );
}
