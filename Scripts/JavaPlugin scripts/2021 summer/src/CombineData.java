
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombineData
{
  private enum EndingCriteria
  {
    EmptyNextLine
    {
      public boolean hasNext(String[] line)
      {
        return line != null && (line.length != 1 || !line[0].equals(" "));
      }
    },
    EmptyFile;

    public boolean hasNext(String[] line)
    {
      return line != null;
    }
  }

  private static List<File> getFileWithPrefix(File inputFolder, String prefix)
  {
    if (!inputFolder.isDirectory())
      throw new IllegalArgumentException("Input should be a folder instead of: " + inputFolder.getClass());
    List<File> res = new ArrayList<>();
    List<File> files = listFilesRecursively(inputFolder);
    files.forEach(f -> {
      if (f.getName().startsWith(prefix))
        res.add(f);
    });
    return res;
  }

  private static List<File> listFilesRecursively(File input)
  {
    if (input.isFile())
      return new ArrayList<>(Arrays.asList(input));
    List<File> res = new ArrayList<>();
    for (File child : input.listFiles())
      res.addAll(listFilesRecursively(child));
    return res;
  }

  private static void combine(File output, List<File> list, EndingCriteria criteria)
  {
    try {
      CSVWriter writer = new CSVWriter(new FileWriter(output));
      boolean noHeader = true;
      for (File file : list) {
        CSVReader reader = new CSVReader(new FileReader(file));
        if (noHeader) {
          noHeader = false;
          writer.writeNext(reader.readNext());
        } else {
          reader.readNext();
        }
        String[] line;
        while (criteria.hasNext(line = reader.readNext())) {
          writer.writeNext(line);
        }
      }
      writer.flush();
      writer.close();
    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args)
  {
    EndingCriteria criteria = EndingCriteria.EmptyNextLine;
    File input = new File("D:\\MockResearch\\Data\\RQ2");
    File output = new File(
        "D:\\MockResearch\\Data\\RQ2_MockedDependenciesCalculationSummary.csv");
    String prefix = "mocked dependencies calculation summary";
    List<File> fileList = getFileWithPrefix(input, prefix);
    System.out.println(fileList.size());
    fileList.forEach(a -> System.out.println(a));
    combine(output, fileList, criteria);
  }

}
