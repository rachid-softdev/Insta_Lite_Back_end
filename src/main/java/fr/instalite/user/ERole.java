package fr.instalite.user;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static fr.instalite.user.EPermission.ADMIN_CREATE;
import static fr.instalite.user.EPermission.ADMIN_DELETE;
import static fr.instalite.user.EPermission.ADMIN_READ;
import static fr.instalite.user.EPermission.ADMIN_UPDATE;
import static fr.instalite.user.EPermission.USER_CREATE;
import static fr.instalite.user.EPermission.USER_DELETE;
import static fr.instalite.user.EPermission.USER_READ;
import static fr.instalite.user.EPermission.USER_UPDATE;

public enum ERole {

	NONE(Collections.emptySet()),
	USER(Set.of(
			USER_CREATE,
			USER_READ,
			USER_UPDATE,
			USER_DELETE)),
	ADMIN(
			Set.of(
					ADMIN_CREATE,
					ADMIN_READ,
					ADMIN_UPDATE,
					ADMIN_DELETE,
					USER_CREATE,
					USER_READ,
					USER_UPDATE,
					USER_DELETE));

	private final Set<EPermission> permissions;
	private static final String ROLE_PREFIX = "ROLE_";

	private ERole(Set<EPermission> permissions) {
		this.permissions = permissions;
	}

	public Set<EPermission> getPermissions() {
		return permissions;
	}

	public static String getRolePrefix() {
		return ERole.ROLE_PREFIX;
	}

	public List<SimpleGrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = getPermissions().stream()
				.map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
				.collect(Collectors.toList());
		authorities.add(new SimpleGrantedAuthority(String.format("%s%s", ERole.getRolePrefix(), this.name())));
		return authorities;
	}

	public static ERole fromValue(String value) {
		if (value == null) {
			return null;
		}
		for (ERole role : ERole.values()) {
			if (role.name().equalsIgnoreCase(value)) {
				return role;
			}
		}
		return null;
	}

}