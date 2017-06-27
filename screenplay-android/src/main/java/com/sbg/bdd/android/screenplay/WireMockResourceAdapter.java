package com.sbg.bdd.android.screenplay;

import com.sbg.bdd.resource.ReadableResource;
import com.sbg.bdd.wiremock.scoped.WireMockResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import cucumber.runtime.io.Resource;


public class WireMockResourceAdapter implements Resource {
    private WireMockResource delegate;

    public WireMockResourceAdapter(WireMockResource resource) {
        delegate = resource;
    }

    @Override
    public String getPath() {
        return delegate.getPath();
    }

    @Override
    public String getAbsolutePath() {
        return getPath();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(((ReadableResource) delegate).read());
    }

    @Override
    public String getClassName(String extension) {
        return getPath().substring(0, getPath().length() - extension.length()).replace('/', '.');
    }
}
