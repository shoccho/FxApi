package org.example.components.actions;

@FunctionalInterface
public interface UpdateField {
     void execute(String key, String id, String name, String value);
}
