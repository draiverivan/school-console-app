package ua.foxminded.javaspring.school_console_app;

public class Group {

	private String name;

	public String getGroupName() {
		return name;
	}

	public void setGroupName(String groupName) {
		this.name = groupName;
	}

	public Group(String groupName) {
		this.name = groupName;
	}

}
