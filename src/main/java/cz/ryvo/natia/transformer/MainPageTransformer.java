package cz.ryvo.natia.transformer;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformer;
import org.springframework.web.servlet.resource.ResourceTransformerChain;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MainPageTransformer implements ResourceTransformer {

    private static final String hash1 = "PC9ib2R5Pg==";
    private static final String hash2 = "PGRpdiBzdHlsZT0icG9zaXRpb246IGFic29sdXRlOyBib3R0b206IDVweDsgd2lkdGg6IDEwMCU7IHRleHQtYWxpZ246IGNlbnRlcjsiPsKpIDIwMTYgcnl2byhhdCljZW50cnVtLmN6OyDCriBOYXRhbGl5YSBSeXpuYXJvdmE8L2Rpdj4NCjwvYm9keT4=";

    @Override
    public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain) throws IOException {
        String html = IOUtils.toString(resource.getInputStream(), UTF_8);
        html = html.replace(new String(Base64.getDecoder().decode(hash1)), new String(Base64.getDecoder().decode(hash2)));
        return new CustomByteArrayResource(html.getBytes(), resource);
    }

    public static class CustomByteArrayResource extends ByteArrayResource {

        private Resource originalResource;

        public CustomByteArrayResource(byte[] byteArray, Resource originalResource) {
            super(byteArray);
            this.originalResource = originalResource;
        }

        @Override
        protected File getFileForLastModifiedCheck() throws IOException {
            return originalResource.getFile();
        }

        @Override
        public String getFilename() {
            try {
                return originalResource.getFile().getName();
            } catch (IOException e) {
                return null;
            }
        }
    }
}