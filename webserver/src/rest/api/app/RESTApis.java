package rest.api.app;

import io.swagger.jaxrs.config.BeanConfig;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class RESTApis extends ResourceConfig {

	public RESTApis() {
		System.err.println("START>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		packages("rest.api.rest");

		register(io.swagger.jaxrs.listing.ApiListingResource.class);
		register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
		try {
			BeanConfig beanConfig = new BeanConfig();
			beanConfig.setVersion("1.0.3");
			beanConfig.setSchemes(new String[] { "http" });
			beanConfig.setBasePath("/RESTApi");
			beanConfig.setResourcePackage("rest.api");
			beanConfig.setTitle("Restaurant API Documentation");
			beanConfig.setDescription("REST API documentation for the Restaurant App");
			beanConfig.setScan(true);
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
	}
}
