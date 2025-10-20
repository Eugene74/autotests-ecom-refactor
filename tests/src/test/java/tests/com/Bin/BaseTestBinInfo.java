package tests.com.Bin;

import org.testng.annotations.BeforeClass;
import tests.com.Endpoint.BasePropertiesTest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BaseTestBinInfo extends BasePropertiesTest {
    protected static String URLBinInfo;
    protected static final String XML_GET_INFO_RESOURCE_VISA = "/XMLBin/GetBinInfoVISA.xml";
    protected static final String XML_GET_INFO_RESOURCE_MC = "/XMLBin/GetBinInfoMC.xml";
    protected static final String XML_GET_INFO_RESOURCE_MAES = "/XMLBin/GetBinInfoMAES.xml";

    @BeforeClass
    @Override
    public void setup() {
        super.setup();
        URLBinInfo = properties.getProperty("URLtomcat") + "/infopoint/api/v1/getBinInfo";
    }


    protected String getResourceContent(String resourcePath) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return readInputStreamToString(is);
        }
    }

    private String readInputStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }
}
