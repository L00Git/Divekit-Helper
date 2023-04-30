package thkoeln.divekithelper.code.interfaces;

public interface DivekitHelperCodeBuilderPostWithAnnotation {
    DivekitHelperCodeBuilderPostShouldHave shouldHave();

    DivekitHelperCodeBuilderPostShouldNotHave shouldNotHave();

    DivekitHelperCodeBuilderPostImmutable immutable();

    DivekitHelperCodeBuilderPostPackage inPackage(String packageName);

    DivekitHelperCodeBuilderPostNoCircularDependencies noCircularDependencies();
}
