
package com.chinacnit.elevatorguard.core.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

public class GzipDecompressingEntity extends HttpEntityWrapper {

    public GzipDecompressingEntity(HttpEntity wrapped) {
        super(wrapped);
    }

    @Override
    public InputStream getContent() throws IOException {
        return new GZIPInputStream(this.wrappedEntity.getContent());
    }

    @Override
    public long getContentLength() {
        return -1L;
    }

}
