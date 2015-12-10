(function () {

    describe('Guest Controller Tests', function () {

        var service, guestVM,httpBackend,
            reservationMock = {
                name: "user",
                email: "test@test",
                phone: "555",
                date: new Date("2015-11-04"),
                rtime: new Date(1970, 0, 1, 1, 40, 0),
                when: "",
                party_size: 2,
                confirmation_code: 1001,
                table: 0,
                status: 1
            };

        beforeEach(module('restaurant'));

        describe("Fetch Reservation", function () {

            beforeEach(inject(function ($controller, guestService, $q, $rootScope,$httpBackend) {
                httpBackend = $httpBackend;
                service = guestService;
                httpBackend.expectGET('api/profile').respond(200, [{}]);
                var defer = $q.defer();

                defer.resolve(reservationMock);

                spyOn(service, 'fetchReservation').and.returnValue(defer.promise);

                guestVM = $controller('GuestController', {'guestService': service});

                $rootScope.$apply();

            }));

            it('should be defined', function () {
                expect(guestVM).toBeDefined();
            });

            it('should get reservation', function () {
                guestVM.fetchReservation();
                expect(guestVM.reservation.phone).toEqual('555');
            });


        })


        describe("Create Reservation and Reset", function () {

            beforeEach(inject(function ($controller, guestService, $q, $rootScope,$httpBackend) {
                httpBackend = $httpBackend;
                service = guestService;
                httpBackend.expectGET('api/profile').respond(200, [{}]);
                var defer = $q.defer();

                defer.resolve(reservationMock);

                spyOn(service, 'createReservation').and.returnValue(defer.promise);

                guestVM = $controller('GuestController', {'guestService': service});

                $rootScope.$apply();

            }));

            it('should be defined', function () {
                expect(guestVM).toBeDefined();
            });


            it('create reservation', function () {
                guestVM.reservation = reservationMock;
                guestVM.reservation.confirmation_code = 1000;
                guestVM.createReservation();
                expect(guestVM.reservation.confirmation_code).toEqual(1000);
            });

            it('Reset Reservation', function () {
                guestVM.resetReservation();

                expect(guestVM.reservation).toEqual({});
                expect(guestVM.userConfirmationCode).toEqual("");
                expect(guestVM.editReservation).toEqual(false);

            });

        })

        describe("Update Reservation", function () {
            beforeEach(inject(function ($controller, guestService, $q, $rootScope,$httpBackend) {
                httpBackend = $httpBackend;
                service = guestService;
                httpBackend.expectGET('api/profile').respond(200, [{}]);
                var defer = $q.defer();

                defer.resolve(reservationMock);

                spyOn(service, 'updateReservation').and.returnValue(defer.promise);

                guestVM = $controller('GuestController', {'guestService': service});

                $rootScope.$apply();

            }));

            it("Update Reservation",function(){
                guestVM.reservation = reservationMock;
                guestVM.updateReservation();
                console.log(guestVM.reservation);
                expect(guestVM.reservation.phone).toContain('555');
            });

        })


    });


})();