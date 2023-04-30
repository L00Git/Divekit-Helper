package thkoeln.divekithelper.code.interfaces;

import java.lang.annotation.Annotation;


public interface DivekitHelperCodeBuilderPostClasses {
    DivekitHelperCodeBuilderPostWithName withName(String klassenname);

    DivekitHelperCodeBuilderPostWithAnnotation withAnnotation(Class<? extends Annotation> annotation);

    DivekitHelperCodeBuilderPostStacktrace stackTrace();

    DivekitHelperCodeBuilderPostNoCircularDependencies noCircularDependencies();
}
