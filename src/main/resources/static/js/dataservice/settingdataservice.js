(function(){
	"use strict";
	let app=angular.module('rentpal');
	app.factory('SettingDataServiceFactory', ["$resource", function($resource) {
		return $resource('/api/settings', {}, {
	        create: {
	            method: "POST",
	            transformRequest: angular.identity,
	            headers: { 'Content-Type': undefined }
	        },
			update: {
	            method: "PUT",
	            transformRequest: angular.identity,
	            headers: { 'Content-Type': undefined }
	        }
	    });
	}]);
})();