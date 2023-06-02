var express = require('express');
var router = express.Router();
var roadController = require('../controllers/roadController.js');

router.get('/', roadController.list);
router.post('/', roadController.create);
router.post('/:id', roadController.remove)
router.post('/post', roadController.postRoad);
module.exports = router;
