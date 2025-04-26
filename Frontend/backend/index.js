import express from 'express';
import cors from 'cors';
import { Sequelize, DataTypes } from 'sequelize';

const app = express();
app.use(express.json());
app.use(cors({
  origin: 'http://localhost:5173'
}));
// Configure Sequelize
const sequelize = new Sequelize('employees', 'root', 'team_late', {
  host: 'localhost',
  dialect: 'mysql',
  logging: false,
});

async function start() {
  // Test DB connection
  await sequelize.authenticate();


  // Get all table names
  const tables = await sequelize.getQueryInterface().showAllTables();

  // Dynamically define models and CRUD endpoints
  for (const rawName of tables) {
    const tableName = typeof rawName === 'string' ? rawName : rawName.tableName;
    const columns = await sequelize.getQueryInterface().describeTable(tableName);

    // Build a simple attribute map (all fields as strings)
    const attributes = {};
    let primaryKey = null;
    for (const [colName, colDef] of Object.entries(columns)) {
      attributes[colName] = {
        type: DataTypes.STRING,
        allowNull: colDef.allowNull === 'YES',
        primaryKey: colDef.primaryKey === true,
      };
      if (colDef.primaryKey) primaryKey = colName;
    }

    // Define the model
    const model = sequelize.define(tableName, attributes, {
      tableName,
      timestamps: false,
    });

    // Manual list route with pagination support
    app.get(`/${tableName}`, async (req, res) => {
      const page = Math.max(parseInt(req.query.page) || 1, 1);
      const limit = Math.max(parseInt(req.query.limit) || 10, 1);
      const offset = (page - 1) * limit;

      try {
        const { count, rows } = await model.findAndCountAll({ limit, offset });
        res.json({
          data: rows,
          meta: {
            total: count,
            page,
            limit,
            totalPages: Math.ceil(count / limit)
          }
        });
      } catch (err) {
        res.status(500).json({ error: err.message });
      }
    });

    // Manual single-item route
    app.get(`/${tableName}/:${primaryKey}`, async (req, res) => {
      try {
        const record = await model.findByPk(req.params[primaryKey]);
        if (!record) return res.status(404).json({ error: 'Not found' });
        res.json(record);
      } catch (err) {
        res.status(500).json({ error: err.message });
      }
    });

    // CREATE
    app.post(`/${tableName}`, async (req, res) => {
      try {
        const created = await model.create(req.body);
        res.status(201).json(created);
      } catch (err) {
        res.status(400).json({ error: err.message });
      }
    });

    // UPDATE
    app.put(`/${tableName}/:${primaryKey}`, async (req, res) => {
      try {
        const [count] = await model.update(req.body, {
          where: { [primaryKey]: req.params[primaryKey] }
        });
        if (!count) return res.status(404).json({ error: 'Not found' });
        const updated = await model.findByPk(req.params[primaryKey]);
        res.json(updated);
      } catch (err) {
        res.status(400).json({ error: err.message });
      }
    });

    // DELETE
    app.delete(`/${tableName}/:${primaryKey}`, async (req, res) => {
      try {
        const count = await model.destroy({
          where: { [primaryKey]: req.params[primaryKey] }
        });
        if (!count) return res.status(404).json({ error: 'Not found' });
        res.status(204).end();
      } catch (err) {
        res.status(400).json({ error: err.message });
      }
    });

    // Log endpoints for this table
    console.log(`🔧 Endpoints for ${tableName}:`);
    console.log(`  GET    /${tableName}`);
    console.log(`  GET    /${tableName}/:${primaryKey}`);
    console.log(`  POST   /${tableName}`);
    console.log(`  PUT    /${tableName}/:${primaryKey}`);
    console.log(`  DELETE /${tableName}/:${primaryKey}`);
  }

  // Start the server
  app.listen(3000, () => {
    console.log('🚀 CRUD API listening on http://localhost:3000');
  });
}

start().catch(err => {
  console.error('Startup error:', err);
  process.exit(1);
});