package org.example.components.actions;

@FunctionalInterface
public interface UpdateField {
     void execute(String key, Integer id, String name, String value);
}
