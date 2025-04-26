import React, { useState } from 'react';
import Navbar from './Navbar';
import Sidebar from './Sidebar';

export default function Layout({ children }) {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  return (
    <div className="flex flex-1 min-h-screen">
      <Sidebar isOpen={sidebarOpen} />
      <div className={`
          flex-1 flex flex-col transition-margin duration-300 ease-in-out
          ${sidebarOpen ? 'ml-64' : 'ml-0'}
        `}>
        <Navbar onSidebarToggle={() => setSidebarOpen(!sidebarOpen)} />
        <main className="flex-1 overflow-auto pt-16 w-full">
          {children}
        </main>
      </div>
    </div>
  );
}
