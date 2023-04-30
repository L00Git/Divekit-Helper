package thkoeln.divekithelper.code.interfaces;


public interface DivekitHelperCodeBuilderPostWithName{
    DivekitHelperCodeBuilderPostShouldHave shouldHave();
    DivekitHelperCodeBuilderPostShouldNotHave shouldNotHave();

    DivekitHelperCodeBuilderPostImmutable immutable();

    DivekitHelperCodeBuilderPostPackage inPackage(String packageName );

    DivekitHelperCodeBuilderPostNoCircularDependencies noCircularDependencies();
}




