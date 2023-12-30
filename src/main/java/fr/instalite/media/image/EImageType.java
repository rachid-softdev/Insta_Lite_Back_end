package fr.instalite.media.image;

public enum EImageType {

	JPEG,
	PNG,
	JPG,
	GIF;

	private EImageType() {
	}

	public static EImageType fromValue(String value) {
		for (EImageType type : values()) {
			if (type.name().equals(value)) {
				return type;
			}
		}
		return null;
	}

	public static EImageType fromValueIgnoreCase(String value) {
		for (EImageType type : values()) {
			if (type.name().equalsIgnoreCase(value)) {
				return type;
			}
		}
		return null;
	}
}
