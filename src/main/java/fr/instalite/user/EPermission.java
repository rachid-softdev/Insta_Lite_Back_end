package fr.instalite.user;

public enum EPermission {

	ADMIN_READ("admin:read"),
	ADMIN_UPDATE("admin:update"),
	ADMIN_CREATE("admin:create"),
	ADMIN_DELETE("admin:delete"),
	USER_READ("user:read"),
	USER_UPDATE("user:update"),
	USER_CREATE("user:create"),
	USER_DELETE("user:delete");

	private final String permission;

	private EPermission(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}

}