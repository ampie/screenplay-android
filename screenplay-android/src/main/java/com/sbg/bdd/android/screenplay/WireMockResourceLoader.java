package com.sbg.bdd.android.screenplay;

import android.content.Context;

import com.sbg.bdd.wiremock.scoped.WireMockResource;
import com.sbg.bdd.wiremock.scoped.WireMockResourceContainer;
import com.sbg.bdd.wiremock.scoped.WireMockResourceRoot;

import java.util.ArrayList;
import java.util.List;

import cucumber.runtime.android.AndroidResourceLoader;
import cucumber.runtime.io.Resource;


public class WireMockResourceLoader extends AndroidResourceLoader {
    private WireMockResourceRoot root;

    public WireMockResourceLoader(WireMockResourceRoot root, Context androidContext) {
        super(androidContext);
        this.root = root;
    }

    @Override
    public Iterable<Resource> resources(String path, String suffix) {
        WireMockResourceContainer existing = (WireMockResourceContainer) root.resolveExisting(path);
        List<Resource> result = new ArrayList<>();
        add(suffix, existing, result);
        for (Resource resource : super.resources(path, suffix)) {
            result.add(resource);
        }
        return result;
    }

    private void add(String suffix, WireMockResourceContainer existing, List<Resource> result) {
        for (WireMockResource resource : existing.list()) {
            if (resource instanceof WireMockResourceContainer) {
                add(suffix, (WireMockResourceContainer) resource, result);
            } else if (resource.getName().endsWith(suffix)) {
                result.add(new WireMockResourceAdapter(resource));
            }
        }
    }
}
