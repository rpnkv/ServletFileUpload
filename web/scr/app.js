var fileUploadApp = angular.module("fileUploadApp",["ui.bootstrap"]);

fileUploadApp.directive("fileModel", ["$parse", FileModel]);

fileUploadApp.service("fileUploadService", ["$http", FileUploadService]);

fileUploadApp.controller("fileUploadController", ["$scope", "fileUploadService", FileUploadController]);