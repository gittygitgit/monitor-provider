"use strict"

var gulp = require('gulp');
var browserify = require('browserify'); // Bundles JS
//var reactify = require('reactify'); // Transforms React JSX to JS
var concat = require('gulp-concat'); // Concatenates files
var source = require('vinyl-source-stream'); // Use conventional text streams with Gulp
var connect = require('gulp-connect'); // Runs a local dev server
var open = require('gulp-open'); // Opens a url in a browser
var babelify = require('babelify');

var config = {
  port: 3000,
  devBaseUrl: 'http://localhost',
  paths: {
    html: './*.html',
    js: './**/*.js',
    images: './images/*',
    css: [
//      'node_modules/bootstrap/dist/css/bootstrap.min.css',
//      'node_modules/bootstrap/dist/css/bootstrap-theme.min.css',
//      'node_modules/fixed-data-table-2/dist/fixed-data-table-base.css',
//      'node_modules/fixed-data-table-2/dist/fixed-data-table.css',
//      'node_modules/fixed-data-table-2/dist/fixed-data-table-style.css',
      './css/main.css'
    ],
    dist: './dist',
    main: './main.js'
  }
}

// Start local dev server
/*gulp.task('connect', function() {
  connect.server({
    root: ['dist'],
    port: config.port,
    base: config.devBaseUrl,
    livereload: true
  });
});

gulp.task('open', ['connect'], function() {
  gulp.src('index.html')
    .pipe(open({ uri: config.devBaseUrl + ':' + config.port + '/'}));
});
*/
gulp.task('html', function() {
  gulp.src(config.paths.html)
    .pipe(gulp.dest(config.paths.dist));
})

gulp.task('js', function() {
  browserify({
    entries: config.paths.main
    //debug: true,
  })
    
    .transform("babelify", {"presets": [
        "react",
        "es2015"
      ],
      "plugins": [
        "transform-object-rest-spread"
      ]
    })
    .bundle() // stuff js into a single file named bundle.js
    .on('error', console.error.bind(console))
    .pipe(source('bundle.js'))
    .pipe(gulp.dest(config.paths.dist + '/built'));
});
gulp.task('css', function() {
  gulp.src(config.paths.css)
    .pipe(concat('bundle.css'))
    .pipe(gulp.dest(config.paths.dist + '/css'));
});

// Migrates images to dist folder
// I could even optimize my images here
gulp.task('images', function() {
  gulp.src(config.paths.images)
    .pipe(gulp.dest(config.paths.dist + '/images'));

  //publish favicon
  gulp.src('./src/favicon.ico')
    .pipe(gulp.dest(config.paths.dist));
});
/*
gulp.task('watch', function() {
  gulp.watch(config.paths.html, ['html']);
  gulp.watch(config.paths.js, ['js']);
});
*/
//gulp.task('default', ['html', 'js', 'css', 'images', 'open', 'watch']);
//gulp.task('default', ['html', 'js', 'css', 'images', 'open']);
gulp.task('default', ['html', 'js', 'css', 'images']);
//gulp.task('default', ['test2' ]);
