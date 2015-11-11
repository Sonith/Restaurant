/**
 * Created by Sonith on 10/28/2015.
 */
(function () {
    'use strict';

    //register our module
    //inject dependencies; routing and validation messages
    //setup routing
    angular
        .module('restaurant', ['ngRoute','ngMessages','ui.bootstrap'])
        .config(function ($routeProvider) {

            $routeProvider

                .when('/', {

                    templateUrl: 'partials/cust-tmpl.html'
                })
                .when('/owner', {
                    templateUrl: 'partials/owner-tmpl.html',
                    controller: 'OwnerController',
                    controllerAs: 'ownerVM'
                })
                .
                otherwise({
                    redirectTo: '/'
                });


        });


})();

