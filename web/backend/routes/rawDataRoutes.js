var express = require('express');
var router = express.Router();
var rawDataController = require('../controllers/rawDataController.js');

router.post('/', roadController.create);
router.post('/:id', roadController.remove)
module.exports = router;
