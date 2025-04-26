import React from 'react';
import { Link } from 'react-router-dom';

export default function Sidebar({ isOpen }) {
  return (
    <aside
      className={`
        fixed top-16 bottom-0 left-0 z-40 w-64 bg-white border-r
        transform transition-transform duration-300 ease-in-out
        ${isOpen ? 'translate-x-0' : '-translate-x-full'}
      `}
    >
      <nav className="mt-4 flex flex-col space-y-4 px-6">
        <Link to="/" className="py-2 font-semibold hover:text-indigo-600">Home</Link>
        <Link to="/dashboard" className="py-2 font-semibold hover:text-indigo-600">Dashboard</Link>
        <Link to="/employees" className="py-2 font-semibold hover:text-indigo-600">Employees</Link>
        <Link to="/departments" className="py-2 font-semibold hover:text-indigo-600">Departments</Link>
        {/* Add more links */}
      </nav>
    </aside>
  );
}
