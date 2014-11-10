/*
 * **************************************************-
 * InGrid CodeList Repository
 * ==================================================
 * Copyright (C) 2014 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
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
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response getCodeLists( @QueryParam("lastModifiedDate") String lastModifiedDate, @QueryParam("name") String name ) {

        if (name == null || "".equals(name) || "*".equals(name)) {
            return Response.ok(manager.getCodeListsAsJson("id", lastModifiedDate, CodeListUtils.SORT_INCREMENT)).build();
        } else {
            return Response.ok(manager.getFilteredCodeListsAsJson(name)).build();
        }
    }
    
    // return only data needed for codelist information (selectbox for administration page)
    @GET
    @Path("short")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCodeListsShort( @QueryParam("lastModifiedDate") String lastModifiedDate, @QueryParam("name") String name ) {
        if (name == null || "".equals(name) || "*".equals(name)) {
            return Response.ok(manager.getCodeListAsShortJson("id", CodeListUtils.SORT_INCREMENT)).build();
        } else {
            return Response.ok(manager.getFilteredCodeListsAsShortJson(name)).build();
        }
    }
    
    @GET
    @Path("short/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCodeListShort(@PathParam("id") String id) {
        return Response.ok(manager.getCodeListAsJson(id)).build();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCodelist(@PathParam("id") String id) {
        return Response.ok(manager.getCodeListAsJson(id)).build();
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

    @GET
    @Path("findEntry/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findEntry(@PathParam("name") String name) {
        return Response.ok(manager.findEntry(name.toLowerCase())).build();
    }
    
    @GET
    @Path("checkChanges")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkForChanges() {
        return Response.ok(manager.checkChangedInitialCodelist()).build();
    }
    
    @PUT
    @Path("addInitialCodelist/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addInitialCodelist(@PathParam("id") String id) {
        boolean success = manager.addCodelistFromInitial(id);
        if (success)
            return Response.ok().build();
        else
            return Response.status(500).build();            
    }
    
    
    public void setManager(CodeListManager manager) {
        this.manager = manager;
    }

    public CodeListManager getManager() {
        return manager;
    }
}
