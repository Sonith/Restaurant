/**
 * Created by Sonith on 11/1/2015.
 */
(function () {
    "use strict";
    angular.module('restaurant').controller('GuestModalController', GuestModalController);


    GuestModalController.$inject = ['$uibModalInstance', 'items'];
    function GuestModalController($uibModalInstance, items) {
        var guestmodalVM = this;
        guestmodalVM.modal = items;

        guestmodalVM.ok = function () {
            //$uibModalInstance.close(guestmodalVM.selected.item);
            $uibModalInstance.close();
        };

        guestmodalVM.cancel = function () {

            $uibModalInstance.dismiss('cancel');
        };
    }
})();
