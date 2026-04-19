'use strict';
const axios = require('axios');
const { statsServiceUrl } = require('../config');

const client = axios.create({ baseURL: statsServiceUrl });

/**
 * Fetches aggregated salary statistics from the stats-service.
 * @returns {Promise<{ average: number, median: number, total: number }>}
 */
const getStats = async () => {
  const { data } = await client.get('/stats');
  return data;
};

module.exports = { getStats };
