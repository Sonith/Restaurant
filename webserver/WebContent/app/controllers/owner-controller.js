/**
 * Created by Sonith on 10/29/2015.
 */


(function () {
    "use strict";

    angular.module('restaurant').controller('OwnerController', ownerController);


    ownerController.$inject = ['$uibModal', '$filter', 'ownerService', 'guestService'];

    function ownerController($uibModal, $filter, ownerService, guestService) {

        var ownerVM = this;


        ownerVM.reservations = null;
        ownerVM.seatingArea = null;
        //reservation
        ownerVM.reservation = {};
        ownerVM.editReservation = false;
        ownerVM.showReservation = false;
        //pagination
        ownerVM.page = {totalItems: 10, currentPage: 1, itemsPerPage: 5};

        //profile
        ownerVM.profile = {

            name: "",
            contact: "",
            email: "",
            auto_assign_table: "",
            timings: []

        };
        //reservation details modal
        ownerVM.selectedReservation = {};
        //modal shows more info on the selected reservation
        ownerVM.openDetailsModal = function (size) {

            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'ownerReservationDetailsModal.html',
                controller: 'OwnerModalController as ownermodalVM',
                size: size,
                resolve: {
                    items: function () {
                        return ownerVM.selectedReservation;
                    }
                }
            });

            modalInstance.result.then(function (data) {
                //guestmVM.selected = data;
            }, function () {
                //Modal dismissed
            });
        };

        ownerVM.modalMessage = "";
        //display the modal to show the reservation details
        ownerVM.openMessageModal = function (size) {

            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'ownerMessageModalContent.html',
                controller: 'OwnerMessageModalController as ownermessageVM',
                size: size,
                resolve: {
                    items: function () {
                        return ownerVM.modalMessage;
                    }
                }
            });

            modalInstance.result.then(function (data) {
                //guestmVM.selected = data;
            }, function () {
                //Modal dismissed
            });
        };


        //reservation details modal
        ownerVM.assignReservation = {};
        //the table assignment modal
        ownerVM.openAssignModal = function (size) {


            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'ownerTableAssignModal.html',
                controller: 'OwnerTableModalController as ownerTableVM',
                size: size,
                resolve: {
                    items: function () {
                        return ownerVM.assignReservation;
                    }
                }
            });

            modalInstance.result.then(function (data) {
                //owner might have changed or assigned tables. So fetch reservations again.
                ownerVM.fetchAllReservations();
            }, function () {
                //Modal dismissed
            });
        };

        //fetch all future reservations.
        ownerVM.fetchAllReservations = function () {

            ownerService.fetchAllReservations().then(function (data) {
                ownerVM.reservations = data;
                ownerVM.page.totalItems = ownerVM.reservations.length;
                //       console.dir(data);
            }, function (err) {
                console.dir(err)
            })
        };
        //get the restaurant profile. contact timings etc
        ownerVM.getProfile = function () {

            ownerService.getProfile().then(function (data) {

                ownerVM.profile = data;


                //   console.dir(data);
            }, function (err) {
                console.dir(err)
            })
        };
        //get the current seating area details
        ownerVM.getSeatingArea = function () {

            ownerService.getSeatingArea().then(function (data) {

                ownerVM.seatingArea = data;

                // console.dir(data);
            }, function (err) {
                console.dir(err)
            })
        };
        //get info of every customer that has made a reservation
        ownerVM.getAllCustomers = function () {

            ownerService.getAllCustomers().then(function (data) {
                ownerVM.customers = data;
                //   console.dir(data);
            }, function (err) {
                console.dir(err)
            })
        };
        //save the restaurant profile
        ownerVM.saveProfile = function () {


            ownerService.saveProfile(ownerVM.profile).then(function (data) {

                ownerVM.modalMessage = "Profile was saved";
                ownerVM.openMessageModal();

            }, function (err) {
                console.dir(err)
            })
        };
        //format the data so that owner can view it
        ownerVM.formatReservation = function () {

            ownerVM.showReservation = true;
            ownerVM.editReservation = true;
            var t = new Date(ownerVM.reservation.time);
            ownerVM.reservation.date = new Date();
            ownerVM.reservation.date.setMilliseconds(t.getMilliseconds());

            ownerVM.reservation.rtime = new Date(1970, 0, 1, t.getHours(), t.getMinutes(), 0);
        };
        //create a new reservation
        ownerVM.createReservation = function () {

            guestService.createReservation(ownerVM.reservation).then(function (data) {

                ownerVM.reservation.confirmation_code = data.confirmation_code;
                ownerVM.reservation.table = data.table;

                if (data.status === 1 && data.table > 0) {

                    ownerVM.modalMessage = "Reserved table " + data.table + " with confirmation code " + data.confirmation_code;

                } else if (data.status === 1) {
                    ownerVM.modalMessage = "Reserved with confirmation code " + data.confirmation_code;
                }
                else if (data.status === 0) {
                    ownerVM.modalMessage = "Reservation in wait list with confirmation code " + data.confirmation_code;
                }

                ownerVM.openMessageModal();
                ownerVM.fetchAllReservations();

            }, function (err) {
                console.log(err);
            });

        };
        ownerVM.updateReservation = function () {

            var d = [ownerVM.reservation.date.getFullYear(), ownerVM.reservation.date.getMonth(), ownerVM.reservation.date.getDate()];
            var t = [ownerVM.reservation.rtime.getHours(), ownerVM.reservation.rtime.getMinutes()];

            ownerVM.reservation.time = new Date();
            ownerVM.reservation.time.setYear(d[0]);
            ownerVM.reservation.time.setMonth(d[1]);
            ownerVM.reservation.time.setDate(d[2]);
            ownerVM.reservation.time.setHours(t[0]);
            ownerVM.reservation.time.setMinutes(t[1]);

            ownerVM.reservation.time = $filter('date')(ownerVM.reservation.time, "yyyy-MM-dd HH:mm:ss Z");
            //ownerVM.reservation.confirmation_code = ownerVM.userConfirmationCode;
            guestService.updateReservation(ownerVM.reservation).then(function (data) {

                ownerVM.reservation.confirmation_code = data.confirmation_code;
                ownerVM.reservation.table = data.table;
                ownerVM.modal = {};
                if (data.status == 1 && data.table_id > 0) {

                    ownerVM.modalMessage = "Updated Reservation with table " + data.table_id + " with confirmation code " + data.confirmation_code;

                } else if (data.status == 1) {
                    ownerVM.modalMessage = "Updated Reservation with confirmation code " + data.confirmation_code;
                }
                else if (data.status == 0) {
                    ownerVM.modalMessage = "Updated Reservation in wait list with confirmation code " + data.confirmation_code;


                }

                ownerVM.openMessageModal();

            }, function (err) {
                console.log(err);
            });

        }
        //cancel a reservation based on the confirmation number
        ownerVM.cancelReservation = function () {

            guestService.cancelReservation(ownerVM.reservation.confirmation_code).then(function (data) {

                if (data == true) {

                    ownerVM.resetReservation();
                    ownerVM.modalMessage = "Cancellation Successful";
                    ownerVM.openMessageModal();
                    ownerVM.fetchAllReservations();
                }

            }, function (err) {
                console.dir(err);
            })

        };

        //clear the form
        ownerVM.resetReservation = function () {
            ownerVM.reservation = {};
            ownerVM.userConfirmationCode = "";
            ownerVM.editReservation = false;
        };

        //fetch the restaurant profile and all the future reservations
        ownerVM.init = function () {
            ownerVM.getProfile();
            ownerVM.fetchAllReservations();
        };

        //initialize the controller
        ownerVM.init();

    }

})();