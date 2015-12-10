(function(){

    describe('Guest Service ', function(){

        var httpBackend,
            dataService;

        beforeEach(module('restaurant'));

        beforeEach(inject(function($injector, $httpBackend){
            dataService = $injector.get('guestService');
            httpBackend = $httpBackend;
        }));

        describe('fetch a reservation', function(){
            it('Success', function(){

                httpBackend.expectGET('api/reservation/100').respond(200, [{},{},{}]);

                dataService.fetchReservation(100).then(function(results){
                    expect(results.length).toEqual(3);
                });
                httpBackend.flush();
            });

            it('Error', function(){

                httpBackend.expectGET('api/reservation/100').respond(500, {});
                dataService.fetchReservation(100).then(function(results){
                    expect(results).toEqual({});
                });
                httpBackend.flush();
            });
        });

        describe('Update a Reservation',function(){

            it('Success', function(){

                httpBackend.expectPUT('api/reservation').respond(200, [{},{},{}]);

                dataService.updateReservation({}).then(function(results){
                    expect(results.length).toEqual(3);
                });
                httpBackend.flush();
            });

        });
        describe('Cancel a Reservation',function(){

            it('Success', function(){

                httpBackend.expectDELETE('api/reservation/100').respond(200, [{},{},{}]);

                dataService.cancelReservation(100).then(function(results){
                    expect(results.length).toEqual(3);
                });
                httpBackend.flush();
            });

        })
    });

})();