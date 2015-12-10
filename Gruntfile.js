module.exports = function (grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        //individual plugin configs
        cssmin: {
            css: {
                files: {
                    'webserver/WebContent/dist/min/app.style.min.css': ['webserver/WebContent/css/bootstrap.min.css', 'webserver/WebContent/css/main.css']
                }
            }
        },
        autoprefixer: {
            options: {
                browsers: ['last 3 version', 'ie 8', 'ie 9']
            },
            files: {
                src: 'webserver/WebContent/dist/min/app.style.min.css',
                dest: 'webserver/WebContent/dist/min/app.style.min.css'
            }
        },
        uglify: {
            js: {
                files: {
                    'webserver/WebContent/dist/min/app.script.min.js': ['webserver/WebContent/js/vendor/angular.js', 'webserver/WebContent/js/vendor/angular-messages.js', 'webserver/WebContent/js/vendor/angular-route.js', 'webserver/WebContent/js/vendor/modernizr-2.8.3-respond-1.4.2.min.js', 'webserver/WebContent/app/app.js', 'webserver/WebContent/js/vendor/ui-bootstrap-tpls-0.14.3.min.js', 'webserver/WebContent/app/**/*-controller.js', 'webserver/WebContent/app/**/*-service.js', 'webserver/WebContent/app/**/*-filter.js']
                }
            }
        },
        copy: {
            main: {
                files: [
                    {
                        expand: true,
                        cwd: 'webserver/WebContent/',
                        src: ['fonts/**'],
                        dest: 'webserver/WebContent/dist'
                    }
                ]
            }
        },
        watch: {
            css: {
                files: ['css/style.css'],
                tasks: ['cssmin', 'autoprefixer']
            },
            js: {
                files: ['app/**/*.js'],
                tasks: ['uglify']
            }
        },
        karma: {
            unit: {
                options: {
                    frameworks: ['jasmine'],
                    browsers: ['PhantomJS'],
                    logLevel: 'INFO',
                    singleRun: true,
                    reporters: ['spec'],
                    files: [
                        'webserver/WebContent/js/vendor/angular.js',
                        'webserver/WebContent/js/vendor/angular-route.js',
                        'webserver/WebContent/js/vendor/angular-mocks.js',
                        'webserver/WebContent/js/vendor/angular-messages.js',
                        'webserver/WebContent/js/vendor/ui-bootstrap-tpls-0.14.3.min.js',
                        'webserver/WebContent/app/**/*.js',
                        'webserver/WebContent/tests/**/*.js'
                    ]
                }
            }
        }
    });

    //load tasks using grunt.loadNpmTasks()
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-copy');
    //grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-autoprefixer');

    //register custom tasks using grunt.registerTask()
    grunt.registerTask('default', ['cssmin', 'autoprefixer', 'uglify', 'copy']);
};