package io.github.isaac;

import com.intellij.openapi.vfs.VirtualFile;
import io.github.isaac.model.SupportedLanguages;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class Utils {

    public static boolean isEnglishStringXML(VirtualFile file) {
        if (file == null)
            return false;
        Intrinsics.checkExpressionValueIsNotNull(file.getName(), "file.name");
        if (!StringsKt.endsWith(file.getName(), ".xml", true))
            return false;
        if (file.getParent() == null)
            return false;
        Intrinsics.checkExpressionValueIsNotNull(file.getParent(), "file.parent");
        Intrinsics.checkExpressionValueIsNotNull(file.getParent(), "file.parent");
        Intrinsics.checkExpressionValueIsNotNull(file.getParent(), "file.parent");
        Intrinsics.checkExpressionValueIsNotNull(file.getParent().getName(), "file.parent.name");
        return (Intrinsics.areEqual(file.getParent().getName(), "values")
                || Intrinsics.areEqual(file.getParent().getName(), "value")
                ||file.getParent().getName().startsWith( "values-en"));
    }

    private static String getValueResourcePath(SupportedLanguages language, VirtualFile file) {
        String resPath = file.getPath().substring(0, file
                .getPath().indexOf("/res/") + "/res/".length());
        return resPath + "values-" + language.getAndroidStringFolderNameSuffix() + "/" + file
                .getName();
    }
}
