'use strict';
const axios = require('axios');
const { voteServiceUrl } = require('../config');

const client = axios.create({ baseURL: voteServiceUrl });

/**
 * Forwards a vote action to the vote-service.
 * @param {{ salaryId: string|number, vote: 1 | 0 | -1 }} payload
 * @param {string} userId  — extracted from verified JWT, passed as header
 * @returns {Promise<object>}
 */
const castVote = async (payload, userId) => {
  const { data } = await client.post('/vote', payload, {
    headers: { 'x-user-id': userId },
  });
  return data;
};

module.exports = { castVote };
