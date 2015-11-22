'use strict';

(function () {

	angular.module('KeycloakApp', []);

	function initializeKeycloak() {
		var keycloakConfig = {
		  "url": "http://localhost:8080/auth",
		  "realm": "master",
		  "clientId": "frontend",
		  "credentials": {
		    "secret": "1a1b98b6-66c5-4384-a4b4-7361717e773e"
		  }
		};
	  var keycloak = Keycloak(keycloakConfig);
	  keycloak.init({
	    onLoad: 'login-required'
	  }).success(function () {
	    keycloak.loadUserInfo().success(function (userInfo) {
	      bootstrapAngular(keycloak, userInfo);
	    });
	  });
	}

  function bootstrapAngular(keycloak, userInfo) {
    angular.module('KeycloakApp')
      .run(function ($rootScope) {
        $rootScope.currentUser = {
          username: userInfo.name || userInfo.preferred_username,
          editPermission: keycloak.realmAccess.roles
        };

        $rootScope.userLogout = function () {
          keycloak.logout();
        };
      });

    angular.bootstrap(document, ['KeycloakApp']);
  }

	initializeKeycloak();

}());
