
package practicos.practico_09_integrador.src;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVParser simple helper used for the exercise. It returns a List of String[] where each array
 * contains the columns of a CSV line split by comma. This is a lightweight parser (does not
 * handle quoted commas) but is sufficient for the demo/testing data assumed here.
 */
public class CSVParser {
    public static List<String[]> parse(String path) throws IOException {
        // Intentos de localización del fichero:
        // 1) ruta tal cual fue pasada
        // 2) ruta relativa dentro del práctico: practicos/practico_09_integrador/<path>
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            Path alt = Paths.get("practicos/practico_09_integrador").resolve(path);
            if (Files.exists(alt)) {
                p = alt;
            } else {
                throw new IOException("CSV no encontrado. Intentados: '" + path + "' y '" + alt.toString() + "'");
            }
        }
        List<String> lines = Files.readAllLines(p);
        List<String[]> rows = new ArrayList<>();
        for (String line : lines) {
            if (line == null) continue;
            line = line.trim();
            if (line.isEmpty()) continue;
            // Simple split by comma
            String[] cols = line.split(",");
            for (int i = 0; i < cols.length; i++) cols[i] = cols[i].trim();
            rows.add(cols);
        }
        return rows;
    }
}
