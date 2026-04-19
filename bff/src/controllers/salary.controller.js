'use strict';
const { submitSalary } = require('../services/salary.service');

/**
 * POST /api/salary/submit
 * Validates required fields then forwards the submission to salary-service.
 * No authentication required — anonymous submissions are supported.
 */
const submit = async (req, res, next) => {
  try {
    const { company, role, salary, experience, location, anonymous } = req.body;

    if (!company || !role || salary === undefined || experience === undefined) {
      return res.status(400).json({
        error: 'company, role, salary, and experience are required',
      });
    }

    if (isNaN(Number(salary)) || isNaN(Number(experience))) {
      return res.status(400).json({
        error: 'salary and experience must be numeric',
      });
    }

    const payload = {
      company:    String(company).trim(),
      role:       String(role).trim(),
      salary:     Number(salary),
      experience: Number(experience),
      location:   location ? String(location).trim() : null,
      anonymous:  Boolean(anonymous),
    };

    const result = await submitSalary(payload);
    return res.status(201).json(result);
  } catch (err) {
    next(err);
  }
};

module.exports = { submit };
