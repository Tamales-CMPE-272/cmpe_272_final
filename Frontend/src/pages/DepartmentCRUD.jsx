

import React, { useEffect, useState } from 'react';
const API = import.meta.env.VITE_API_BASE_URL;

function DepartmentCRUD() {
  const [departments, setDepartments] = useState([]);
  const [form, setForm] = useState({
    dept_no: '',
    dept_name: ''
  });
  const [errors, setErrors] = useState({});
  const [isEditing, setIsEditing] = useState(false);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);

  const fetchDepartments = () => {
    fetch(`${API}/departments?page=${page}`)
      .then(res => res.json())
      .then(result => {
        const list = result.data || result;
        setDepartments(list);
        if (result.meta) {
          setTotalPages(result.meta.totalPages);
        }
      })
      .catch(console.error);
  };

  useEffect(() => {
    fetchDepartments();
  }, [page]);

  const validateForm = () => {
    const errs = {};
    if (!form.dept_no) {
      errs.dept_no = 'Department number is required';
    } else if (!/^d\d+$/.test(form.dept_no)) {
      errs.dept_no = 'Department number must start with "d" followed by digits';
    }
    if (!form.dept_name) {
      errs.dept_name = 'Department name is required';
    }
    return errs;
  };

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setErrors({});
  }

  const handleSubmit = e => {
    e.preventDefault();
    const errs = validateForm();
    if (Object.keys(errs).length) {
      setErrors(errs);
      return;
    }
    setErrors({});
    const url = isEditing
      ? `${API}/departments/${form.dept_no}`
      : `${API}/departments`;
    const method = isEditing ? 'PUT' : 'POST';
    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form),
    })
      .then(() => {
        setForm({ dept_no: '', dept_name: '' });
        setIsEditing(false);
        fetchDepartments();
      })
      .catch(console.error);
  };

  const handleEdit = dept => {
    setForm(dept);
    setIsEditing(true);
  };

  const handleDelete = dept_no => {
    fetch(`${API}/departments/${dept_no}`, { method: 'DELETE' })
      .then(() => fetchDepartments())
      .catch(console.error);
  };

  return (
    <div className="max-w-4xl mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Department</h1>

      <div className="bg-white p-6 rounded-lg shadow-md mb-6">
        <form onSubmit={handleSubmit} className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-semibold text-gray-600 uppercase mb-1">
              Dept No
            </label>
            <input
              name="dept_no"
              placeholder="Department No"
              value={form.dept_no}
              onChange={handleChange}
              className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder-gray-400"
            />
            {errors.dept_no && <p className="text-red-600 text-xs mt-1">{errors.dept_no}</p>}
          </div>
          <div>
            <label className="block text-xs font-semibold text-gray-600 uppercase mb-1">
              Dept Name
            </label>
            <input
              name="dept_name"
              placeholder="Department Name"
              value={form.dept_name}
              onChange={handleChange}
              className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder-gray-400"
            />
            {errors.dept_name && <p className="text-red-600 text-xs mt-1">{errors.dept_name}</p>}
          </div>
          <div className="sm:col-span-2 flex space-x-4 pt-4">
            <button
              type="submit"
              disabled={Object.keys(errors).length > 0}
              className="inline-flex items-center px-4 py-2 bg-blue-600 text-white text-sm font-medium rounded-md hover:bg-blue-700 disabled:opacity-50"
            >
              {isEditing ? 'Update' : 'Create'}
            </button>
            <button
              type="button"
              onClick={() => {
                setForm({ dept_no: '', dept_name: '' });
                setIsEditing(false);
              }}
              className="inline-flex items-center px-4 py-2 bg-gray-200 text-gray-700 text-sm font-medium rounded-md hover:bg-gray-300"
            >
              Clear
            </button>
          </div>
        </form>
      </div>

      <table className="w-full border-collapse border border-gray-300">
        <thead>
          <tr className="bg-gray-100">
            <th className="border border-gray-300 px-3 py-1 text-left">Dept No</th>
            <th className="border border-gray-300 px-3 py-1 text-left">Dept Name</th>
            <th className="border border-gray-300 px-3 py-1 text-left">Actions</th>
          </tr>
        </thead>
        <tbody>
          {departments.map(dept => (
            <tr
              key={dept.dept_no}
              className="hover:bg-gray-50 cursor-pointer"
            >
              <td className="border border-gray-300 px-3 py-1">{dept.dept_no}</td>
              <td className="border border-gray-300 px-3 py-1">{dept.dept_name}</td>
              <td className="border border-gray-300 px-3 py-1 space-x-2">
                <button
                  onClick={e => { e.stopPropagation(); handleEdit(dept); }}
                  className="bg-yellow-400 text-white px-2 py-1 rounded hover:bg-yellow-500"
                >
                  Edit
                </button>
                <button
                  onClick={e => { e.stopPropagation(); handleDelete(dept.dept_no); }}
                  className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="flex justify-between items-center mt-4 space-x-2">
        {/* First Page */}
        <button
          onClick={() => setPage(1)}
          disabled={page <= 1}
          className="px-3 py-1 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 disabled:opacity-50"
        >
          &laquo; First
        </button>
        {/* Previous Page */}
        <button
          onClick={() => setPage(p => Math.max(p - 1, 1))}
          disabled={page <= 1}
          className="px-3 py-1 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 disabled:opacity-50"
        >
          &lsaquo; Previous
        </button>
        {/* Page Indicator */}
        <span className="text-sm text-gray-600">
          Page {page} of {totalPages}
        </span>
        {/* Next Page */}
        <button
          onClick={() => setPage(p => Math.min(p + 1, totalPages))}
          disabled={page >= totalPages}
          className="px-3 py-1 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 disabled:opacity-50"
        >
          Next &rsaquo;
        </button>
        {/* Last Page */}
        <button
          onClick={() => setPage(totalPages)}
          disabled={page >= totalPages}
          className="px-3 py-1 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 disabled:opacity-50"
        >
          Last &raquo;
        </button>
      </div>
    </div>
  );
}

export default DepartmentCRUD;