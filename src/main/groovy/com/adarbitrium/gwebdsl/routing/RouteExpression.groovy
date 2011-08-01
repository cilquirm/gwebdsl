package com.adarbitrium.gwebdsl.routing

import groovy.lang.Closure;

import java.util.regex.Pattern;

import sun.dc.path.PathException;

class RouteExpression {
	
	final Closure block
	
	
	static final NAMED_VARIABLE_PATTERN = ~/.*:\w+.*/
	static final VARIABLE_MATCH_PATTERN = ~/(:(\w+))/
	static final REPLACEMENT_PATTERN = "([^\\/\$]+)"
	
	Pattern pathExpression 
	List pathVariableNames = []

	public RouteExpression(String path) {
	
		def expression = new StringBuffer(path)
		
		// while the string contains a named variable pattern
		while ( expression ==~ NAMED_VARIABLE_PATTERN ) {
		
			def match
		
			// find the first match 
			match = ( expression =~ VARIABLE_MATCH_PATTERN ) 
		
			if ( match.find() ) {
		
				// replace the string with an expression
				// store the variable name, start and end
				def (name,start,end) = [match.group(2), match.start(1), match.end(2)]
			
				pathVariableNames << name

				if  ( end >= expression.size()) {
					expression[start..<end] = REPLACEMENT_PATTERN
				} else {
					expression[start..<end] = "([^${expression[end]}]+)"
				}
			}
		}
				
		pathExpression = ~expression.toString()
		
	}
	
	public RouteExpression(Pattern expression) {
		
		this.pathExpression = expression
		
	}
	
	

	public matches(String uri) { uri =~ pathExpression }
	
	public process( String uri ) { 
		
		def params = [:]
		def match = matches(uri)
		if ( match ) {
			
			
			def groupCount = match.groupCount()
			if ( groupCount > 0 ) {
				
				if ( pathVariableNames.size() > 0 ) {
					match.groupCount().times { idx -> params[pathVariableNames[idx]] = match.group(idx+1) }
				} else {
					params['captures'] = match[0][1..-1]
				}
			}
			
		
		}
		
		return params
	}
	
}
