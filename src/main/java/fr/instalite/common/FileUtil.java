package fr.instalite.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

	public static void createFolderIfNotExists(Path folder) {
		if (!Files.exists(folder)) {
			try {
				Files.createDirectories(folder);
				System.out.println("Le dossier créé : " + folder.toString());
			} catch (IOException e) {
				System.err.println("Erreur lors de la création du dossier : " + e.getMessage());
			}
		} else {
			System.out.println("Le dossier existe déjà : " + folder.toString());
		}
	}

	public static void deleteAllFiles(Path folder) {
		final File[] files = folder.toFile().listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					file.delete();
				}
			}
		}
	}

}
