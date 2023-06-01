var express = require('express');
var router = express.Router();
var roadController = require('../controllers/roadController.js');

router.get('/', roadController.list);
router.post('/', roadController.create);
router.post('/:id', roadController.remove)

module.exports = router;
