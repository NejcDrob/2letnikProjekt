var express = require('express');
var router = express.Router();
var rawDataController = require('../controllers/rawDataController.js');

router.post('/', rawDataController.create);
router.post('/:id', rawDataController.remove)
module.exports = router;
