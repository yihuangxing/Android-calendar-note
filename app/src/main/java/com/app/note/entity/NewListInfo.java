package com.app.note.entity;

import java.util.List;

public class NewListInfo {
    private List<NewInfo> infoList;

    public NewListInfo(List<NewInfo> infoList) {
        this.infoList = infoList;
    }

    public List<NewInfo> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<NewInfo> infoList) {
        this.infoList = infoList;
    }
}
