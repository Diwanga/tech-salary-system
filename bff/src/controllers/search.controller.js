'use strict';
const { searchSalaries } = require('../services/search.service');

/**
 * GET /api/search
 * Accepts optional query params: company, role, experience, location.
 * Forwards them directly to search-service and returns the result array.
 */
const search = async (req, res, next) => {
  try {
    const { company, role, experience, location } = req.query;
    const filters = {};

    if (company)    filters.company    = String(company).trim();
    if (role)       filters.role       = String(role).trim();
    if (location)   filters.location   = String(location).trim();
    if (experience) filters.experience = Number(experience);

    const results = await searchSalaries(filters);
    return res.status(200).json(results);
  } catch (err) {
    next(err);
  }
};

module.exports = { search };
