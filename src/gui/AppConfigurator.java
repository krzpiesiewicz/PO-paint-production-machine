/**
 * 
 */
package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import paint_and_pigment_logic.DataException;
import paint_and_pigment_logic.Paint;
import paint_and_pigment_logic.Pigment;
import paint_and_pigment_logic.UseOfPigment;

/**
 * 
 * @author Krzysztof Piesiewicz
 */
public class AppConfigurator {

	public static void readConfig(Machine machine) throws ConfigFileException {

		try {
			File mainConfigFile = new File("maszyna.conf");
			Scanner sc = null;
			try {
				sc = new Scanner(mainConfigFile);

				if (!sc.hasNextLine()) {
					sc.close();
					throw new ConfigFileException("Brak ścieżki do pliku z farbami.");
				}

				File paintConfigFile = new File(sc.nextLine());
				readPaints(machine, paintConfigFile);

				if (!sc.hasNextLine()) {
					sc.close();
					throw new ConfigFileException("Brak ścieżki do pliku z pigmentami.");
				}

				File pigmentsConfigFile = new File(sc.nextLine());
				readPigments(machine, pigmentsConfigFile);
				sc.close();
			} catch (Exception e) {
				if (sc != null) {
					sc.close();
				}
				throw e;
			}

		} catch (FileNotFoundException e) {
			throw new ConfigFileException("Brak pliku 'maszyna.conf'.");
		}
	}

	private static void readPaints(Machine machine, File source) throws ConfigFileException {
		try {
			Scanner sc = new Scanner(source);

			int lineNumber = 0;

			while (sc.hasNextLine()) {
				lineNumber++;
				String line = sc.nextLine();
				String[] words = line.split("\\s+");

				if (words.length != 3) {
					sc.close();
					throw new ConfigFileException(
							"Definicja farby musi zawierać 3 ciągi znaków oddzielone białymi znakami.");
				}

				try {
					Paint paint = Paint.parse(words[0], words[1], words[2]);
					machine.addPaint(paint);
				} catch (Exception e) {
					sc.close();
					throw new ConfigFileException(
							errorPreffix(source, lineNumber) + e.getMessage());
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			throw new ConfigFileException("Brak pliku '" + source.getPath() + "'.");
		}
	}

	private static void readPigments(Machine machine, File source) throws ConfigFileException {
		try {
			Scanner sc = new Scanner(source);
			int lineNumber = 0;

			while (sc.hasNextLine()) {
				lineNumber++;
				String line = sc.nextLine();
				String[] words = line.split("\\s+");

				if (words.length != 5) {
					sc.close();
					throw new ConfigFileException(
							"Definicja pigmentu musi zawierać 5 ciągów znaków oddzielonych białymi znakami.");
				}

				Pigment pigment;
				try {
					pigment = new Pigment(words[0]);
				} catch (DataException e) {
					sc.close();
					throw new ConfigFileException(
							errorPreffix(source, lineNumber) + e.getMessage());
				}

				try {
					machine.addPigment(pigment);
				} catch (Exception e) {
					// pigment już był w maszynie.
				}

				try {
					UseOfPigment useOfPigment = UseOfPigment.parse(words[1], words[2], words[3],
							words[4]);
					machine.addUseOfPigment(pigment, useOfPigment);
				} catch (Exception e) {
					sc.close();
					throw new ConfigFileException(
							errorPreffix(source, lineNumber) + e.getMessage());
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			throw new ConfigFileException("Brak pliku '" + source.getPath() + "'.");
		}
	}

	private static String errorPreffix(File file, int lineNumber) {
		return "Błąd w pliku '" + file.getPath() + "' w linii " + lineNumber + ": ";
	}
}
