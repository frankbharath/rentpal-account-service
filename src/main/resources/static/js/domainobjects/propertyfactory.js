(function(){
	"use strict";
	let app=angular.module('rentpal');
	app.factory('PropertyFactory', ["constants", function(constants) {
		class Property{
			#name;
			#address1;
			#address2;
			#postalcode;
			#city;
			#type;
			set name(name){
				this.#name=name;
			}
			get name(){
				return this.#name;
			}
			set address1(address1){
				this.#address1=address1;
			}
			get address1(){
				return this.#address1;
			}
			set address2(address2){
				this.#address2=address2;
			}
			get address2(){
				return this.#address2;
			}
			set postalcode(postalcode){
				this.#postalcode=postalcode;
			}
			get postalcode(){
				return this.#postalcode;
			}
			set city(city){
				this.#city=city;
			}
			get city(){
				return this.#city;
			}
			set type(type){
				this.#type=type;
			}
			get type(){
				return this.#type;
			}
		}
		return Property;
	}]);
	app.factory('PropertyDetailsFactory', ["constants", function(constants) {
		class PropertyDetails{
			#area;
			#rent;
			#capacity;
			set area(area){
				this.#area=area;
			}
			get area(){
				return this.#area;
			}
			set rent(rent){
				this.#rent=rent;
			}
			get rent(){
				return this.#rent;
			}
			set capacity(capacity){
				this.#capacity=capacity;
			}
			get capacity(){
				return this.#capacity;
			}
			toJSON = function() {
				return{
					area:this.area,
					rent:this.rent,
					capacity:this.capacity
				}
			}
		}
		return PropertyDetails;
	}]);
	app.factory('AppartmentDetailsFactory', ["PropertyDetailsFactory", function() {
		class AppartmentDetails extends PropertyDetailsFactory{
			#doorno;
			#floorno;
			set doorno(doorno){
				this.#doorno=doorno;
			}
			get doorno(){
				return this.#doorno;
			}
			set floorno(floorno){
				this.#floorno=floorno;
			}
			get floorno(){
				return this.#floorno;
			}
		}
		return AppartmentDetails;
	}]);
	app.factory('HouseFactory', ["PropertyFactory", "PropertyDetailsFactory", function(PropertyFactory, PropertyDetailsFactory) {
		class House extends PropertyFactory{
			#propertydetails;
			set propertydetails(propertydetails){
				if(!(propertydetails instanceof PropertyDetailsFactory)){
					throw new Error("Property details should be an instance of PropertyDetailsFactory");
				}
				this.#propertydetails=propertydetails;
			}
			get propertydetails(){
				return this.#propertydetails;
			}
			toJSON = function() {
				return{
					name:this.name,
					address1:this.address1,
					address2:this.address2,
					postalcode:this.postalcode,
					city:this.city,
					type:this.type,
					propertydetails:this.propertydetails
				}
			}
		}
		return House;
	}]);
	app.factory('AppartmentFactory', ["PropertyFactory", "AppartmentDetailsFactory", function(PropertyFactory, AppartmentDetailsFactory) {
		class Appartment extends PropertyFactory{
			#appartmentdetails=[];
			set appartmentdetails(appartmentdetails){
				if(!(appartmentdetails instanceof AppartmentDetailsFactory)){
					throw new Error("Appartment Property details should be an instance of AppartmentDetailsFactory");
				}
				this.#appartmentdetails.push(appartmentdetails);
			}
			get appartmentdetails(){
				return this.#appartmentdetails;
			}
		}
		return Appartment;
	}]);
})();