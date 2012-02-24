package de.ingrid.codelistHandler.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.ingrid.codelistHandler.CodeListManager;
import de.ingrid.codelists.util.CodeListUtils;

@Component
@Path("/getCodelists/")
public class CodeListAccessResource {
    @Autowired
    private CodeListManager manager;

    // and implement the following GET method 
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCodeLists( @QueryParam("lastModifiedDate") String lastModifiedDate, @QueryParam("name") String name ) {

        if (name == null || "".equals(name) || "*".equals(name)) {
            return Response.ok(manager.getCodeListsAsJson("id", lastModifiedDate, CodeListUtils.SORT_INCREMENT)).build();
        } else {
            return Response.ok(manager.getFilteredCodeListsAsJson(name)).build();
        }
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON) 
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCodelists(@PathParam("id") String id, String body) {
        boolean success = manager.updateCodeList(id, body);
        
        if (success)
            return Response.ok().build();
        
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCodelist(@PathParam("id") String id) {
        return Response.ok(manager.getCodeListAsJson(id)).build();
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeCodelist(@PathParam("id") String id) {
        if (manager.removeCodeList(id)) {
            return Response.ok().build();
        } else {
            return Response.status(123).build();
        }
    }

    public void setManager(CodeListManager manager) {
        this.manager = manager;
    }

    public CodeListManager getManager() {
        return manager;
    }
}
