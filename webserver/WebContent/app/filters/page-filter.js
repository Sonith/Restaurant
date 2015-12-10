/**
 * Created by Sonith on 11/1/2015.
 */


(function () {
    "use strict";

    angular.module('restaurant')
        .filter('startFrom', function () {
            return function (input, start) {
                if (input) {
                    start = +start;
                    return input.slice(start);
                }
                return [];
            };
        });
})()
