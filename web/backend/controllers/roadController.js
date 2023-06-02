var RoadModel = require('../models/roadModel.js');


module.exports = {

    /**
     * roadController.list()
     */
    list: function (req, res) {
        RoadModel.find(function (err, roads) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting roads.',
                    error: err
                });
            }

            return res.json(roads);
        });
    },

   

    /**
     * roadController.create()
     */
    create: function (req, res) {
        var road = new RoadModel({
			xStart : req.body.xStart,
			yStart : req.body.yStart,
			xEnd : req.body.xEnd,
			yEnd : req.body.yEnd,
            postedBy : req.body.postedBy,
            state : req.body.state
        });

        road.save(function (err, road) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating road',
                    error: err
                });
            }

            return res.status(201).json(road);
            //return res.redirect('/roads/login');
        });
    },

    /**
     * roadController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        RoadModel.findByIdAndRemove(id, function (err, road) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the road.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    },
    postRoad: function (req, res) {
        var road = new RoadModel({
          xStart: req.body.xStart,
          yStart: req.body.yStart,
          xEnd: req.body.xEnd,
          yEnd: req.body.yEnd,
          postedBy: req.body.postedBy,
          state: req.body.state
        });
    
        road.save(function (err, road) {
          if (err) {
            return res.status(500).json({
              message: 'Error when creating road',
              error: err
            });
          }
    
          return res.status(201).json(road);
        });
      }

};
