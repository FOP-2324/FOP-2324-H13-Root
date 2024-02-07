package h13.ui.controls;

import h13.rubric.H13_Tests;
import h13.util.Links;
import org.testfx.framework.junit5.ApplicationTest;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;

import java.util.Map;

public abstract class H3_Tests extends ApplicationTest implements H13_Tests {

    protected static final PackageLink PACKAGE_LINK = Links.getPackage(BASE_PACKAGE_LINK, "ui", "controls");

    @Override
    public PackageLink getPackageLink() {
        return PACKAGE_LINK;
    }

    @Override
    public Map<String, String> getContextInformation() {
        return Map.of();
    }
}
