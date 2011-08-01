package com.adarbitrium.gwebdsl.routing

import groovy.transform.Immutable


class RouteDefinition {
	
	private RouteMethod method
	private RouteExpression rexp
	private Closure block
	
	public RouteDefinition() {} 

	public RouteDefinition(  m, String p, Closure b ) { 
		this.method = m as RouteMethod ; 
		this.rexp = new RouteExpression(p) ; 
		this.block = b 
	} 

}
