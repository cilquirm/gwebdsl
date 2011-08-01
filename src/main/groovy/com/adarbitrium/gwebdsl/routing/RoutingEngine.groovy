package com.adarbitrium.gwebdsl.routing

import javax.servlet.http.HttpServletRequest

class RoutingEngine {
	
	private dispatcher = [:].withDefault{ [:] }
	
	
	RoutingEngine attach( RouteDefinition definition ) { 
		dispatcher[definition.method][definition.rexp] = definition
		this	
	}
	
	def dispatch( HttpServletRequest request ) {
		
		// finds the route
		def definition = dispatcher[request.method as RouteMethod].find{ it.key.matches( request.requestURI ) }?.value
		
		if ( definition ) {
			
			new RouteExecutionContext(request,definition).with definition.block
		
		} else {
			return false 
		}
	}
	
	
	// syntax sugar methods
	
	RoutingEngine leftShift( RouteDefinition rd ) { attach( rd ) }
}
