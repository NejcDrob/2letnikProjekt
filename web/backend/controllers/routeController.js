var RouteModel = require('../models/routeModel.js');

/**
 * routeController.js
 *
 * @description :: Server-side logic for managing routes.
 */
module.exports = {

    /**
     * routeController.list()
     */
    list: function (req, res) {
        RouteModel.find()
        .populate('postedBy')
        .exec(function (err, routes) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting route.',
                    error: err
                });
            }
            var data = [];
            data.routes = routes;
            //return res.render('route/list', data);
            return res.json(routes);
        });
    },

    /**
     * routeController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        RouteModel.findOne({_id: id}, function (err, route) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting route.',
                    error: err
                });
            }

            if (!route) {
                return res.status(404).json({
                    message: 'No such route'
                });
            }

            return res.json(route);
        });
    },

    /**
     * routeController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        RouteModel.findByIdAndRemove(id, function (err, route) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the route.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    },

    publish: function(req, res){
        return res.render('route/publish');
    }
};