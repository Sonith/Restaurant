/**
 * Created by Sonith on 10/29/2015.
 */

(function () {
    "use strict";

    angular.module('restaurant').service('guestService', guestService);

    guestService.$inject = ['$q', '$http','dateFilter'];

    function guestService($q, $http,dateFilter) {

        var self = this;


        self.createReservation = function (reservation) {


            var d = [reservation.date.getFullYear(), reservation.date.getMonth(), reservation.date.getDate()];
            var t = [reservation.rtime.getHours(), reservation.rtime.getMinutes()];

            reservation.time = new Date();
            reservation.time.setYear(d[0]);
            reservation.time.setMonth(d[1]);
            reservation.time.setDate(d[2]);
            reservation.time.setHours(t[0]);
            reservation.time.setMinutes(t[1]);

            reservation.time = dateFilter(reservation.time, "yyyy-MM-dd HH:mm:ss Z");

            
            var defer = $q.defer();


            $http({
                method: 'POST',
                url: 'api/reservation',
                data: reservation

            })
                .success(function (data) {
                    defer.resolve(data);
                })
                .error(function (err) {
                    defer.reject(err)
                });

            return defer.promise;
        }

        self.fetchReservation = function (userConfirmationCode) {
            var defer = $q.defer();


            $http({
                method: 'GET',
                url: 'api/reservation/' + userConfirmationCode

            })
                .success(function (data) {
                    defer.resolve(data);
                })
                .error(function (err) {
                    defer.reject(err)
                });

            return defer.promise;
        }

        self.cancelReservation = function (userConfirmationCode) {
            var defer = $q.defer();


            $http({
                method: 'DELETE',
                url: 'api/reservation/' + userConfirmationCode

            })
                .success(function (data) {
                    defer.resolve(data);
                })
                .error(function (err) {
                    defer.reject(err)
                });

            return defer.promise;
        }

        self.updateReservation = function (reservation) {
            var defer = $q.defer();


            $http({
                method: 'PUT',
                url: 'api/reservation' ,
                data: reservation

            })
                .success(function (data) {
                    defer.resolve(data);
                })
                .error(function (err) {
                    defer.reject(err)
                });

            return defer.promise;
        }


    }

})();