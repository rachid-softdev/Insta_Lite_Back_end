package fr.instalite.common;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class RandomIDGenerator implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Random random = new SecureRandom();

	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	private static final Integer DEFAULT_LENGTH = 17;

	public static Integer getDefaultLength() {
		return RandomIDGenerator.DEFAULT_LENGTH;
	}

	public String generateStringId(Integer capacity) {
		if (capacity == 0 || capacity == null) {
			capacity = RandomIDGenerator.getDefaultLength();
		}
		StringBuilder id = new StringBuilder(capacity);
		for (int i = 0; i < capacity; i++) {
			id.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
		}
		return new String(id);
	}

	public String generateRandomUUID() {
		final UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

}
