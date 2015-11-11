/**
 * Created by Sonith on 11/3/2015.
 */
(function () {
    "use strict";
    angular.module('restaurant').controller('OwnerTableModalController', OwnerTableModalController);


    OwnerTableModalController.$inject = ['$uibModalInstance', 'items', 'ownerService'];
    function OwnerTableModalController($uibModalInstance, items, ownerService) {
        var ownerTableVM = this;
        ownerTableVM.reservation = items;
        ownerTableVM.table = [];
        ownerTableVM.selectedTable = "Select a Table";
        //guestmodalVM.selected = {
        //    item: guestmodalVM.items[0]
        //};

        ownerTableVM.getFreeTables = function () {
            ownerService.getFreeTables(ownerTableVM.reservation).then(function (data) {
                for (var i = 0; i < data.length; i++) {
                    ownerTableVM.table.push(data[i].id);
                }

            }, function (err) {
                console.dir(err);
            })
        }();


        ownerTableVM.ok = function () {
            //$uibModalInstance.close(ownerTableVM.selected.item);
            ownerTableVM.reservation.table_id = ownerTableVM.selectedTable;
            ownerService.assignTable(ownerTableVM.reservation).then(function (data) {
                console.dir(data);

            }, function (err) {
                console.dir(err);
            })
            $uibModalInstance.close();
        };

        ownerTableVM.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
