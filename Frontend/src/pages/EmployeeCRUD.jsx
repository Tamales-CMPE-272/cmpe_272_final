import React, { useEffect, useState } from 'react';
const API = import.meta.env.VITE_API_BASE_URL;

function EmployeeCRUD() {
    const [isEditing, setIsEditing] = useState(false);
    const [employees, setEmployees] = useState([]);
    const [form, setForm] = useState({
        emp_no: '',
        first_name: '',
        last_name: '',
        gender: '',
        hire_date: '',
        birth_date: ''
    });
    const [errors, setErrors] = useState({});

    // 1. Fetch all employees (with pagination support)
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const fetchEmployees = () => {
        fetch(`${API}/employees?page=${page}`)
            .then(res => res.json())
            .then(result => {
                setEmployees(result.data || []);
                if (result.meta) setTotalPages(result.meta.totalPages);
            })
            .catch(console.error);
    };
    useEffect(() => {
        fetchEmployees();
    }, [page]);

    const validateForm = () => {
      const errs = {};
      if (!form.emp_no) errs.emp_no = 'Employee number is required';
      if (!form.first_name) errs.first_name = 'First name is required';
      if (!form.last_name) errs.last_name = 'Last name is required';
      if (!form.gender) errs.gender = 'Gender is required';
      if (!form.hire_date) errs.hire_date = 'Hire date is required';
      if (!form.birth_date) errs.birth_date = 'Birth date is required';
      return errs;
    };

    // 2. Handlers: create (POST) or update (PUT)
    const handleChange = e => {
        setForm({ ...form, [e.target.name]: e.target.value });
        setErrors({})
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
          ? `${API}/employees/${form.emp_no}`
          : `${API}/employees`;
        const method = isEditing ? 'PUT' : 'POST';
        fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(form),
        })
            .then(() => {
                setForm({ emp_no: '', first_name: '', last_name: '', gender: '', hire_date: '', birth_date: '' });
                setIsEditing(false);
                fetchEmployees();
            })
            .catch(console.error);
    };

    // 3. Edit and Delete
    const handleEdit = emp => {
      setForm(emp);
      setIsEditing(true);
    };

    const handleDelete = emp_no => {
        fetch(`${API}/employees/${emp_no}`, { method: 'DELETE' })
            .then(() => {
                fetchEmployees();
            })
            .catch(console.error);
    };

    return (
        <div className="max-w-4xl mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">Employee</h1>

            <div className="bg-white p-6 rounded-lg shadow-md mb-6">
                <form onSubmit={handleSubmit} className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <div>
                        <label className="block text-xs font-semibold text-gray-600 uppercase mb-1">Employee No</label>
                        <input
                            name="emp_no"
                            placeholder="Emp No"
                            value={form.emp_no}
                            onChange={handleChange}
                            className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder-gray-400"
                        />
                        {errors.emp_no && <p className="text-red-600 text-xs mt-1">{errors.emp_no}</p>}
                    </div>
                    <div>
                        <label className="block text-xs font-semibold text-gray-600 uppercase mb-1">First Name</label>
                        <input
                            name="first_name"
                            placeholder="First Name"
                            value={form.first_name}
                            onChange={handleChange}
                            className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder-gray-400"
                        />
                        {errors.first_name && <p className="text-red-600 text-xs mt-1">{errors.first_name}</p>}
                    </div>
                    <div>
                        <label className="block text-xs font-semibold text-gray-600 uppercase mb-1">Last Name</label>
                        <input
                            name="last_name"
                            placeholder="Last Name"
                            value={form.last_name}
                            onChange={handleChange}
                            className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder-gray-400"
                        />
                        {errors.last_name && <p className="text-red-600 text-xs mt-1">{errors.last_name}</p>}
                    </div>
                    <div>
                        <label className="block text-xs font-semibold text-gray-600 uppercase mb-1">Gender</label>
                        <select
                            name="gender"
                            value={form.gender}
                            onChange={handleChange}
                            className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder-gray-400"
                        >
                            <option value="">Gender</option>
                            <option value="M">M</option>
                            <option value="F">F</option>
                        </select>
                        {errors.gender && <p className="text-red-600 text-xs mt-1">{errors.gender}</p>}
                    </div>
                    <div>
                        <label className="block text-xs font-semibold text-gray-600 uppercase mb-1">Birth Date</label>
                        <input
                            type="date"
                            name="birth_date"
                            value={form.birth_date}
                            onChange={handleChange}
                            className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder-gray-400"
                        />
                        {errors.birth_date && <p className="text-red-600 text-xs mt-1">{errors.birth_date}</p>}
                    </div>
                    <div>
                        <label className="block text-xs font-semibold text-gray-600 uppercase mb-1">Hire Date</label>
                        <input
                            type="date"
                            name="hire_date"
                            value={form.hire_date}
                            onChange={handleChange}
                            className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder-gray-400"
                        />
                        {errors.hire_date && <p className="text-red-600 text-xs mt-1">{errors.hire_date}</p>}
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
                                setForm({ emp_no: '', first_name: '', last_name: '', gender: '', hire_date: '', birth_date: '' });
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
                    <th className="border border-gray-300 px-3 py-1 text-left">Emp No</th>
                    <th className="border border-gray-300 px-3 py-1 text-left">First</th>
                    <th className="border border-gray-300 px-3 py-1 text-left">Last</th>
                    <th className="border border-gray-300 px-3 py-1 text-left">Gender</th>
                    <th className="border border-gray-300 px-3 py-1 text-left">Hire Date</th>
                    <th className="border border-gray-300 px-3 py-1 text-left">Actions</th>
                </tr>
                </thead>
                <tbody>
                {employees.map(emp => (
                    <tr
                        key={emp.emp_no}
                        className="hover:bg-gray-50 cursor-pointer"
                    >
                        <td className="border border-gray-300 px-3 py-1">{emp.emp_no}</td>
                        <td className="border border-gray-300 px-3 py-1">{emp.first_name}</td>
                        <td className="border border-gray-300 px-3 py-1">{emp.last_name}</td>
                        <td className="border border-gray-300 px-3 py-1">{emp.gender}</td>
                        <td className="border border-gray-300 px-3 py-1">{emp.hire_date}</td>
                        <td className="border border-gray-300 px-3 py-1 space-x-2">
                            <button
                                onClick={() => handleEdit(emp)}
                                className="bg-yellow-400 text-white px-2 py-1 rounded hover:bg-yellow-500"
                            >
                                Edit
                            </button>
                            <button
                                onClick={() => handleDelete(emp.emp_no)}
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

export default EmployeeCRUD;