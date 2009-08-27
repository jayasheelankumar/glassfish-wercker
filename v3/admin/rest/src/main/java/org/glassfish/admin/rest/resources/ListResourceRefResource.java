/**
* DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
* Copyright 2009 Sun Microsystems, Inc. All rights reserved.
* Generated code from the com.sun.enterprise.config.serverbeans.*
* config beans, based on  HK2 meta model for these beans
* see generator at org.admin.admin.rest.GeneratorResource
* date=Wed Aug 26 14:38:44 PDT 2009
* Very soon, this generated code will be replace by asm or even better...more dynamic logic.
* Ludovic Champenois ludo@dev.java.net
*
**/
package org.glassfish.admin.rest.resources;
import com.sun.enterprise.config.serverbeans.*;
import javax.ws.rs.*;
import java.util.List;
import org.glassfish.admin.rest.TemplateListOfResource;
import org.glassfish.admin.rest.provider.GetResultList;
import com.sun.enterprise.config.serverbeans.ResourceRef;
public class ListResourceRefResource extends TemplateListOfResource<ResourceRef> {


	@Path("{Ref}/")
	public ResourceRefResource getResourceRefResource(@PathParam("Ref") String id) {
		ResourceRefResource resource = resourceContext.getResource(ResourceRefResource.class);
		for (ResourceRef c: entity){
			if(c.getRef().equals(id)){
				resource.setEntity(c);
			}
		}
		return resource;
	}


public String getPostCommand() {
	return "create-resource-ref";
}
}
