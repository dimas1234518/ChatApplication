package net.zhevakin.models.enums;

public enum Provider {
	LOCAL ("Local", 1),
	GOOGLE ("Google", 2);

	private final String name;
	private final  int value;

	Provider(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return name;
	}
}
