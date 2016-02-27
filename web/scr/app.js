var fileUploadApp = angular.module("fileUploadApp",[]);

fileUploadApp.directive("fileModel", ["$parse", FileModel]);

fileUploadApp.service("fileUploadService", ["$http", FileUploadService]);

fileUploadApp.controller("fileUploadController", ["$scope", "fileUploadService", FileUploadController]);