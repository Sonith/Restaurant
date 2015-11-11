/**
 * Created by Sonith on 11/2/2015.
 */

(function () {
    "use strict";

    angular.module('restaurant').controller('LoginController', LoginController);

    LoginController.$inject = ['$location', 'ownerService'];
    function LoginController($location, ownerService) {
        var loginVM = this;
        loginVM.email = "a@a";
        loginVM.password = "1";
        loginVM.validLogin = false;
        loginVM.navCollapsed = true;
        loginVM.signOff = function(){
            $location.path('/');

            loginVM.validLogin = false;
        }
        loginVM.validateOwner = function () {


            //for testing
            $location.path('/owner');
            loginVM.validLogin = true;
            loginVM.navCollapsed = !loginVM.navCollapsed;
            //uncomment these
            //ownerService.authenticateOwner(loginVM.email, loginVM.password).then(function (data) {
            //    loginVM.validLogin = true;
            //    $location.path('/owner');
            //}, function (err) {
            //    console.log(err);
            //});

        }
    }
})();