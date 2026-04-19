'use strict';
const { castVote } = require('../services/vote.service');

/**
 * POST /api/vote
 * Protected route — req.user is set by the authenticate middleware.
 * Validates inputs then forwards to vote-service with the authenticated user id.
 */
const vote = async (req, res, next) => {
  try {
    const { salaryId, vote: voteValue } = req.body;

    if (!salaryId) {
      return res.status(400).json({ error: 'salaryId is required' });
    }

    if (![1, 0, -1].includes(Number(voteValue))) {
      return res.status(400).json({ error: 'vote must be 1, 0, or -1' });
    }

    // req.user.id is injected by the authenticate middleware from the JWT payload
    const userId = req.user.id || req.user.sub;
    const result = await castVote({ salaryId, vote: Number(voteValue) }, userId);
    return res.status(200).json(result);
  } catch (err) {
    next(err);
  }
};

module.exports = { vote };
