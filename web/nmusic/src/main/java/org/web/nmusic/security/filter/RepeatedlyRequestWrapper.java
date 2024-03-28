package org.web.nmusic.security.filter;


import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StreamUtils;

import java.io.*;


/**
 * 构建可重复读取inputStream的request
 * 如果使用原生的 HttpServletRequest ，
 * 只能读取一次，如果想要二次读取就会报错。 因此需要能够重复读取 InputStream 的方法。
 * request的inputStream只能被读取一次，
 * 多次读取将报错，那么如何才能重复读取呢？答案之一是：增加缓冲，记录已读取的内容。
 *
 * @author ruoyi
 */
public class RepeatedlyRequestWrapper extends HttpServletRequestWrapper {
    //    将request 里面的东西 缓存到这个数组里面
    private final byte[] cachedBody;
    
    public RepeatedlyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.cachedBody = StreamUtils.copyToByteArray(request.getInputStream());
    }
    
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
    
    @Override
    public ServletInputStream getInputStream() {
        return new BodyInputStream(cachedBody);
    }
    
    private static class BodyInputStream extends ServletInputStream {
        private final InputStream delegate;
        
        public BodyInputStream(byte[] body) {
            this.delegate = new ByteArrayInputStream(body);
        }
        
        public boolean isFinished() {
            try {
                return delegate.available() == 0;
            } catch (IOException e) {
                return false;
            }
        }
        
        public boolean isReady() {
            return true;
        }
        
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException();
        }
        
        public int read() throws IOException {
            return this.delegate.read();
        }
        
        @Override
        public int read(@NotNull byte[] b, int off, int len) throws IOException {
            return this.delegate.read(b, off, len);
        }
        
        public int read(byte[] b) throws IOException {
            return this.delegate.read(b);
        }
        
        public long skip(long n) throws IOException {
            return this.delegate.skip(n);
        }
        
        public int available() throws IOException {
            return this.delegate.available();
        }
        
        public void close() throws IOException {
            this.delegate.close();
        }
        
        public synchronized void mark(int readlimit) {
            this.delegate.mark(readlimit);
        }
        
        public synchronized void reset() throws IOException {
            this.delegate.reset();
        }
        
        public boolean markSupported() {
            return this.delegate.markSupported();
        }
    }
}