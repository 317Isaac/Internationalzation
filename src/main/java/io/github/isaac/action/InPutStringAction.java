package io.github.isaac.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.xml.XmlFileImpl;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlTag;
import io.github.isaac.Utils;
import io.github.isaac.model.AndroidString;
import io.github.isaac.model.SupportedLanguages;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static io.github.isaac.Constant.*;

public class InPutStringAction extends AnAction {


    private AnActionEvent event;
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        event = anActionEvent;
        chooserFileToInput(anActionEvent);
    }

    private void chooserFileToInput(AnActionEvent anActionEvent) {
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false);
        VirtualFile virtualFile = FileChooser.chooseFile(descriptor, anActionEvent.getProject(), null);
        if (virtualFile != null) {
            String name = virtualFile.getName();
            System.out.println("name = " + name);
            String path = virtualFile.getPath();
            if (!Utils.isExcelFile(path)) {
                Messages.showMessageDialog("该文件格式不是Excel文件，请检查后传入正确格式", "错误", Messages.getWarningIcon());
                return;
            }
            WriteCommandAction.runWriteCommandAction(anActionEvent.getProject(), () -> {
                insertAndroidStringIntoXml(parseExcelFile(path));
            });
        } else {
            System.out.println("selectedFile is null");
        }
    }

    private HashMap<SupportedLanguages, List<AndroidString>> parseExcelFile(String path) {
        List<SupportedLanguages> languagesList = getAllSupportedLanguages();
        HashMap<SupportedLanguages, List<AndroidString>> hashMap = new HashMap<>();
        for (SupportedLanguages language :languagesList) {
            List<AndroidString> stringList = new ArrayList<>();
            hashMap.put(language, stringList);
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(path));
            if (!Utils.isExcelFile(fileInputStream)) {
                Messages.showMessageDialog("该文件格式不是Excel文件，请检查后传入正确格式", "错误", Messages.getWarningIcon());
                return null;
            }

            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

            XSSFSheet sheet = workbook.getSheetAt(0);
            if (sheet.getLastRowNum()<=1) {
                Messages.showMessageDialog("该文件内导入字段为空", "错误", Messages.getWarningIcon());
                return null;
            }
            String id;
            String cnChineseValue;
            String hkChineseValue;
            String englishValue;
            for (int i =1;i<= sheet.getLastRowNum();i++) {
                XSSFRow row =sheet.getRow(i);
                id = row.getCell(ANDROID_STRING_ID_INDEX).getStringCellValue();
                cnChineseValue = row.getCell(ANDROID_STRING_CHINESE_SIMPLIFIED_INDEX).getStringCellValue();
                hkChineseValue = row.getCell(ANDROID_STRING_CHINESE_TRADITIONAL_INDEX).getStringCellValue();
                englishValue = row.getCell(ANDROID_STRING_ENGLISH_INDEX).getStringCellValue();
                hashMap.get(SupportedLanguages.Chinese_Simplified).add(new AndroidString(id, cnChineseValue));
                hashMap.get(SupportedLanguages.Chinese_Traditional).add(new AndroidString(id, hkChineseValue));
                hashMap.get(SupportedLanguages.English).add(new AndroidString(id, englishValue));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    @NotNull
    private List<SupportedLanguages> getAllSupportedLanguages() {
        return SupportedLanguages.getAllSupportedLanguages();
    }

    private void insertAndroidStringIntoXml(HashMap<SupportedLanguages, List<AndroidString>> hashMap) {
        if (event == null || event.getProject() == null ) {
            return;
        }
        @NotNull Collection<VirtualFile> list = FilenameIndex.getVirtualFilesByName(event.getProject(),
                ANDROID_STRING_FILE_NAME, GlobalSearchScope.projectScope(event.getProject()));
        VirtualFile stringsFile = null;
        for (VirtualFile file :list) {
            if (Utils.isStringXML(file)) {
                stringsFile  = file;
                break;
            }
        }
        if (stringsFile == null) {
            System.out.println("This Project do not have Strings.xml");
            return;
        }

        for (SupportedLanguages languages: getAllSupportedLanguages()) {
            insertStringByLanguages(Utils.getValueResourcePath(languages, stringsFile), hashMap.get(languages));
        }

        if (hashMap.get(SupportedLanguages.DefaultLocal)!= null) {
            int stringCount = hashMap.get(SupportedLanguages.DefaultLocal).size();
            Messages.showMessageDialog("已完成字段导入，一共" + stringCount + "条", "提示", Messages.getInformationIcon());
        }
    }

    private void insertStringByLanguages(String filePath, List<AndroidString> stringList) {
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(filePath);
        if (virtualFile != null) {
            addStringElement(virtualFile, stringList);
        } else {
            writeAndroidStringToLocal(filePath, stringList);
        }
    }

    private void addStringElement(VirtualFile virtualFile, List<AndroidString> stringList) {
        PsiFile psiFile = PsiManager.getInstance(Objects.requireNonNull(event.getProject())).findFile(virtualFile);
        assert psiFile != null;
        XmlDocument document = ((XmlFileImpl)psiFile).getDocument();
        if (document != null) {
            XmlTag resourceTag = document.getRootTag();
            if (resourceTag != null) {
                for (AndroidString androidString: stringList) {
                    XmlTag addString = resourceTag.createChildTag("string", "", androidString.getValue(),false);
                    addString.setAttribute("name", androidString.getKey());
                    addString.getValue().setText(androidString.getValue());
                    resourceTag.addSubTag(addString,false);
                }
            }
        }
    }

    private void writeAndroidStringToLocal(String filePath, List<AndroidString> stringList) {
        Utils.writeAndroidStringToLocal(event.getProject(), filePath, stringList);
    }
}
