package com.huotu.hotedu.web.service.impl;

import com.huotu.hotedu.web.service.StaticResourceService;
import com.huotu.hotedu.web.service.VFSHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author CJ
 */
public abstract class AbstractStaticResourceService implements StaticResourceService {

    private static final Log log = LogFactory.getLog(AbstractStaticResourceService.class);

    protected URI uriPrefix;
    protected URI fileHome;
    @Autowired
    private VFSHelper vfsHelper;

    @Override
    public void deleteResource(String path) throws IOException {
        if (path == null)
            return;
        StringBuilder stringBuilder = new StringBuilder(fileHome.toString());
        if (!stringBuilder.toString().endsWith("/") && !path.startsWith("/"))
            stringBuilder.append("/");
        stringBuilder.append(path);

        vfsHelper.handle(stringBuilder.toString(), file -> file.delete());
    }

    @Override
    public URI uploadResource(String path, InputStream data) throws IOException, IllegalStateException, URISyntaxException {
        StringBuilder stringBuilder = new StringBuilder(fileHome.toString());
        if (!stringBuilder.toString().endsWith("/") && !path.startsWith("/"))
            stringBuilder.append("/");
        stringBuilder.append(path);

        vfsHelper.handle(stringBuilder.toString(), file -> {
            if (file.exists())
                throw new IllegalStateException("" + file.toString() + " already existing");
            OutputStream out = file.getContent().getOutputStream();
            try {
                StreamUtils.copy(data, out);
            } catch (IOException e) {
                throw new FileSystemException(e);
            } finally {
                try {
                    data.close();
                    out.close();
                } catch (IOException e) {
                    log.info("Exception on close stream." + e);
                }
            }
        });
        return getResource(path);
    }

    @Override
    public URI getResource(String path) throws URISyntaxException {
        StringBuilder stringBuilder = new StringBuilder(uriPrefix.toString());
        if (!stringBuilder.toString().endsWith("/") && !path.startsWith("/"))
            stringBuilder.append("/");
        stringBuilder.append(path);
        return new URI(stringBuilder.toString());
    }

    @Override
    public void deleteResource(URI uri) throws IOException {
        if (!uri.toString().startsWith(uriPrefix.toString())) {
            log.warn("can not resolve " + uri);
            return;
        }
        String path = uri.toString().substring(uriPrefix.toString().length());
        deleteResource(path);
    }
}
