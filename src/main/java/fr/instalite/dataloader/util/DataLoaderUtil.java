package fr.instalite.dataloader.util;

import com.github.javafaker.Faker;

public class DataLoaderUtil {

  public static String generateRandomText(
    Faker faker,
    int minLength,
    int maxLength
  ) {
    String text = faker.lorem().sentence();
    if (text.length() > maxLength) {
      text = text.substring(0, maxLength);
    } else if (text.length() < minLength) {
      text += faker.lorem().sentence();
    }
    return text;
  }
}
