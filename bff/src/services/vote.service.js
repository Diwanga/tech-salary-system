'use strict';
const axios = require('axios');
const { voteServiceUrl } = require('../config');

const client = axios.create({ baseURL: voteServiceUrl });

/**
 * Forwards a vote action to the vote-service.
 * @param {{ submissionId: string, upvote: boolean }} payload
 * @param {string} authHeader — Authorization header to forward
 * @returns {Promise<object>}
 */
const castVote = async (payload, authHeader) => {
  const { data } = await client.post('/votes', payload, {
    headers: { 'Authorization': authHeader },
  });
  return data;
};

module.exports = { castVote };
