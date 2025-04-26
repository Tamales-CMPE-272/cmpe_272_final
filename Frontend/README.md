# HR Management App (Frontend)
A modern, responsive React/Vite frontend for HR Management, featuring dynamic CRUD interfaces and analytics dashboards.

## Table of Contents

- [Getting Started](#getting-started)
    - [1. Clone the repository](#1-clone-the-repository)
    - [2. Install dependencies](#2-install-dependencies)
    - [3. Environment variables](#3-environment-variables)
    - [4. Running in development](#4-running-in-development)
    - [5. Building for production](#5-building-for-production)
- [Project Structure](#project-structure)
- [Technologies](#technologies)
- [Available Scripts](#available-scripts)
- [License](#license)

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/your-username/hr-management-frontend.git
cd frontend
```

### 2. Install dependencies

Using npm:

```bash
npm install
```

### 3. Environment variables

Create a `.env.development` file in the project root with:

```env
VITE_API_BASE_URL=http://localhost:3000
```
This should point to your backend which can do the required cruds.

> **Note:** Vite only exposes variables prefixed with `VITE_` to the client.

### 4. Running in development

Start the development server:

```bash
npm run dev
```

Open [http://localhost:5173](http://localhost:5173) to view the app. The page will reload on code changes.

### 5. Building for production

Generate optimized production build:

```bash
npm run build
```

Preview the production build locally:

```bash
npm run preview
```

## Project Structure

```
src/
├── assets/         # Static assets (logo, images)
├── components/     # Reusable UI components (Navbar, Sidebar, FormField)
├── pages/          # Route components (Home, Dashboard, EmployeeCRUD, DepartmentCRUD)
├── App.jsx         # Root component with routing and layout
├── main.jsx        # Entry point (Vite)
├── config.js       # App-wide configuration (API base URL)
└── styles/         # Global styles and Tailwind setup
```

## Technologies

- [Vite](https://vitejs.dev/) – Frontend build tool
- [React](https://reactjs.org/) – UI library
- [Tailwind CSS](https://tailwindcss.com/) – Utility-first CSS framework
- [Heroicons](https://heroicons.com/) – Icons
- [Chart.js](https://www.chartjs.org/) – Charts
- [react-chartjs-2](https://github.com/reactchartjs/react-chartjs-2) – Chart.js React bindings

## Available Scripts

- `npm run dev` – Start development server
- `npm run build` – Build for production
- `npm run preview` – Serve production build locally
- `npm run lint` – Run ESLint (if configured)
- `npm run format` – Run Prettier (if configured)