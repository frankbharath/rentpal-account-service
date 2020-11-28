(function() {
	"use strict";
	let app=angular.module('rentpal');
	let stateParams={
		totalTenants:0
	}
	app.controller("TenantsCtrl",  ["$state", "resolvedTenants", "countries","$stateParams","TenantDataServiceFactory", function($state, resolvedTenants, countries, $stateParams, TenantDataServiceFactory){
		let vm=this;
		vm.tenants=[];
		vm.minPage=1;
		vm.maxPage=1;
		vm.currentPage=$stateParams.pageNo;
		vm.totalTenants=-1;
		vm.limit=50;
		vm.loaded=false;
		vm.countries=countries.data;
		vm.addTenant = function() {
			$state.go('tenants.add');
		}
		resolvedTenants.$promise
		.then(function(res) {
			vm.tenants=res.data.tenantlist;
			if(res.data.count!=undefined){
				vm.totalTenants=res.data.count;
				stateParams.totalTenants=vm.totalTenants;
			}else{
				vm.totalTenants=stateParams.totalTenants;
			}
			if(vm.tenants.length===0){
				vm.finished();
			}
			vm.maxPage=parseInt(vm.totalTenants/vm.limit);
			if(vm.maxPage*vm.limit<vm.totalTenants){
				vm.maxPage=vm.maxPage+1;
			}
		}).catch(angular.noop);
		vm.pagenation=function(bool){
			if(bool){
				if(vm.currentPage+1<=vm.maxPage){
					vm.currentPage++;
					$state.go("tenants", {"countRequired":false, "pageNo":vm.currentPage}, {reload: true});
				}
			}else{
				if(vm.currentPage-1>=vm.minPage){
					vm.currentPage--;
					$state.go("tenants", {"countRequired":false, "pageNo":vm.currentPage}, {reload: true});
				}
			}
		}
		vm.edit=function(index){
			$state.go("tenants.edit", {"id":vm.tenants[index].tenantid});
		}
		vm.deleteTenant=function(index){
			TenantDataServiceFactory.delete({"tenantIds":vm.tenants[index].tenantid}).$promise
			.then(function(res) {
				if(vm.tenants.length===1){
					if(vm.currentPage>1){
						vm.currentPage--;
					}
				}
				$state.go("tenants", {"countRequired":true, "pageNo":vm.currentPage}, {reload: true});
			}).catch(angular.noop);
		}
		vm.finished=function(){
			vm.loaded=true;
		}
		vm.addLease=function(tenantid){
			$state.go("tenants.addlease", {"tenantid":tenantid});
		}
		vm.showLeaseInfo=function(entry){
			$state.go("lease", {"entry":entry});
		} 
	}]);
	app.controller("LeasesCtrl", ["$state", "$stateParams", "resolvedLeases", "TenantDataServiceFactory", function($state, $stateParams, resolvedLeases, TenantDataServiceFactory){
		let vm=this;
		vm.firstname=$stateParams.entry.firstname;
		vm.lastname=$stateParams.entry.lastname;
		vm.email=$stateParams.entry.email;
		vm.leaseslist=[];
		vm.loaded=false;
		resolvedLeases.$promise
		.then(function(res) {
			vm.leaseslist=res.data;
			if(vm.leaseslist.length===0){
				vm.finished();
			}
		}).catch(angular.noop);
		
		vm.edit=function(entry){
			$state.go("lease.editlease", {"lease":entry}, {reload: false});
		}
		vm.finished=function(){
			vm.loaded=true;
		}
		vm.deleteLease=function(leaseId){
			TenantDataServiceFactory.delete({lease:"lease","id":leaseId}).$promise
			.then(function(res) {
				$state.go("lease", {}, {reload: true});
			}).catch(angular.noop);
			console.log(leaseId);
		}
		vm.goBack=function(){
			$state.go("tenants");
		}
	}]);
	app.controller("AddLeaseCtrl", ["$state", "constants", "resolvedProperties", "TenantDataServiceFactory", "$stateParams","$filter", function($state, constants, resolvedProperties, TenantDataServiceFactory, $stateParams, $filter){
		let vm=this;
		vm.properties=[];
		vm.housedetails={};
		vm.apartmentdetails={};
		vm.showSelectUnit=false;
		vm.showLeaseInfo=false;
		vm.propertyNotAvailable=false;
		vm.unitNotAvailable=false;
		vm.selectedProperty={};
		vm.selectedUnits=[];
		vm.selectedUnit={};
		vm.maxtenants=0;
		vm.contractReq=false;
		vm.moveinmindate = new Date();
		vm.moveinchange = function(){
			if(vm.moveindate===undefined){
				vm.moveoutmindate = new Date(
		    		vm.moveinmindate.getFullYear(),
		    		vm.moveinmindate.getMonth(),
		    		vm.moveinmindate.getDate() + 28
	  			);
			}else{
				vm.moveoutmindate = new Date(
		    		vm.moveindate.getFullYear(),
		    		vm.moveindate.getMonth(),
		    		vm.moveindate.getDate() + 28
	  			);
			}
		}
		vm.moveinchange();
		vm.selectedPropertyIndex;
		resolvedProperties.$promise
		.then(function(res) {
			vm.properties=res.data.properties;
			vm.housedetails=res.data.housedetails;
			vm.apartmentdetails=res.data.apartmentdetails;
		}).catch(angular.noop);
		vm.propertyChange=function(){
			vm.selectedProperty=vm.properties[vm.selectedPropertyIndex];
			if(vm.properties[vm.selectedPropertyIndex].propertytype === constants.HOUSETYPE.APPARTMENT){
				vm.showSelectUnit=true;
				vm.selectedUnits=vm.apartmentdetails[vm.selectedProperty.propertyid];
				vm.maxtenants=0;
				vm.showLeaseInfo=false;
				vm.propertyNotAvailable=false;
				vm.unitNotAvailable=false;
			}else{
				vm.unitIndex="";
				vm.showSelectUnit=false;
				vm.maxtenants=vm.housedetails[vm.selectedProperty.propertyid].capacity-vm.housedetails[vm.selectedProperty.propertyid].occupied;
				if(vm.maxtenants===0){
					vm.propertyNotAvailable=true;
				}else{
					vm.showLeaseInfo=true;
				}
			}
		}
		vm.unitChange=function(){
			vm.selectedUnit=vm.selectedUnits[vm.unitIndex];
			vm.maxtenants=vm.selectedUnit.capacity-vm.selectedUnit.occupied;
			if(vm.maxtenants===0){
				vm.unitNotAvailable=true;
			}else{
				vm.showLeaseInfo=true;
			}
		}
		vm.submit=function(){
			if (vm.form.$valid) {
				let payload = new FormData();
				payload.append("propertyId", vm.selectedProperty.propertyid);
				if(vm.properties[vm.selectedPropertyIndex].propertytype === constants.HOUSETYPE.APPARTMENT){
					payload.append("tenantspropertydetailid", vm.selectedUnit.propertydetailsid);
				}else{
					payload.append("tenantspropertydetailid", vm.housedetails[vm.selectedProperty.propertyid].propertydetailsid);
				}
				payload.append("movein", $filter('date')(vm.moveindate, "MMM dd, yyyy"));
				payload.append("moveout", $filter('date')(vm.moveoutdate, "MMM dd, yyyy"));
				payload.append("occupants", vm.occupants);
				payload.append("propertyType", vm.properties[vm.selectedPropertyIndex].propertytype);
				payload.append("leasetenantid", $stateParams.tenantid);
				payload.append("contractReq", vm.contractReq);
				TenantDataServiceFactory.save({lease:"lease"}, payload).$promise
				.then(function(res) {
					$state.go("tenants");
				}).catch(angular.noop);
			}
		}
		vm.closeTemplate = function() {
			$state.go('tenants');
		}
	}]);
	app.controller("EditLeaseCtrl", ["$state", "constants", "resolvedProperties", "TenantDataServiceFactory", "$stateParams","$filter", function($state, constants, resolvedProperties, TenantDataServiceFactory, $stateParams, $filter){
		let vm=this;
		vm.properties=[];
		vm.housedetails={};
		vm.apartmentdetails={};
		vm.lease=$stateParams.lease;
		vm.showSelectUnit=false;
		vm.showForm=false;
		vm.showLeaseInfo=false;
		vm.propertyNotAvailable=false;
		vm.unitNotAvailable=false;
		vm.selectedProperty={};
		vm.selectedUnits=[];
		vm.selectedUnit={};
		vm.maxtenants=0;
		vm.contractReq=false;
		vm.selectedPropertyIndex;
		vm.moveinmindate = new Date();
		vm.moveinchange = function(){
			if(vm.moveindate===undefined){
				vm.moveoutmindate = new Date(
		    		vm.moveinmindate.getFullYear(),
		    		vm.moveinmindate.getMonth(),
		    		vm.moveinmindate.getDate() + 28
	  			);
			}else{
				vm.moveoutmindate = new Date(
		    		vm.moveindate.getFullYear(),
		    		vm.moveindate.getMonth(),
		    		vm.moveindate.getDate() + 28
	  			);
			}
		}
		vm.moveinchange();
		resolvedProperties.$promise
		.then(function(res) {
			vm.properties=res.data.properties;
			vm.housedetails=res.data.housedetails;
			vm.apartmentdetails=res.data.apartmentdetails;
			for(var i=0;i<vm.properties.length;i++){
				if(vm.properties[i].propertyid === vm.lease.propertyid){
					vm.selectedPropertyIndex=i+"";
					vm.selectedProperty=vm.properties[vm.selectedPropertyIndex];
					if(vm.properties[i].propertytype === constants.HOUSETYPE.APPARTMENT){
						vm.selectedUnits=vm.apartmentdetails[vm.properties[i].propertyid];
						for(var j=0;j<vm.selectedUnits.length;j++){
							if(vm.lease.tenantspropertydetailid === vm.selectedUnits[j].propertydetailsid){
								vm.selectedUnit=vm.selectedUnits[j];
								vm.unitIndex=j+"";
								vm.maxtenants=vm.selectedUnit.capacity-vm.selectedUnit.occupied+vm.lease.occupants;
							}
						}
						vm.showSelectUnit=true;
					}else{
						vm.maxtenants=vm.housedetails[vm.selectedProperty.propertyid].capacity-vm.housedetails[vm.selectedProperty.propertyid].occupied+vm.lease.occupants;
					}
					vm.moveindate=new Date(vm.lease.movein);
					vm.moveoutdate=new Date(vm.lease.moveout);
					vm.moveinchange();
					vm.occupants=vm.lease.occupants;
					if(vm.lease.contractid!==null){
						vm.contractReq=true;
					}
					vm.showForm=true;
				}
			}
			vm.showLeaseInfo=true;
		}).catch(angular.noop);
		
		vm.propertyChange=function(){
			vm.selectedProperty=vm.properties[vm.selectedPropertyIndex];
			if(vm.properties[vm.selectedPropertyIndex].propertytype === constants.HOUSETYPE.APPARTMENT){
				vm.showSelectUnit=true;
				vm.selectedUnits=vm.apartmentdetails[vm.selectedProperty.propertyid];
				vm.maxtenants=0;
				vm.showLeaseInfo=false;
				vm.propertyNotAvailable=false;
				vm.unitNotAvailable=false;
			}else{
				vm.showSelectUnit=false;
				vm.unitIndex="";
				vm.maxtenants=vm.housedetails[vm.selectedProperty.propertyid].capacity-vm.housedetails[vm.selectedProperty.propertyid].occupied;
				if(vm.selectedProperty.propertyid===vm.lease.propertyid){
					vm.maxtenants+=vm.lease.occupants;
				}
				if(vm.maxtenants===0){
					vm.propertyNotAvailable=true;
				}else{
					vm.showLeaseInfo=true;
				}
			}
		}
		vm.unitChange=function(){
			vm.selectedUnit=vm.selectedUnits[vm.unitIndex];
			vm.maxtenants=vm.selectedUnit.capacity-vm.selectedUnit.occupied;
			if(vm.selectedUnit.propertydetailsid===vm.lease.tenantspropertydetailid){
				vm.maxtenants+=vm.lease.occupants;
			}
			if(vm.maxtenants===0){
				vm.unitNotAvailable=true;
			}else{
				vm.showLeaseInfo=true;
			}
		}
		vm.submit=function(){
			if (vm.form.$valid) {
				let payload = new FormData();
				payload.append("propertyId", vm.selectedProperty.propertyid);
				if(vm.properties[vm.selectedPropertyIndex].propertytype === constants.HOUSETYPE.APPARTMENT){
					payload.append("tenantspropertydetailid", vm.selectedUnit.propertydetailsid);
				}else{
					payload.append("tenantspropertydetailid", vm.housedetails[vm.selectedProperty.propertyid].propertydetailsid);
				}
				payload.append("leaseid", vm.lease.leaseid);
				payload.append("movein", $filter('date')(vm.moveindate, "MMM dd, yyyy"));
				payload.append("moveout", $filter('date')(vm.moveoutdate, "MMM dd, yyyy"));
				payload.append("occupants", vm.occupants);
				payload.append("propertyType", vm.properties[vm.selectedPropertyIndex].propertytype);
				payload.append("leasetenantid", vm.lease.leasetenantid);
				payload.append("contractReq", vm.contractReq);
				TenantDataServiceFactory.update({lease:"lease"}, payload).$promise
				.then(function(res) {
					$state.go("lease", {}, {reload: true});
				}).catch(angular.noop);
			}
		}
		vm.closeTemplate = function() {
			$state.go("lease");
		}
	}]);
	app.controller("AddTenantCtrl",["constants", "TenantDataServiceFactory","$filter", "$state", function(constants, TenantDataServiceFactory, $filter, $state){
		let vm=this;
		vm.constants=constants;
		vm.maxdate=new Date();
		vm.mindate=new Date(
			vm.maxdate.getFullYear()-100,
    		vm.maxdate.getMonth(),
    		vm.maxdate.getDate()	
		);
		vm.submit=function(){
			if (vm.form.$valid) {
				let payload = new FormData();
				payload.append("firstname", vm.firstname);
				payload.append("lastname", vm.lastname);
				payload.append("email", vm.email);
				payload.append("dob", $filter('date')(vm.dob, "MMM dd, yyyy"));
				payload.append("nationality", vm.nationality);
				TenantDataServiceFactory.save({}, payload).$promise
				.then(function(res) {
					$state.go("tenants", {"countRequired":true}, {reload: true});
				}).catch(angular.noop);
			}
		}
		vm.closeTemplate = function() {
			$state.go('tenants');
		}
	}]);
	app.controller("EditTenantCtrl",["constants", "TenantDataServiceFactory","$filter", "$state", "resolvedTenant", function(constants, TenantDataServiceFactory, $filter, $state, resolvedTenant){
		let vm=this;
		vm.constants=constants;
		vm.maxdate=new Date();
		vm.mindate=new Date(
			vm.maxdate.getFullYear()-100,
    		vm.maxdate.getMonth(),
    		vm.maxdate.getDate()	
		);
		vm.tenant={};
		vm.tenant.dob=vm.maxdate;
		vm.tenant.nationality="";
		resolvedTenant.$promise
		.then(function(res) {
			vm.tenant=res.data;
			vm.tenant.dob=new Date(vm.tenant.dob);
		}).catch(angular.noop);
		vm.submit=function(){
			if (vm.form.$valid) {
				let payload = new FormData();
				payload.append("tenantid", vm.tenant.tenantid);
				payload.append("firstname", vm.tenant.firstname);
				payload.append("lastname", vm.tenant.lastname);
				payload.append("email", vm.tenant.email);
				payload.append("dob", $filter('date')(vm.tenant.dob, "MMM dd, yyyy"));
				payload.append("nationality", vm.tenant.nationality);
				TenantDataServiceFactory.update({}, payload).$promise
				.then(function(res) {
					$state.go("tenants", {"countRequired":true}, {reload: true});
				}).catch(angular.noop);
			}
		}
		vm.closeTemplate = function() {
			$state.go('tenants');
		}
	}]);
})();