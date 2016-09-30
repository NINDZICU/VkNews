package com.therishka.androidlab_2.models;

import java.util.List;

/**
 * @author Rishad Mustafaev.
 */

public class VkDialogResponse {
    private int count;
    private List<VkDialog> dialogs;

    public VkDialogResponse() {
    }

    public VkDialogResponse(int count, List<VkDialog> dialogs) {
        this.count = count;
        this.dialogs = dialogs;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<VkDialog> getDialogs() {
        return dialogs;
    }

    public void setDialogs(List<VkDialog> dialogs) {
        this.dialogs = dialogs;
    }
}
