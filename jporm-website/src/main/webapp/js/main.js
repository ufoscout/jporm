'use strict';

var jporm = angular.module('jporm', [ 'ngResource' ])

.service( 'markdownConverter', function() {
	var converter = new Markdown.Converter();
	return {
		makeHtml : function(text) {
			return converter.makeHtml(text);
		}
	};
})

.directive(	'markdownContent',
			function factory( markdownConverter ) {
				var directiveDefinitionObject = {
					restrict: 'A',
					//transclude: true,
					isolation : true,
					scope: {
				        markdown: '=' 
				    },
					compile : function compile(tElement, tAttrs, transclude) {
						return {
							pre : function preLink(scope, iElement, iAttrs, controller) {
	//							alert('preLink1 execution');
							},
							post : function postLink(scope, iElement, iAttrs, controller) {
								var htmlCode = markdownConverter.makeHtml(scope.markdown);
								iElement.html(htmlCode);
								prettyPrint( iElement );
	
							}
						};
					},
				};
				return directiveDefinitionObject;
			})

.controller("markdownController", function($scope, $http) {

	$scope.mdFiles = [];
	
	$http.get('partial/help.md').then(function(res) {
		$scope.mdFiles[0] = res.data;
	});

});