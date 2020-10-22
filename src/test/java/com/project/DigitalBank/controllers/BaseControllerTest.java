package com.project.DigitalBank.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class BaseControllerTest {

    @BeforeEach
    public void setUp() {
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setContextPath("/your-app-context");
        ServletRequestAttributes attrs = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attrs);
    }

}
