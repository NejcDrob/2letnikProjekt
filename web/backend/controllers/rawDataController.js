var RawDataModel = require('../models/rawDataModel.js');

module.exports = {

    list: function (req, res) {
        RawDataModel.find(function (err, roads) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting roads.',
                    error: err
                });
            }

            return res.json(roads);
        });
    },

create: function (req, res) {
    var rawData = new RawDataModel({
        speed : req.body.speed,
        location : req.body.location,
        posX : req.body.posX,
        posY : req.body.posY,
        gyroX : req.body.gyroX,
        gyroY : req.body.gyroY,
        gyroZ : req.body.gyroZ,
        postedBy : req.body.postedBy,
    });

    rawData.save(function (err, rawData) {
        if (err) {
            return res.status(500).json({
                message: 'Error when creating rawData',
                error: err
            });
        }

        return res.status(201).json(rawData);
    });
},

remove: function (req, res) {
    var id = req.params.id;

    RawDataModel.findByIdAndRemove(id, function (err, rawData) {
        if (err) {
            return res.status(500).json({
                message: 'Error when deleting raw data.',
                error: err
            });
        }

        return res.status(204).json();
    });
},

}