/**
 * Created by Sonith on 10/29/2015.
 */


(function () {
    "use strict";

    angular.module('restaurant').service('ownerService', ownerService);

    ownerService.$inject = ['$q', '$http', 'dateFilter'];

    function ownerService($q, $http, dateFilter) {

        var self = this;
        self.ownerProfile = {};
        self.reservations = {};
        self.fetchAllReservations = function () {

            var defer = $q.defer();

            $http({
                method: 'GET',
                url: 'api/reservation/all'
            })
                .success(function (data) {
                    self.reservations = data;
                    defer.resolve(data);
                })
                .error(function (err) {
                    defer.reject(err)
                });

            return defer.promise;

        };
        self.authenticateOwner = function (email, password) {
            var defer = $q.defer();

            $http({
                method: 'POST',
                url: 'api/admin',
                data: {"email": email, "password": password}
            })
                .success(function (data) {
                    self.ownerProfile = data;
                    defer.resolve(data);
                })
                .error(function (err) {
                    defer.reject(err)
                });

            return defer.promise;
        };

        self.getFreeTables = function (reservation) {
            var defer = $q.defer();

            $http({
                method: 'PUT',
                url: 'api/reservation/free',
                data: reservation

            })
                .success(function (data) {

                    defer.resolve(data);
                })
                .error(function (err) {
                    defer.reject(err)
                });

            return defer.promise;
        };
        self.assignTable = function (reservation) {
            var defer = $q.defer();

            $http({
                method: 'PUT',
                url: 'api/reservation/assign',
                data: reservation

            })
                .success(function (data) {

                    defer.resolve(data);
                })
                .error(function (err) {
                    defer.reject(err)
                });

            return defer.promise;
        };
        self.getSeatingArea = function () {

            var defer = $q.defer();

            $http({
                method: 'GET',
                url: 'api/reservation/now'
            })
                .success(function (data) {

                    var seatingArea = [];
                    var item = function (it) {
                        this.confirmationcode = it.confirmationcode;
                        this.status = it.status;
                        this.partysize = it.partysize;
                        this.since = it.since;
                        this.table = it.table;
                    };
                    var it = {};
                    for (var i = 0; i < data.length; i++) {


                        for (var j = 0; j < self.reservations.length; j++) {

                            if (data[i].reservation_id === self.reservations[j].id) {
                                it.table = data[i].table_id;
                                it.confirmationcode = self.reservations[j].confirmation_code;
                                it.status = self.reservations[j].status > 0 ? "Reserved" : "Available";
                                it.partysize = self.reservations[j].party_size;
                                it.since = self.reservations[j].time;
                                seatingArea.push(new item(it));
                                break;
                            }

                        }


                    }


                    defer.resolve(seatingArea);
                })
                .error(function (err) {
                    defer.reject(err)
                });

            return defer.promise;
        };
        self.getAllCustomers = function () {

            var defer = $q.defer();

            $http({
                method: 'GET',
                url: 'api/customer'
            })
                .success(function (data) {
                    defer.resolve(data);
                })
                .error(function (err) {
                    defer.reject(err)
                });

            return defer.promise;
        };
        self.getProfile = function () {

            var defer = $q.defer();

            $http({
                method: 'GET',
                url: 'api/profile'
            })
                .success(function (data) {
                    self.formatProfileTimes(data.timings);
                    defer.resolve(data);
                })
                .error(function (err) {
                    defer.reject(err)
                });

            return defer.promise;
        };

        self.saveProfile = function (profile) {


            profile.timings.forEach(function (item) {

                item.from = dateFilter(item.tFrom, 'HH:mm');
                item.to = dateFilter(item.tTo, 'HH:mm');
            });

            var defer = $q.defer();

            $http({
                method: 'PUT',
                url: 'api/profile',
                data: profile
            })
                .success(function (data) {

                    self.formatProfileTimes(data.timings);


                    defer.resolve(data);
                })
                .error(function (err) {
                    defer.reject(err)
                });


            return defer.promise;
        };
        self.formatProfileTimes = function (times) {
            times.forEach(function (t) {
                var temp = t.from;

                //t.tFrom = new Date();
                //t.tFrom.setHours(temp.split(':')[0]);
                //t.tFrom.setMinutes(temp.split(':')[1]);
                t.tFrom = new Date(1970, 0, 1,temp.split(':')[0], temp.split(':')[1], 0);


                temp = t.to;
                t.tTo = new Date(1970, 0, 1,temp.split(':')[0], temp.split(':')[1], 0);
                //t.tTo = new Date();
                //t.tTo.setHours(temp.split(':')[0]);
                //t.tTo.setMinutes(temp.split(':')[1]);


            });

        }


    }

})();
