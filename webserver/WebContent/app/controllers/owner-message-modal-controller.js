/**
 * Created by Sonith on 11/6/2015.
 */

(function () {
    "use strict";
    angular.module('restaurant').controller('OwnerMessageModalController', OwnerMessageModalController);


    OwnerMessageModalController.$inject = ['$uibModalInstance', 'items'];
    function OwnerMessageModalController($uibModalInstance, items) {
        var ownermessageVM = this;
        ownermessageVM.message = items;

        ownermessageVM.ok = function () {
            //$uibModalInstance.close(guestmodalVM.selected.item);
            $uibModalInstance.close();
        };

        ownermessageVM.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

