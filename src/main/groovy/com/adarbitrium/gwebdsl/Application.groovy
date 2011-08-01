package com.adarbitrium.gwebdsl

import javax.servlet.ServletException
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import com.adarbitrium.gwebdsl.routing.RouteDefinition;
import com.adarbitrium.gwebdsl.routing.RouteMethod;
import com.adarbitrium.gwebdsl.routing.RoutingEngine;

abstract class Application extends HttpServlet {

	
	def engine = new RoutingEngine()
	
	def get( String s, Closure c ) {  engine << new RouteDefinition('GET', s, c) } 
	def post( String s, Closure c ) {  engine << new RouteDefinition('POST', s, c) }
	def put( String s, Closure c ) { engine << new RouteDefinition('PUT', s, c) }
	def delete( String s, Closure c ) { engine << new RouteDefinition('DELETE', s, c)  }
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException ,IOException {
	  	def blockResponse = engine.dispatch(request )
		  
		if ( blockResponse ) {
			response.status = 200
			response.outputStream << blockResponse
		} else {
			response.status = 404 
		}
	}
	
}
