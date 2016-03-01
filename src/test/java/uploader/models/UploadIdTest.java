package uploader.models;

import org.junit.Test;
import uploader.exceptions.InvalidParameterException;

import javax.servlet.http.HttpServletRequest;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

public class UploadIdTest {

    @Test
    public void testCreateFileIdByRequest() throws Exception {

    }

    @Test(expected=InvalidParameterException.class)
    public void testEqualsAllMatch() throws Exception {


    }
}