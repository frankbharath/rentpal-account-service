(function(){
	"use strict";
	let app=angular.module('rentpal');
	app.factory('PropertyDataServiceFactory', ["$resource", function($resource) {
		return $resource('/api/property/:type/:id', {type:"@type", id: "@id" }, {
	        create: {
	            method: "POST",
	            transformRequest: angular.identity,
	            headers: { 'Content-Type': undefined }
	        },
			query:{
				method:'GET', 
				isArray:false
			},
			queryWithMeta:{
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