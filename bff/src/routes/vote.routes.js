'use strict';
const { Router } = require('express');
const { authenticate } = require('../middleware/auth.middleware');
const { vote } = require('../controllers/vote.controller');

const router = Router();

// POST /api/vote  (authentication REQUIRED)
router.post('/vote', authenticate, vote);

module.exports = router;
