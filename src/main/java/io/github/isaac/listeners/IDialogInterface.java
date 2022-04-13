package io.github.isaac.listeners;


import io.github.isaac.model.DataModel;

public interface IDialogInterface {
    /**
     * 点击提交按钮
     * @param dataModel 当前弹窗上的数据模型
     */
    void onOk(DataModel dataModel);

    /**
     * 点击取消按钮
     */
    void onCancel();
}
