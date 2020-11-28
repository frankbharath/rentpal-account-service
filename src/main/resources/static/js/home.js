(function() {
	"use strict";
	let app=angular.module('rentpal');
	app.controller("HomeCtrl", ["resolvedStats", "$translate", function(resolvedStats, $translate) {
		let vm=this;
		vm.labels = [];
  		vm.data = [];
		vm.options = {
			tooltips: {
         		enabled: true
    		},
			legend:{
				display:true,
				position:'bottom'
			}
	  
	    };
		vm.propertiesOccupantInfo=[];
		vm.showChartError=false;
		vm.showPropStatsError=false;
		resolvedStats.$promise
		.then(function(res) {
			let propertiesCountByType=res.data.propertiesCountByType;
			for(let key in propertiesCountByType){
				vm.data.push(propertiesCountByType[key]);
				$translate(key).then(function (translatedValue) {
					vm.labels.push(translatedValue);
				});
			}
			if(vm.data.length===0){
				vm.showChartError=true;
			}
			vm.propertiesOccupantInfo=res.data.getPropertiesOccupantInfo;
			if(vm.propertiesOccupantInfo.length===0){
				vm.showPropStatsError=true;
			}
		}).catch(angular.noop);
	}]);
})();