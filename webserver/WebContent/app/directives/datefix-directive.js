/**
 * Created by Sonith on 11/6/2015.
 */


(function () {
    "use strict";

    angular.module('restaurant')
        .directive('timepicker', function () {
            return {
                restrict: 'EAC',
                require: 'ngModel',
                link: function (scope, element, attr, controller) {
                    //remove the default formatter from the input directive to prevent conflict
                    controller.$formatters.shift();
                }
            }
        });

})();

