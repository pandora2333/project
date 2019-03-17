package com.dangdangnet.base.service;

import com.dangdangnet.base.entity.Label;

import java.util.List;

public interface LabelService {
    public void save(Label label);
    public List<Label> findAll();
    public Label findById(String id);
    public void update(Label label);
    public void delete(String labelId);
    public List<Label> findSearch(Label label);
    public List<Label> findSearchLike(Label label);
}
