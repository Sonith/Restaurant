/**
 * Created by Sonith on 10/29/2015.
 */


(function () {
    "use strict";

    angular.module('restaurant').controller('GuestController', guestController);


    guestController.$inject = ['$filter', '$uibModal', 'guestService', 'ownerService'];

    function guestController($filter, $uibModal, guestService, ownerService) {

        var guestVM = this;
        guestVM.email = "";
        guestVM.password = "";
        guestVM.reservation = {
            name: "user",
            email: "test@test",
            phone: "555",
            date: new Date("2015-11-11"),
            rtime: new Date(1970, 0, 1, new Date().getHours(), new Date().getMinutes(), 0),
            when: "",
            party_size: 2,
            confirmation_code: 0,
            table: 0,
            status: 0
        };

        guestVM.modal = {
            message: "",
            cancel: false
        };

        guestVM.editReservation = false;
        guestVM.userConfirmationCode = "";
        //restaurant profile
        guestVM.profile = null;
        //accordion preferences
        guestVM.showReservation = true;

        guestVM.open = function (size) {

            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'guestModalContent.html',
                controller: 'GuestModalController as guestmodalVM',
                size: size,
                resolve: {
                    items: function () {
                        return guestVM.modal;
                    }
                }
            });

            modalInstance.result.then(function (data) {
                //dismiss the modal
            }, function () {
                //cancel the wait list reservation.
                guestVM.userConfirmationCode = guestVM.reservation.confirmation_code;
                guestVM.cancelReservation(guestVM.reservation.confirmation_code);
            });
        };

        //create a new reservation
        guestVM.createReservation = function () {

            guestService.createReservation(guestVM.reservation).then(function (data) {

                guestVM.reservation.confirmation_code = data.confirmation_code;
                guestVM.reservation.table = data.table;
                guestVM.modal = {};
                if (data.status === 1 && data.table_id > 0) {

                    guestVM.modal.message = "Reserved table " + data.table_id + " with confirmation code " + data.confirmation_code;

                } else if (data.status === 1) {
                    guestVM.modal.message = "Reserved with confirmation code " + data.confirmation_code;
                }
                else if (data.status === 0) {
                    guestVM.modal.message = "Reservation in wait list with confirmation code " + data.confirmation_code +
                        ". What do you want to do?";
                    guestVM.modal.cancel = true;

                }

                guestVM.open();

            }, function (err) {
                console.log(err);
            });

        }
        //cancel a reservation based on the confirmation number
        guestVM.cancelReservation = function () {

            guestService.cancelReservation(guestVM.userConfirmationCode).then(function (data) {

                if (data == true) {
                    guestVM.modal = {};
                    guestVM.resetReservation();
                    guestVM.modal.message = "Cancellation Successful";
                    guestVM.open();
                }

            }, function (err) {
                console.dir(err);
            })

        };
        //user can fetch a reservation with the confirmation code.
        guestVM.fetchReservation = function () {

            guestService.fetchReservation(guestVM.userConfirmationCode).then(function (data) {

                var t = new Date(data.time);
                data.date = new Date();
                data.date.setMilliseconds(t.getMilliseconds());

                data.rtime = new Date(1970, 0, 1, t.getHours(), t.getMinutes(), 0);

                guestVM.reservation = data;
                guestVM.editReservation = true;
                guestVM.showReservation = true;


              //  console.dir(data);

            }, function (err) {
                guestVM.modal = {};
                guestVM.modal.message = "Unable to fetch the reservation";

                guestVM.open();
                console.dir(err);
            })


        }
        //update a reservation that was fetched.
        guestVM.updateReservation = function () {

            var d = [guestVM.reservation.date.getFullYear(), guestVM.reservation.date.getMonth(), guestVM.reservation.date.getDate()];
            var t = [guestVM.reservation.rtime.getHours(), guestVM.reservation.rtime.getMinutes()];

            guestVM.reservation.time = new Date();
            guestVM.reservation.time.setYear(d[0]);
            guestVM.reservation.time.setMonth(d[1]);
            guestVM.reservation.time.setDate(d[2]);
            guestVM.reservation.time.setHours(t[0]);
            guestVM.reservation.time.setMinutes(t[1]);

            guestVM.reservation.time = $filter('date')(guestVM.reservation.time, "yyyy-MM-dd HH:mm:ss Z");
            guestVM.reservation.confirmation_code = guestVM.userConfirmationCode;
            guestService.updateReservation(guestVM.reservation).then(function (data) {

                guestVM.reservation.confirmation_code = data.confirmation_code;
                guestVM.reservation.table = data.table;
                guestVM.modal = {};
                if (data.status == 1 && data.table_id > 0) {

                    guestVM.modal.message = "Updated Reservation with table " + data.table_id + " with confirmation code " + data.confirmation_code;

                } else if (data.status == 1) {
                    guestVM.modal.message = "Updated Reservation with confirmation code " + data.confirmation_code;
                }
                else if (data.status == 0) {
                    guestVM.modal.message = "Updated Reservation in wait list with confirmation code " + data.confirmation_code;
                    guestVM.modal.cancel = true;

                }

                guestVM.open();

            }, function (err) {
                console.log(err);
            });

        }
        //clear the inputs and field vars
        guestVM.resetReservation = function () {
            guestVM.modal = {};
            guestVM.reservation = {};
            guestVM.userConfirmationCode = "";
            guestVM.editReservation = false;
        }
        //fetch the restaurant profile so the user is informed
        guestVM.getProfile = function () {
            ownerService.getProfile().then(function (data) {
                guestVM.profile = data;
            }, function (err) {
                console.log(err);
            })

        }
        //initialize the controller
        guestVM.init = function () {

            guestVM.getProfile();
        }
        guestVM.init();


    }

})();