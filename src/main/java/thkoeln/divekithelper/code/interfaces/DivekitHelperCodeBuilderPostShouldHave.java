package thkoeln.divekithelper.code.interfaces;

import java.lang.annotation.Annotation;

public interface DivekitHelperCodeBuilderPostShouldHave {
    DivekitHelperCodeBuilderPostAnnotation annotation(Class<? extends Annotation> annotation);

    DivekitHelperCodeBuilderPostOtherClass otherClass(String klassenname);
}
