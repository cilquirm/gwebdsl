package com.adarbitrium.webdsl;

import groovyx.net.http.ContentType;
import groovyx.net.http.HTTPBuilder;
import groovyx.net.http.Method;

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.adarbitrium.gwebdsl.Application;

import spock.lang.Specification;

class ApplicationSpec extends Specification {

	static class MyApp extends Application {{
		get( '/first-get' ) { "this is a get!" }
		get( '/second-get' ) { "this is get no.2!" }
		post( '/first-post' ) { "this is a post!" }
		post( '/second-post') { "this is post no.2!" }
		put( '/first-put' ) { "this is a put!" }
		put( '/second-put' ) { "this is put no.2!" }
		delete( '/first-delete' ) { "this is a delete!" }
		delete( '/second-delete' ) { "this is delete no.2!" }
		
		get('/pattern-:a') { "this is a $params.a" }
		post('/pattern-:a') { "this is a $params.a" }
	}}
	
	Server server
	HTTPBuilder builder
	
	def setup() {
		
		server = new Server(8008)
		server.handler = new ServletContextHandler()
		server.handler.addServlet( MyApp  , "/")
		server.start()
		
		builder = new HTTPBuilder("http://localhost:8008")
	}
	
	def cleanup() {
		server.stop()
		
	}
	
	
	def "application should also be a servlet"() {
		notThrown(Exception)	
	}
	
	def "app should support get"() {
		when :
			def result = executeRequest( 'get', '/first-get' )
		then :
			result == [200, "this is a get!"]
	}
	
	def "app should support multiple get requests"() {
		when :
			def result = executeRequest( 'get', '/second-get' )
		then :
			result == [200, "this is get no.2!" ]
	}
	
	def "app should support post"() {
		when :
			def result = executeRequest( 'post', '/first-post' )
		then :
			result == [200, "this is a post!"]
	}
	
	def "app should support multiple post requests"() {
		when : 
			def result = executeRequest('post', '/second-post')
		then :
			result == [200, "this is post no.2!" ]
	}
	
	def "app should support put requests"() {
		when :
			def result = executeRequest('put', '/first-put' )
		then :
			result == [200, "this is a put!" ]
	}
	
	def "app should support multiple put requests"() {
		when :
			def result = executeRequest('put', '/second-put' )
		then :
			result == [200, "this is put no.2!" ]
	}

	def "app should support delete requests"() {
		when :
			def result = executeRequest('delete', '/first-delete' )
		then :
			result == [200, "this is a delete!" ]
	}
	
	def "app should support multiple delete requests"() {
		when :
			def result = executeRequest('delete', '/second-delete' )
		then :
			result == [200, "this is delete no.2!" ]
	}
	
	def "app should support patterns on get requests"() {
		when :
			def result = executeRequest('get','/pattern-test')
		then :
			result == [200, "this is a test"]
	}
	
	def "app should support patterns on post requests"() {
		when :
			def result = executeRequest( 'post', '/pattern-post')
		then :
			result == [200, "this is a post"]	
	}

		
	def "app should distinguish between get and post"() {
		when :
			def result = builder.request( 'http://localhost:8008/first-post', Method.GET, ContentType.ANY ) { req  ->
				response.success = { "found!" }
				response.'404' = {
				 	"not found!"
				}
			}
		then :
			result == "not found!"
	}
	

	private executeRequest( method, url ) { 
		switch ( method ) {
			case ['get','post'] :
				builder."$method"( path: url ) { resp, reader -> [resp.status, reader.text ] }
				break;
			default :
				builder.request( builder.uri.toString() + url, method.toUpperCase() as Method , ContentType.ANY ) { req ->
					response.success = { resp, reader -> [ resp.status, reader.text ] } 
				}
		}  
	}
		
}
