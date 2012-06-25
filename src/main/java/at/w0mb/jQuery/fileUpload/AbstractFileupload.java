package at.w0mb.jQuery.fileUpload;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import at.w0mb.jQuery.fileUpload.domain.iFile;
import at.w0mb.jQuery.fileUpload.domain.iFileMeta;

import com.google.common.collect.Lists;

public abstract class AbstractFileupload implements FileUpload {
    @Override
    @DELETE
    @Path("/{uuid}")
    public Response delete(@Context HttpServletRequest req, @PathParam("uuid") String uuid) {
	Status status;

	if (remove(uuid)) {
	    status = Status.OK;
	} else {
	    status = Status.NOT_FOUND;
	}

	return Response.status(status).build();
    }
    
    @Override
    @GET
    public Response get(@Context HttpServletRequest req) {
	GenericEntity<List<iFileMeta>> entity = new GenericEntity<List<iFileMeta>>(getFileMetas()) {};

	return Response.ok(entity).build();
    }

    protected abstract String getCallbackUrl();

    @Override
    @GET
    @Path("/url")
    public Response getCallbackUrl(@Context HttpServletRequest req) {
//	return Response.ok(new FileUrl(getCallbackUrl())).build();
	return Response.ok().build();
    }
    
    protected abstract iFile getFileByUuid(String uuid);

    protected abstract List<iFileMeta> getFileMetas();
    
    public String getFileUrl(String randomUUIDString) {
	return getCallbackUrl() + "/"+ randomUUIDString;
    }

    @Override
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void post(@Context HttpServletRequest req, @Context HttpServletResponse res) throws IOException, URISyntaxException, ServletException, FileUploadException {
	UUID uuid = UUID.randomUUID();
	String randomUUIDString = uuid.toString();

	FileItemFactory factory = new DiskFileItemFactory();
	ServletFileUpload upload = new ServletFileUpload(factory);
	List<?> items = upload.parseRequest(req);

	// Process the uploaded items
	for (Object item1 : items) {
	    FileItem item = (FileItem) item1;
	    iFileMeta meta = createFileMeta(item.getName(), item.getSize(), getFileUrl(randomUUIDString), item.getContentType());

	    iFile file = createFile(item.get(), randomUUIDString, meta);

	    save(file);
	}

	res.sendRedirect(getCallbackUrl() + "/" + randomUUIDString + "/meta");
    }
    
    protected abstract iFileMeta createFileMeta(String name, long size, String url, String contentType);
    
    protected abstract iFile createFile(byte[] data, String uuid, iFileMeta meta);
    
    @Override
    @GET
    @Path("/{uuid}/meta")
    public Response redirect(@Context HttpServletRequest req, @PathParam("uuid") String uuid) throws IOException {
	iFile file = getFileByUuid(uuid);

	if (file != null) {
	    iFileMeta meta = file.getMeta();

	    List<iFileMeta> metas = Lists.newArrayList(meta);
	    GenericEntity<List<iFileMeta>> entity = new GenericEntity<List<iFileMeta>>(metas) {
	    };

	    return Response.ok(entity).build();
	} else {
	    return Response.status(Status.NOT_FOUND).build();
	}
    }

    protected abstract boolean remove(String uuid);
    
    protected abstract void save(iFile file);
    
    @Override
    @GET
    @Path("/{uuid}")
    public Response serve(@PathParam("uuid") String uuid, @Context HttpServletResponse response) throws IOException {
	iFile file = getFileByUuid(uuid);

	return Response.ok(file.getData(), file.getMeta().getContentType()).header("Content-Disposition", "attachment; filename=" + file.getMeta().getName()).build();
    }
}