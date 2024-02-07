package h13.noise;

import h13.rubric.ContextInformaton;
import h13.rubric.H13_Tests;
import h13.util.Links;
import org.tudalgo.algoutils.tutor.general.reflections.Link;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

public abstract class H1_Tests implements H13_Tests {

    protected static final PackageLink PACKAGE_LINK = Links.getPackage(BASE_PACKAGE_LINK, "noise");

    @Override
    public PackageLink getPackageLink() {
        return PACKAGE_LINK;
    }

    public abstract TypeLink getTypeLink();

    @Override
    public ContextInformaton contextBuilder(Link subject, String methodName) {
        return H13_Tests.super.contextBuilder(subject, methodName)
            .add("Package", getPackageLink().name())
            .add("Type", getTypeLink().reflection().getName());
    }
}
