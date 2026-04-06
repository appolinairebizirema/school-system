* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: "Segoe UI", Arial, sans-serif;
}

body {
    background: #f1f5f9;
    color: #1e293b;
}

.navbar {
    background: #1e3a8a;
    color: white;
    padding: 18px 40px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
}

.brand {
    font-size: 22px;
    font-weight: bold;
}

.nav-links {
    display: flex;
    gap: 18px;
    flex-wrap: wrap;
}

.nav-links a {
    color: white;
    text-decoration: none;
    font-weight: 600;
    transition: 0.3s;
}

.nav-links a:hover {
    color: #93c5fd;
}

.page-header {
    width: 90%;
    margin: 25px auto 10px;
}

.page-header h1 {
    font-size: 28px;
    margin-bottom: 6px;
}

.page-header p {
    color: #64748b;
}

.container {
    width: 90%;
    margin: 20px auto;
}

.card {
    background: white;
    padding: 25px;
    border-radius: 14px;
    margin-bottom: 20px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

input, select, button {
    width: 100%;
    padding: 12px;
    margin-top: 10px;
    border-radius: 8px;
    border: 1px solid #cbd5e1;
}

button {
    background: #2563eb;
    color: white;
    border: none;
    font-weight: bold;
    cursor: pointer;
    transition: 0.3s;
}

button:hover {
    background: #1d4ed8;
}

.table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 15px;
}

.table th, .table td {
    border: 1px solid #e2e8f0;
    padding: 12px;
    text-align: center;
}

.table th {
    background: #f8fafc;
}

.btn-link {
    display: inline-block;
    background: #2563eb;
    color: white;
    padding: 8px 12px;
    border-radius: 6px;
    text-decoration: none;
    font-size: 14px;
}

.btn-link:hover {
    background: #1d4ed8;
}

.danger {
    background: #dc2626;
}

.danger:hover {
    background: #b91c1c;
}

.stats-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 15px;
    margin-bottom: 20px;
}

.two-col {
    grid-template-columns: repeat(2, 1fr);
}

.stat-card {
    background: white;
    padding: 20px;
    border-radius: 12px;
    text-align: center;
    box-shadow: 0 4px 10px rgba(0,0,0,0.08);
}

.stat-card h3 {
    font-size: 28px;
    color: #1e3a8a;
}

.stat-card p {
    color: #64748b;
}

.success {
    color: #15803d;
    font-weight: bold;
    margin-bottom: 10px;
}

.error {
    color: #dc2626;
    font-weight: bold;
    margin-bottom: 10px;
}

.action-row {
    display: flex;
    gap: 8px;
    justify-content: center;
    flex-wrap: wrap;
}

.center-wrap {
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
}

.login-card {
    width: 460px;
}

.muted {
    color: #64748b;
}

.quick-links {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
}

.form-grid {
    display: grid;
    grid-template-columns: 1fr;
    gap: 10px;
}

@media (max-width: 900px) {
    .stats-grid {
        grid-template-columns: repeat(2, 1fr);
    }
}

@media (max-width: 600px) {
    .stats-grid {
        grid-template-columns: 1fr;
    }

    .navbar {
        padding: 15px;
    }

    .container,
    .page-header {
        width: 95%;
    }
}