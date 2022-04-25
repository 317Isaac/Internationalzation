package io.github.isaac.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import io.github.isaac.Constant;
import io.github.isaac.Utils;
import io.github.isaac.model.AndroidString;
import io.github.isaac.model.SupportedLanguages;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.codegen.And;

import java.io.*;
import java.util.*;

import static io.github.isaac.Constant.ANDROID_STRING_FILE_NAME;

public class OutPutStringAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        outputStringsToExcel(anActionEvent);
    }

    private void outputStringsToExcel(AnActionEvent anActionEvent) {
        FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        VirtualFile virtualFile = FileChooser.chooseFile(descriptor, anActionEvent.getProject(), null);
        if (virtualFile != null) {
            String name = virtualFile.getName();
            System.out.println("name = " + name);
            String path = virtualFile.getPath();
            List<AndroidString> valueList = getDefaultStringsXml(anActionEvent);
            if (valueList != null) {
                writeExcelFile(valueList, path);
            }
        }
    }

    private List<AndroidString> getDefaultStringsXml(@NotNull AnActionEvent anActionEvent) {
        List<AndroidString> valueList = null;
        @NotNull Collection<VirtualFile> list = FilenameIndex.getVirtualFilesByName(anActionEvent.getProject(),
                ANDROID_STRING_FILE_NAME, GlobalSearchScope.projectScope(anActionEvent.getProject()));
        VirtualFile stringsFile = null;
        for (VirtualFile file :list) {
            if (Utils.isStringXML(file)) {
                stringsFile  = file;
                break;
            }
        }
        if (stringsFile == null) {
            System.out.println("This Project do not have Strings.xml");
            return null;
        }

        String xmlPath = Utils.getValueResourcePath(SupportedLanguages.DefaultLocal, stringsFile);
        try {
            valueList = AndroidString.getAndroidStringsList(new FileInputStream(new File(xmlPath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return valueList;
    }

    public void writeExcelFile(List<AndroidString> defaultList, String fileFoldersPath){
        //创建工作簿
        XSSFWorkbook wb=new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet=wb.createSheet();
        //创建行
        XSSFRow row=sheet.createRow(0);
        row.createCell(Constant.ANDROID_STRING_ID_INDEX).setCellValue("id名称");
        row.createCell(Constant.ANDROID_STRING_CHINESE_SIMPLIFIED_INDEX).setCellValue("简体中文");
        row.createCell(Constant.ANDROID_STRING_CHINESE_TRADITIONAL_INDEX).setCellValue("繁体中文");
        row.createCell(Constant.ANDROID_STRING_ENGLISH_INDEX).setCellValue("英文");

        for (int i=1; i<= defaultList.size(); i++) {
            //创建行
            row=sheet.createRow(i);
            row.createCell(Constant.ANDROID_STRING_ID_INDEX).setCellValue(defaultList.get(i).getKey());
            row.createCell(Constant.ANDROID_STRING_CHINESE_SIMPLIFIED_INDEX).setCellValue(defaultList.get(i).getValue());
            row.createCell(Constant.ANDROID_STRING_CHINESE_TRADITIONAL_INDEX).setCellValue("");
            row.createCell(Constant.ANDROID_STRING_ENGLISH_INDEX).setCellValue("");
        }
        //写入文件
        try{
            String filePath= fileFoldersPath + "\\AndroidProjectStrings.xlsx";
            File file=new File(filePath);
            if (!file.exists()){
                file.mkdirs();
            }
            FileOutputStream stream=new FileOutputStream(new File(filePath));
            wb.write(stream);
            wb.close();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
