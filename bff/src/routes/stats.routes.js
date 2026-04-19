'use strict';
const { Router } = require('express');
const { stats } = require('../controllers/stats.controller');

const router = Router();

// GET /api/stats
router.get('/stats', stats);

module.exports = router;
