/**
* DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
* Copyright 2009 Sun Microsystems, Inc. All rights reserved.
* Generated code from the com.sun.enterprise.config.serverbeans.*
* config beans, based on  HK2 meta model for these beans
* see generator at org.admin.admin.rest.GeneratorResource
* date=Wed Aug 26 14:38:45 PDT 2009
* Very soon, this generated code will be replace by asm or even better...more dynamic logic.
* Ludovic Champenois ludo@dev.java.net
*
**/
package org.glassfish.admin.rest.resources;
import com.sun.enterprise.config.serverbeans.*;
import javax.ws.rs.*;
import org.glassfish.admin.rest.TemplateResource;
import org.glassfish.admin.rest.provider.GetResult;
import com.sun.enterprise.config.serverbeans.Application;
public class ApplicationResource extends TemplateResource<Application> {

	@Path("module/")
	public ListModuleResource getModuleResource() {
		ListModuleResource resource = resourceContext.getResource(ListModuleResource.class);
		resource.setEntity(getEntity().getModule() );
		return resource;
	}
	@Path("web-service-endpoint/")
	public ListWebServiceEndpointResource getWebServiceEndpointResource() {
		ListWebServiceEndpointResource resource = resourceContext.getResource(ListWebServiceEndpointResource.class);
		resource.setEntity(getEntity().getWebServiceEndpoint() );
		return resource;
	}
	@Path("engine/")
	public ListEngineResource getEngineResource() {
		ListEngineResource resource = resourceContext.getResource(ListEngineResource.class);
		resource.setEntity(getEntity().getEngine() );
		return resource;
	}
	@Path("property/")
	public ListPropertyResource getPropertyResource() {
		ListPropertyResource resource = resourceContext.getResource(ListPropertyResource.class);
		resource.setEntity(getEntity().getProperty() );
		return resource;
	}
}
