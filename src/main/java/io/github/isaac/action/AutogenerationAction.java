package io.github.isaac.action;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import io.github.isaac.Constant;
import io.github.isaac.TextUtil;
import io.github.isaac.Utils;
import io.github.isaac.gui.CreateGenerationDialog;
import io.github.isaac.listeners.IDialogInterface;
import io.github.isaac.model.DataModel;

import java.awt.*;


public class AutogenerationAction extends AnAction implements IDialogInterface {

    private CreateGenerationDialog mDialog;
    AnActionEvent tempE;
    private VirtualFile clickedFile;
    @Override
    public void actionPerformed(AnActionEvent e) {
        System.out.println("AutogenerationAction actionPerformed");
        tempE = e;
        createGenerationDialog();
    }

    private void createGenerationDialog() {
        mDialog = new CreateGenerationDialog();

        mDialog.setPreferredSize(new Dimension(800, 600));

        mDialog.setTitle(Constant.CREATE_INTERNATIONAL_GENERATION_DIALOG);

        mDialog.setDialogInterface(this);
        mDialog.pack();

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
        insertString();
    }

    private void insertString() {
        if (tempE != null) {
            buildInternationalString(tempE);
        }
    }

//    private boolean checkCnStringXmlBuild() {
//    }

    private void buildInternationalString(AnActionEvent e) {
        final Project project= e.getProject();
        WriteCommandAction.runWriteCommandAction(project, () -> {
            assert project != null;
            PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, "strings.xml",
                    GlobalSearchScope.allScope(project));
            int count = 0;
            for (PsiFile file : psiFiles) {
                if (isEnglishStringXML(file)) {
                    count++;
                    System.out.println(file.getName());
                    System.out.println(file.getText());
                    System.out.println(count);
                }
            }
        });
    }

    @Override
    public void onCancel() {
        if (mDialog != null) {
            mDialog.dispose();
        }
    }

    private boolean isEnglishStringXML(PsiFile file) {

        return Utils.isStringXML(file.getVirtualFile());
    }

}
