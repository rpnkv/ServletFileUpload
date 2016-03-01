package uploader;

import javax.servlet.http.HttpServletRequest;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class UploadControllerTest {

    UploadController uploadController;

    @org.junit.Before
    public void setUp() throws Exception {

    }



    @org.junit.Test
    public void testInitUpload() throws Exception {

        HttpServletRequest req = createMock(HttpServletRequest.class);
        expect(req.getParameter("username")).andReturn("repnikov");

    }

    @org.junit.Test
    public void testReceiveChunk() throws Exception {

    }

    @org.junit.Test
    public void testCancelUpload() throws Exception {

    }
}