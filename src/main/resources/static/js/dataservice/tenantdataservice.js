(function(){
	"use strict";
	let app=angular.module('rentpal');
	app.factory('TenantDataServiceFactory', ["$resource", function($resource) {
		return $resource('/api/tenant/:lease/:id', {id: "@id", lease:"@lease"}, {
	        save: {
	            method: "POST",
	            transformRequest: angular.identity,
	            headers: { 'Content-Type': undefined }
	        },
			query:{
				method:'GET', 
				isArray:false
			},
			update: {
	            method: "PUT",
	            transformRequest: angular.identity,
	            headers: { 'Content-Type': undefined }
	        }
	    });
	}]);
})();