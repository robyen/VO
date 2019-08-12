package com.systex.b300.vbooking.sys;

import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.core.util.ReaderWriter;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class JerseyFilter implements ContainerRequestFilter,ContainerResponseFilter {
	private static Logger log = LogManager.getLogger("sysLog");

    @Override
    public ContainerRequest filter(ContainerRequest request) {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        InputStream in = request.getEntityInputStream();
//        final StringBuilder b = new StringBuilder();
//        try {
//            if (in.available() > 0) {
//                ReaderWriter.writeTo(in, out);
//
//                byte[] requestEntity = out.toByteArray();
//                printEntity(b, requestEntity);
//
//                request.setEntityInputStream(new ByteArrayInputStream(requestEntity));
//            }
//            return request;
//        } catch (IOException ex) {
//            throw new ContainerException(ex);
//        }
        log.info("request==>"+request.getPath());

    	return request;

    }

    private void printEntity(StringBuilder b, byte[] entity) throws IOException {
        if (entity.length == 0)
            return;
        b.append(new String(entity)).append("\n");
        log.info("request==>"+b.toString());
    }

	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        log.info("response==>"+response.getEntity());

		ConnectionManager.closeConnection();
		return response;
	}
}