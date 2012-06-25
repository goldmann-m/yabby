package at.w0mb.jQuery.fileUpload;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileUploadException;

public interface FileUpload {

    @GET
    @Path("/url")
    public abstract Response getCallbackUrl(@Context HttpServletRequest req);

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public abstract void post(@Context HttpServletRequest req, @Context HttpServletResponse res) throws IOException, URISyntaxException, ServletException, FileUploadException;

    @GET
    public abstract Response get(@Context HttpServletRequest req);

    @GET
    @Path("/{uuid}/meta")
    public abstract Response redirect(@Context HttpServletRequest req, @PathParam("uuid") String uuid) throws IOException;

    @GET
    @Path("/{uuid}")
    public abstract Response serve(@PathParam("uuid") String uuid, @Context HttpServletResponse response) throws IOException;

    @DELETE
    @Path("/{uuid}")
    public abstract Response delete(@Context HttpServletRequest req, @PathParam("uuid") String uuid);
}