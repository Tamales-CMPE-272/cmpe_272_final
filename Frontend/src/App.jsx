import Layout from './components/Layout';
import { Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Dashboard from './pages/Dashboard';
import EmployeeCRUD from './pages/EmployeeCRUD';
import DepartmentCRUD from './pages/DepartmentCRUD.jsx';
function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<Home/>} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/employees" element={<EmployeeCRUD />} />
          <Route path="/departments" element={<DepartmentCRUD />} />
      </Routes>
    </Layout>
  );
}
export default App;