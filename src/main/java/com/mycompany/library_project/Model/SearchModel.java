package com.mycompany.library_project.Model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class SearchModel extends RecursiveTreeObject<SearchModel> {

    private String vaalues;

    public SearchModel(String vaalues) {
        this.vaalues = vaalues;
    }

    public String getVaalues() {
        return vaalues;
    }

    public void setVaalues(String vaalues) {
        this.vaalues = vaalues;
    }


}
