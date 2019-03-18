package hr.fer.zemris.java.webserver.workers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class that implements IWebWorker.
 * Creates image 200x200, draws circle with center in 100, 100
 * and radius of 100.
 * Writes image using given context onto site.
 * Can be accessed by /cw or /ext/CircleWorker.
 * Takes no parameters.
 * 
 * @author Martin Sršen
 *
 */
public class CircleWorker implements IWebWorker {

	/**
	 * Method that worker implements and determines the way request will be processed.
	 * 
	 * @param context	RequestContext used to read data from and write data to.
	 * @throws Exception	If exception happens during processing request.
	 */
	@Override
	public void processRequest(RequestContext context) throws Exception {
		BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);
		
		Graphics2D g2d = bim.createGraphics();
		g2d.fillOval(0, 0, 200, 200);
		g2d.dispose();
		
		context.setMimeType("image/png");
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bim, "png", bos);
			
			byte[] bytes = bos.toByteArray();
			context.setContentLength(Long.valueOf(bytes.length));
			
			context.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
