package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollData;

/**
 * Servlet that will create pie chart png image created with
 * current voting results.
 * Image will have width of 400 and height of 400.
 * 
 * @author Martin Sršen
 *
 */
public class GlasanjeGrafikaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Called by the server (via the service method) to allow a servlet to handle a GET request.
	 * If the request is incorrectly formatted, doGet returns an HTTP "Bad Request" message.
	 * 
	 * @param req	an HttpServletRequest object that contains the request the client has made of the servlet.
	 * @param resp	an HttpServletResponse object that contains the response the servlet sends to the client
	 * @throws ServletException	if the request for the GET could not be handled
	 * @throws IOException	if an input or output error is detected when the servlet handles the GET request
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
		int id = Integer.parseInt(req.getParameter("pollId"));
		List<PollData> results = DAOProvider.getDao().getPollDataList(id);
		
        PieDataset dataset = createDataset(results);
        JFreeChart chart = createChart(dataset, "Voting results");
        
        resp.setContentType("image/png");
        ChartUtils.writeChartAsPNG(resp.getOutputStream(), chart, 400, 400);
	}
	
	/**
	 * Method that created dataset that will be added into chart.
	 * Used given list to add all voting results into dataset.
	 * 
	 * @param results	list of results that will be added into dataset.
	 * @return	created dataset with all voting results from list.
	 */
    private  PieDataset createDataset(List<PollData> results) {
        DefaultPieDataset result = new DefaultPieDataset();
        
        for(PollData data : results) {
        	result.setValue(data.getName(), data.getNoVotes());
        }
        
        return result;
    }
    
    /**
     * Method that creates chart using given dataset, and title
     * to set chart to.
     * 
     * @param dataset	data used to draw chart.
     * @param title	chart title.
     * @return	created chart.
     */
    private JFreeChart createChart(PieDataset dataset, String title) {

        JFreeChart chart = ChartFactory.createPieChart3D(
            title,                  // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false
        );

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(90);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.9f);
        return chart;
    }
}
