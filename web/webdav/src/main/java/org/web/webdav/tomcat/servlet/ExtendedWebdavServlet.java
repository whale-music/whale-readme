package org.web.webdav.tomcat.servlet;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.WebResource;
import org.apache.tomcat.util.http.parser.Ranges;
import org.api.webdav.utils.WebdavAccountUtil;
import org.core.common.properties.DebugConfig;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class ExtendedWebdavServlet extends WebdavServlet {
    
    private static long getStart(Ranges.Entry range, long length) {
        long start = range.getStart();
        if (start == -1) {
            long end = range.getEnd();
            // If there is no start, then the start is based on the end
            if (end >= length) {
                return 0;
            } else {
                return length - end;
            }
        } else {
            return start;
        }
    }
    
    private static long getEnd(Ranges.Entry range, long length) {
        long end = range.getEnd();
        if (range.getStart() == -1 || end == -1 || end >= length) {
            return length - 1;
        } else {
            return end;
        }
    }
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            super.service(req, resp);
        } finally {
            // 移除用户数据，防止内存泄露
            WebdavAccountUtil.clear();
        }
    }
    
    /**
     * Process a GET request for the specified resource.
     * 请求Webdav数据
     *
     * @param request  The servlet request we are processing
     * @param response The servlet response we are creating
     * @throws IOException if an input/output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = getRelativePath(request, true);
        WebResource resource = resources.getResource(path);
        response.setHeader("ETag", generateETag(resource));
        response.setHeader("Last-Modified", resource.getLastModifiedHttp());
        response.setContentType(resource.getMimeType());
        long contentLength = resource.getContentLength();
        response.setContentLength(Math.toIntExact(contentLength));
        
        // range header
        Ranges ranges = parseRange(request, response, resource);
        if (Objects.nonNull(ranges) && CollUtil.isNotEmpty(ranges.getEntries())) {
            Ranges.Entry range = ranges.getEntries().getFirst();
            long start = getStart(range, contentLength);
            long end = getEnd(range, contentLength);
            response.addHeader("Content-Range", "bytes " + start + "-" + end + "/" + contentLength);
            long length = end - start + 1;
            response.setContentLengthLong(length);
        }
        
        String url = resource.getURL().toString();
        HttpRequest http = HttpUtil.createGet(url);
        String headerRange = "Range";
        String rangeHeader = request.getHeader(headerRange);
        if (Boolean.TRUE.equals(DebugConfig.getDebug())) {
            log.debug("url: {}", url);
            log.debug("range: {}", rangeHeader);
        }
        http.header(headerRange, rangeHeader, true);
        try (HttpResponse execute = http.execute()) {
            IoUtil.copy(execute.bodyStream(), response.getOutputStream());
        } catch (IORuntimeException e) {
            log.warn(e.getMessage());
        }
    }
}
