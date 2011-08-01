package com.adarbitrium.gwebdsl.routing

import javax.servlet.http.HttpServletRequest

class RouteExecutionContext {

	final HttpServletRequest request
	final Map params
	
	public RouteExecutionContext( HttpServletRequest req, RouteDefinition defn) {
		request.metaClass {
			getAt { val -> request.getAttribute(val) }
			putAt { key, val -> request.setAttribute(key, val) }
		}
		
		this.request = req
		
		this.params = defn.rexp.process( req.requestURI )
	}
}
