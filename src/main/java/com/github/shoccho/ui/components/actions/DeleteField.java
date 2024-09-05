package com.github.shoccho.ui.components.actions;

@FunctionalInterface
public interface DeleteField {
    void execute(String key, Integer id);
}