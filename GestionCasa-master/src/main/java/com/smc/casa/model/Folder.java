package com.smc.casa.model;

import javax.persistence.Table;
import java.util.List;

@Table(name = "folder")
public class Folder {

    public long id;
    public String name;
    public List<Task> tasks;

    public Folder() {
    }

    public Folder(long id, String name, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
