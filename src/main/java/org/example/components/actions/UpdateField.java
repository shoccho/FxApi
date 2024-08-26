package org.example.components.actions;

@FunctionalInterface
public interface UpdateField {
     void execute(String key, String name, String value);
}
