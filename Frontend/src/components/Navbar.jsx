import React from 'react';
import { Link } from 'react-router-dom';
import { Bars3Icon } from '@heroicons/react/24/outline';
import logo from '../../public/logo.svg';

export default function Navbar({ onSidebarToggle }) {
  return (
    <nav className="fixed top-0 left-0 right-0 bg-white shadow z-20">
      <div className="w-full px-4 sm:px-6 lg:px-8 flex items-center h-16">
        {/* Sidebar toggle */}
        <button
          onClick={onSidebarToggle}
          className="mr-4 p-2 rounded-md hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-indigo-500"
        >
          <Bars3Icon className="h-6 w-6 text-gray-700" />
        </button>
        {/* Logo */}
        <Link to="/" className="flex-shrink-0">
          <img src={logo} alt="HR Portal" className="h-8 w-auto" />
        </Link>
      </div>
    </nav>
  );
}