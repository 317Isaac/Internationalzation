package io.github.isaac;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import io.github.isaac.model.AndroidString;
import io.github.isaac.model.SupportedLanguages;
import kotlin.jvm.internal.Intrinsics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class Utils {

    public static boolean isStringXML(VirtualFile file) {
        if (file == null)
            return false;
        Intrinsics.checkExpressionValueIsNotNull(file.getName(), "file.name");
        if (!Intrinsics.areEqual(file.getName(), "strings.xml"))
            return false;
        if (file.getParent() == null)
            return false;
        Intrinsics.checkExpressionValueIsNotNull(file.getParent(), "file.parent");
        Intrinsics.checkExpressionValueIsNotNull(file.getParent().getName(), "file.parent.name");
        return (Intrinsics.areEqual(file.getParent().getName(), "values")
                || Intrinsics.areEqual(file.getParent().getName(), "value")
                || file.getParent().getName().startsWith("values-" + SupportedLanguages.English.getAndroidStringFolderNameSuffix())
                || file.getParent().getName().startsWith("values-" + SupportedLanguages.Chinese_Simplified.getAndroidStringFolderNameSuffix())
                || file.getParent().getName().startsWith("values-" + SupportedLanguages.Chinese_Traditional.getAndroidStringFolderNameSuffix()));
    }

    public static String getValueResourcePath(SupportedLanguages language, VirtualFile file) {
        String resPath = file.getPath().substring(0, file
                .getPath().indexOf("/res/") + "/res/".length());
        String nameSuffix = language.getAndroidStringFolderNameSuffix();
        String valueSuffix = "values" + (nameSuffix.isEmpty() ? "" : "-");
        return resPath + valueSuffix + nameSuffix + "/" + file
                .getName();
    }

    public static void writeAndroidStringToLocal(final Project myProject, String filePath,
                                                 List<AndroidString> fileContent) {
        File file = new File(filePath);
        boolean fileExits = true;
        try {
            file.getParentFile().mkdirs();
            if (!file.exists()) {
                fileExits = false;
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(getFileContent(fileContent));
            osw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fileExits) {
            final VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file);
            if (virtualFile == null)
                return;
            virtualFile.refresh(true, false, new Runnable() {
                public void run() {
                }
            });
        } else {
            final VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file);
        }
    }

    private static String getFileContent(List<AndroidString> fileContent) {
        String xmlHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
        String stringResourceHeader = "<resources>\n\n";
        String stringResourceTail = "</resources>\n";
        StringBuilder sb = new StringBuilder();
        sb.append(xmlHeader).append(stringResourceHeader);
        for (AndroidString androidString : fileContent)
            sb.append("\t").append(androidString.toString()).append("\n");
        sb.append("\n").append(stringResourceTail);
        return sb.toString();
    }
}
