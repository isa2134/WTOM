package wtom.model.pdf;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class GraficoService {

    public static String gerarGraficoDesafios(int corretas, int incorretas) {

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Corretas", corretas);
        dataset.setValue("Incorretas", incorretas);

        JFreeChart chart = ChartFactory.createPieChart(
                "Desafios", dataset, false, false, false);

        return chartToBase64(chart);
    }

    public static String gerarGraficoMedalhas(
            int ouro, int prata, int bronze, int mh) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(ouro, "Medalhas", "Ouro");
        dataset.addValue(prata, "Medalhas", "Prata");
        dataset.addValue(bronze, "Medalhas", "Bronze");
        dataset.addValue(mh, "Medalhas", "MH");

        JFreeChart chart = ChartFactory.createBarChart(
                "Medalhas", "", "Quantidade", dataset);

        return chartToBase64(chart);
    }

    private static String chartToBase64(JFreeChart chart) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(out, chart, 400, 300);
            return "data:image/png;base64," +
                   Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
