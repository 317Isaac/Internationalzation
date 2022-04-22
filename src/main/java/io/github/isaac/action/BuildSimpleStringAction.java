package io.github.isaac.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.xml.XmlFileImpl;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import io.github.isaac.Constant;
import io.github.isaac.TextUtil;
import io.github.isaac.Utils;
import io.github.isaac.gui.CreateGenerationDialog;
import io.github.isaac.listeners.IDialogInterface;
import io.github.isaac.model.AndroidString;
import io.github.isaac.model.DataModel;
import io.github.isaac.model.SupportedLanguages;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.github.isaac.Utils.isStringXML;

public class BuildSimpleStringAction extends AnAction implements IDialogInterface {

    private CreateGenerationDialog mDialog;
    private AnActionEvent event;

    @Override
    public void update(@NotNull AnActionEvent e) {
        Intrinsics.checkParameterIsNotNull(e, "e");
        VirtualFile file = (VirtualFile) CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        boolean isStringXML = isStringXML(file);
        Intrinsics.checkExpressionValueIsNotNull(e.getPresentation(), "presentation");
        e.getPresentation().setEnabled(isStringXML);
        Intrinsics.checkExpressionValueIsNotNull(e.getPresentation(), "presentation");
        e.getPresentation().setVisible(isStringXML);Intrinsics.checkParameterIsNotNull(e, "e");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        event = e;
        createGenerationDialog();
    }

    private void createGenerationDialog() {
        mDialog = new CreateGenerationDialog();
        // 设置对话框窗体大小
        mDialog.setPreferredSize(new Dimension(800, 600));
        // 设置对话框标题
        mDialog.setTitle(Constant.CREATE_INTERNATIONAL_GENERATION_DIALOG);

        mDialog.setDialogInterface(this);
        mDialog.pack();
        // 设置对话框居于屏幕正中
        mDialog.setLocationRelativeTo(null);

        mDialog.setVisible(true);
    }

    @Override
    public void onOk(DataModel dataModel) {
        if (dataModel == null || dataModel.isDataIllegal()) {
            Messages.showMessageDialog("请提供完整的词条名称及各语言对应内容", "错误", Messages.getWarningIcon());
            return;
        }
        if (!TextUtil.isComplyWithIDNamingConvention(dataModel.getId())) {
            Messages.showMessageDialog("输入词条名称不符合规范，词条名称仅支持小写字母及下划线", "错误", Messages.getWarningIcon());
            return;
        }
        if (!TextUtil.isContainChinese(dataModel.getValueSimpleCn())
                || !TextUtil.isContainChinese(dataModel.getValueTraditionalCn())
                || TextUtil.isContainChinese(dataModel.getValueEnglish())) {
            Messages.showMessageDialog("中文词句未包含中文或英文词语内有中文字符", "错误", Messages.getWarningIcon());
            return;
        }
        insertString(dataModel);
    }

    private void insertString(DataModel dataModel) {
        if (event != null && dataModel != null) {
            XmlFile xmlFile = (XmlFile) event.getData(LangDataKeys.PSI_FILE);
            if (xmlFile != null) {

                WriteCommandAction.runWriteCommandAction(event.getProject(), () -> {
                    VirtualFile stringsFile = xmlFile.getVirtualFile();
                    List<SupportedLanguages> supportLanguages = SupportedLanguages.getAllSupportedLanguages();
                    for (SupportedLanguages languages: supportLanguages) {
                        String filePath = Utils.getValueResourcePath(languages, stringsFile);
                        insertStringByLanguages(languages, filePath, dataModel);
                    }
                    mDialog.dispose();
                });
            }

        }
    }

    private void insertStringByLanguages(SupportedLanguages language, String filePath, DataModel dataModel) {
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(filePath);
        if (virtualFile != null) {
            addStringElement(language, virtualFile, dataModel);
        } else {
            writeAndroidStringToLocal(language, filePath, dataModel);
        }
    }

    private void addStringElement(SupportedLanguages language, VirtualFile virtualFile, DataModel dataModel) {
        PsiFile psiFile = PsiManager.getInstance(Objects.requireNonNull(event.getProject())).findFile(virtualFile);
        assert psiFile != null;
        XmlDocument document = ((XmlFileImpl)psiFile).getDocument();
        if (document != null) {
            XmlTag resourceTag = document.getRootTag();
            if (resourceTag != null) {
                XmlTag[] strings = resourceTag.findSubTags("string");
                XmlTag addString = resourceTag.createChildTag("string", "", dataModel.getValueByLanguages(language),false);
                addString.setAttribute("name", dataModel.getId());
                addString.getValue().setText(dataModel.getValueByLanguages(language));
                resourceTag.addSubTag(addString,false);
            }
        }
    }

    private void writeAndroidStringToLocal(SupportedLanguages language, String filePath, DataModel dataModel) {
        List<AndroidString> stringList = new ArrayList<AndroidString>();
        AndroidString addString = new AndroidString();
        addString.setKey(dataModel.getId());
        addString.setValue(dataModel.getValueByLanguages(language));
        addString.setLanguage(language);
        stringList.add(addString);
        Utils.writeAndroidStringToLocal(event.getProject(), filePath, stringList);
    }

    @Override
    public void onCancel() {
        if (mDialog != null) {
            mDialog.dispose();
        }
    }
}
