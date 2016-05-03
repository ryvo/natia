package cz.ryvo.natia.log;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoggingInputStream extends ServletInputStream {

    private final InputStream is;

    public LoggingInputStream(byte[] content) {
        this.is = new ByteArrayInputStream(content);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
    }

    @Override
    public int read() throws IOException {
        return this.is.read();
    }

    @Override
    public void close() throws IOException {
        super.close();
        is.close();
    }
}
