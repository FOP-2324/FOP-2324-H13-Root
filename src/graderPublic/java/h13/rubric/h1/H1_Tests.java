package h13.rubric.h1;

import h13.rubric.ContextInformaton;
import h13.rubric.H13_Tests;
import h13.util.Links;
import org.tudalgo.algoutils.tutor.general.reflections.Link;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

public abstract class H1_Tests extends H13_Tests {

    protected static final PackageLink PACKAGE_LINK = Links.getPackage(H13_Tests.PACKAGE_LINK, "noise");

    @Override
    public PackageLink getPackageLink() {
        return PACKAGE_LINK;
    }

    public abstract TypeLink getTypeLink();

    @Override
    protected ContextInformaton contextBuilder(Link subject, String methodName) {
        return super.contextBuilder(subject, methodName)
            .add("Package", getPackageLink().name())
            .add("Type", getTypeLink().reflection().getName());
    }
}
