package io.github.isaac.action;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import kotlin.jvm.internal.Intrinsics;

public class TranslationAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        VirtualFile file = (VirtualFile) CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());

    }
}
