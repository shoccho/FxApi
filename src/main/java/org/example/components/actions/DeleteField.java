package org.example.components.actions;

@FunctionalInterface
public interface DeleteField {
    void execute(String key, String id);
}