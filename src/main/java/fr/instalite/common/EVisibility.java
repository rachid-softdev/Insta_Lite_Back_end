package fr.instalite.common;

public enum EVisibility {

	PUBLIC(0, "Public"), PRIVATE(2, "Private"), UNLISTED(3, "Unlisted");

	private final Integer id;

	private final String value;

	private EVisibility(Integer id, String value) {
		this.id = id;
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public static EVisibility fromId(Integer id) {
		for (EVisibility visibility : values()) {
			if (visibility.getId().equals(id)) {
				return visibility;
			}
		}
		return null;
	}

	public static EVisibility fromValue(String value) {
		for (EVisibility visibility : values()) {
			if (visibility.getValue().equalsIgnoreCase(value)) {
				return visibility;
			}
		}
		return null;
	}

}
