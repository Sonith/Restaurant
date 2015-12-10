/**
 * Created by Sonith on 11/1/2015.
 */
(function () {
    "use strict";
    angular.module('restaurant').controller('OwnerModalController', OwnerModalController);


    OwnerModalController.$inject = ['$uibModalInstance', 'items'];
    function OwnerModalController($uibModalInstance, items) {
        var ownermodalVM = this;
        ownermodalVM.reservation = items;
        //guestmodalVM.selected = {
        //    item: guestmodalVM.items[0]
        //};

        ownermodalVM.ok = function () {
            //$uibModalInstance.close(guestmodalVM.selected.item);
            $uibModalInstance.close();
        };

        ownermodalVM.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
