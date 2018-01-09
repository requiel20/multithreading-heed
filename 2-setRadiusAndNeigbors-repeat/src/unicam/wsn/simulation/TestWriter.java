package unicam.wsn.simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class TestWriter {

	private static TestWriter testWriter;

	public static TestWriter getTestWriter() {
		if (testWriter != null)
			return testWriter;
		testWriter = new TestWriter();
		return testWriter;
	}

	private HashMap<String, ArrayList<Long>> columns = new HashMap<>();

	private final String PATH = System.getProperty("user.home") + "/heedCSV/";
	private String FILENAME = "HEED_setradius_repeat";// cambiare questa
															// linea a ogni
	// nuova versione
	private final String EXTENSION = ".csv";

	private File f;
	private PrintStream out;
	private BufferedReader in;
	private ArrayList<String> lines;

	private TestWriter() {
		f = new File(PATH + FILENAME + EXTENSION);
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addField(String field) {
		columns.put(field, new ArrayList<Long>());
	}

	public void addValue(String field, Long value) {
		columns.get(field).add(value);
	}

	public void readFile() {
		lines = new ArrayList<>();
		String line = null;
		if (f != null) {
			try {
				in = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		do {
			try {
				line = in.readLine();
				if (line != null)
					lines.add(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (line != null);

	}

	public void printFile() {

		HashMap<String, ArrayList<Long>> printColumns = new HashMap<>();

		/*
		 * per ogni valore di questo progetto fa la media e lo mette su
		 * printcolumns
		 */
		for (String column : columns.keySet()) {
			ArrayList<Long> values = columns.get(column);
			long somma = 0, media = 0;
			for (Long value : values) {
				somma += value;
			}
			if (values.size() != 0)
				media = (long) somma / values.size();

			ArrayList<Long> al = new ArrayList<Long>();
			al.add(media);
			printColumns.put(column, al);
		}

		// legge il file e lo mette su lines
		readFile();

		// per ogni linea letta
		for (String s : lines) {
			String[] fields = s.split(",");
			String key = fields[0];
			// se la key c'è anche in questo progetto
			if (printColumns.containsKey(key)) {
				// aggiunge i valori nella hashmap da scrivere
				for (int i = 1; i < fields.length; i++)
					printColumns.get(key).add(0, Long.parseLong(fields[i]));
			}
			// se la key non c'è
			else {
				ArrayList<Long> values = new ArrayList<Long>();
				for (int i = 1; i < fields.length; i++)
					values.add(Long.parseLong(fields[i]));
				printColumns.put(key, values);
			}
		}

		try {
			out = new PrintStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (String column : printColumns.keySet()) {

			out.print(column + ",");

			ArrayList<Long> values = printColumns.get(column);
			for (Long value : values) {
				out.print(value + ",");
			}
			out.println();
		}
	}

}
