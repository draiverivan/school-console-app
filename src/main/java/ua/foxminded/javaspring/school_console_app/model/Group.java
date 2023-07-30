package ua.foxminded.javaspring.school_console_app.model;

import java.util.Objects;

public class Group {

  private long id;
  private String name;

  public Group(long id, String name) {
    this.id = id;
    this.name = name;
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

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Group group = (Group) o;
    return id == group.id && Objects.equals(name, group.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
