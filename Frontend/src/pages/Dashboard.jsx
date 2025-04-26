import React, { useEffect, useState } from 'react';
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
} from 'chart.js';
import { Pie, Bar } from 'react-chartjs-2';

ChartJS.register(
  ArcElement,
  Tooltip,
  Legend,
  CategoryScale,
  LinearScale,
  BarElement,
  Title
);

export default function Dashboard() {
  // State for metrics
  const [employeesCount, setEmployeesCount] = useState(0);
  const [departmentsCount, setDepartmentsCount] = useState(0);
  const [avgSalary, setAvgSalary] = useState(0);
  const [totalSalaries, setTotalSalaries] = useState(0);
  const [titlesCount, setTitlesCount] = useState(0);
  const [genderRatio, setGenderRatio] = useState({ M: 0, F: 0 });
  const [avgTenure, setAvgTenure] = useState(0);
  const [topDepartment, setTopDepartment] = useState('');
  const [newHiresLastMonth, setNewHiresLastMonth] = useState(0);
  const [deptCountsMap, setDeptCountsMap] = useState({});

  const API = import.meta.env.VITE_API_BASE_URL;

  useEffect(() => {
    // Fetch employees
    fetch(`${API}/employees`)
      .then(res => res.json())
      .then(data => {
        const list = data.data || data;
        setEmployeesCount(list.length);
        const genders = list.reduce((acc, emp) => {
          acc[emp.gender] = (acc[emp.gender] || 0) + 1;
          return acc;
        }, {});
        setGenderRatio(genders);
        // Tenure: assume hire_date exists
        const now = new Date();
        const totalYears = list.reduce((sum, emp) => {
          const hire = new Date(emp.hire_date);
          return sum + (now - hire) / (1000 * 60 * 60 * 24 * 365);
        }, 0);
        setAvgTenure((totalYears / list.length).toFixed(1));
        // New hires last month
        const oneMonthAgo = new Date();
        oneMonthAgo.setMonth(oneMonthAgo.getMonth() - 1);
        setNewHiresLastMonth(
          list.filter(emp => new Date(emp.hire_date) > oneMonthAgo).length
        );
      });

    // Fetch departments
    fetch(`${API}/departments`)
      .then(res => res.json())
      .then(data => setDepartmentsCount(data.data.length));

    // Fetch salaries
    fetch(`${API}/salaries`)
      .then(res => res.json())
      .then(data => {
        const rows = data.data;
        const total = rows.reduce((sum, s) => sum + parseFloat(s.salary), 0);
        setTotalSalaries(total.toLocaleString());
        setAvgSalary((total / rows.length).toFixed(2));
      });

    // Fetch titles
    fetch(`${API}/titles`)
      .then(res => res.json())
      .then(data => setTitlesCount(data.data.length));

    // Determine top department by employee count
    fetch(`${API}/dept_emp`)
      .then(res => res.json())
      .then(data => {
        const rows = data.data;
        const counts = rows.reduce((acc, r) => {
          acc[r.dept_no] = (acc[r.dept_no] || 0) + 1;
          return acc;
        }, {});
        setDeptCountsMap(counts);
        const top = Object.entries(counts).sort((a, b) => b[1] - a[1])[0];
        setTopDepartment(top ? `${top[0]} (${top[1]} emp)` : '');
      });
  }, [API]);

  const cards = [
    { title: 'Total Employees', value: employeesCount },
    { title: 'Total Departments', value: departmentsCount },
    { title: 'Average Salary', value: `$${avgSalary}` },
    { title: 'Total Salaries Paid', value: `$${totalSalaries}` },
    { title: 'Total Titles', value: titlesCount },
    { title: 'Avg. Tenure (yrs)', value: avgTenure },
    { title: 'Gender M / F', value: `${genderRatio.M || 0} / ${genderRatio.F || 0}` },
    { title: 'Top Department', value: topDepartment },
    { title: 'New Hires (30d)', value: newHiresLastMonth },
  ];

  // Data for Gender Pie Chart
  const genderData = {
    labels: ['Male', 'Female'],
    datasets: [{
      data: [genderRatio.M || 0, genderRatio.F || 0],
      backgroundColor: ['#36A2EB', '#FF6384'],
    }]
  };

  // Data for Department Bar Chart
  const deptLabels = Object.keys(deptCountsMap);
  const deptValues = deptLabels.map(label => deptCountsMap[label]);
  const deptData = {
    labels: deptLabels,
    datasets: [{
      label: 'Employees per Department',
      data: deptValues,
      backgroundColor: 'rgba(75, 192, 192, 0.6)',
    }]
  };

  // Data for New Hires vs Existing Employees Pie
  const hiresData = {
    labels: ['New Hires (30d)', 'Existing'],
    datasets: [{
      data: [newHiresLastMonth, employeesCount - newHiresLastMonth],
      backgroundColor: ['#4BC0C0', '#FFCE56'],
    }]
  };

  return (
    <main className="p-6">
      <h1 className="text-3xl font-bold mb-6">HR Analytics Dashboard</h1>
      {/* Render the other metric cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
        {cards.map((card, idx) => (
          <div key={idx} className="bg-white p-6 rounded-lg shadow hover:shadow-lg transition">
            <h2 className="text-lg font-medium mb-2">{card.title}</h2>
            <p className="text-2xl font-bold">{card.value}</p>
          </div>
        ))}
      </div>
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mt-8">
        {/* Gender Ratio Pie */}
        <div className="bg-white p-6 rounded-lg shadow w-full max-w-sm mx-auto">
          <h2 className="text-lg font-medium mb-2">Gender Distribution</h2>
          <Pie data={genderData} height={200} width={200} />
        </div>
        {/* Department Headcount Bar */}
        <div className="bg-white p-6 rounded-lg shadow w-full max-w-md mx-auto">
          <h2 className="text-lg font-medium mb-2">Employees by Department</h2>
          <Bar data={deptData} height={200} width={300} options={{ plugins: { legend: { display: false }, title: { display: true, text: 'Employees per Department' } } }} />
        </div>
        {/* New Hires vs Existing Pie */}
        <div className="bg-white p-6 rounded-lg shadow w-full max-w-sm mx-auto">
          <h2 className="text-lg font-medium mb-2">New Hires vs Existing</h2>
          <Pie data={hiresData} height={200} width={200} />
        </div>
      </div>
    </main>
  );
}
