package com.app.note.entity;

import java.util.List;

public class MapNoteListInfo {
    private List<MapNoteInfo> list;

    public MapNoteListInfo(List<MapNoteInfo> list) {
        this.list = list;
    }

    public List<MapNoteInfo> getList() {
        return list;
    }

    public void setList(List<MapNoteInfo> list) {
        this.list = list;
    }
}
