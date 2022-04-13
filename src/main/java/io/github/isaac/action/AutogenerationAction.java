package io.github.isaac.action;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.search.FilenameIndex;
import io.github.isaac.Constant;
import io.github.isaac.TextUtil;
import io.github.isaac.gui.CreateGenerationDialog;
import io.github.isaac.listeners.IDialogInterface;
import io.github.isaac.model.DataModel;

import java.awt.*;


public class AutogenerationAction extends AnAction implements IDialogInterface {

    CreateGenerationDialog mDialog;
    @Override
    public void actionPerformed(AnActionEvent e) {
        System.out.println("AutogenerationAction actionPerformed");
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
        if (TextUtil.isComplyWithIDNamingConvention(dataModel.getId())) {
            Messages.showMessageDialog("输入词条名称不符合规范，词条名称仅支持小写字母及下划线", "错误", Messages.getWarningIcon());
            return;
        }
        if (!TextUtil.isContainChinese(dataModel.getValueSimpleCn())
                || !TextUtil.isContainChinese(dataModel.getValueTraditionalCn())
                || TextUtil.isContainChinese(dataModel.getValueEnglish())) {
            Messages.showMessageDialog("中文词句未包含中文或英文词语内有中文字符", "错误", Messages.getWarningIcon());
            return;
        }
    }

    private void buildInternationalString(AnActionEvent e) {
        final Project project= e.getProject();
        WriteCommandAction.runWriteCommandAction(project, () -> {
            assert project != null;
//            Module moduleForPsiElement = ModuleUtil.findModuleForPsiElement()
//            FilenameIndex.getFilesByName(project, "string", );
        });
    }

    @Override
    public void onCancel() {

    }
}
