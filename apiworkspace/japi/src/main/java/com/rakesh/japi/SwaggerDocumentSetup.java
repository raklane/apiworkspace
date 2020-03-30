package com.rakesh.japi;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import io.swagger.jaxrs.config.BeanConfig;


public class SwaggerDocumentSetup extends HttpServlet {
	
	private final static long serialVersionUID = 1L;
	
	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
		
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0.0");
		beanConfig.setTitle("POC API framework");
		beanConfig.setDescription("The purpose of this API to retrieve/update/insert/delete the user's information.");
		
		beanConfig.setSchemes(new String[] {"http"});
		beanConfig.setHost("localhost:8080");
		beanConfig.setBasePath("/japi/services");
		beanConfig.setResourcePackage("com.rakesh.japi.services");
		
		beanConfig.setScan(true);
		
	}

}
