import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PainterPlus {

	private static int prevX ;
    private static int prevY ; 
    private static boolean dragging = true;
    private static Graphics2D g2d;
	
	/**
	 * Lista boja koje korisnik može odabrati za crtanje.
	 */
	private static Color[] palette = new Color[] {
		Color.WHITE,
		Color.BLACK,
		Color.RED,
		Color.BLUE,
		Color.GREEN,
		Color.CYAN,
		Color.MAGENTA,
		new Color(133, 7, 42),
	};
	
	/**
	 * Veličina kvadratića u paleti boja.
	 */
	private static int colorPickerSize = 50;
	
	public static void main(String[] args) {
		PaintListener listener = new PaintListener(); 
		
		Canvas canvasPanel = new Canvas();
		canvasPanel.setBackground(Color.WHITE);
		canvasPanel.addMouseListener(listener);
		canvasPanel.addMouseMotionListener(listener);
		
		JFrame mainWindow = new JFrame("Paint+");
		mainWindow.setContentPane(canvasPanel);
		
		// postavljamo širinu prozora tako da vidimo sve boje
		mainWindow.setSize(palette.length * colorPickerSize, 500);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
	}
	
	public static class PaintListener implements MouseListener, MouseMotionListener {
		private Color selectedColor = Color.RED;
		
		/**
		 * Provjerava koordinate na kojima se nalazio kursor miša i poredi
		 * s indeksima niza palette u kojem se nalaze boje iscrtane na dnu
		 * ekrana. Ako se klik poklapa s nekom pojom, stavlja tu boju u
		 * privatni atribut selectedColor.
		 * 
		 * Obratite pažnju da ovdje koristimo dva privatna statička atributa klase
		 * PainterPlus kojima imamo pristup jer je klasa PaintListener static
		 * nested klasa koja pripada klasi PaintPlus. Ako bismo klasu PaintListener
		 * izdvojili u poseban fajl, ovim atributima ne bismo imali pristup. 
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			Component source = (Component)e.getSource();
			dragging = true;
			if (e.getY() >= source.getHeight() - colorPickerSize) {
				for (int i = 0; i < palette.length; i++) {
					if (e.getX() < colorPickerSize * (i+1)) {
						selectedColor = palette[i];
						
						// ako bismo ovdje zaboravili break, for petlja
						// bi se nastavila izvršavati za ostale boje
						// a pošto bi uslov bio ispunjen i za njih,
						// bila bi odabrana posljednja broja u listi, a ne
						// ona na koju je korisnik kliknuo
						break;
					}
				}				
			}
		}

		//Funkcija je promijenjena tako da se ispisuje linija umjesto niza kružića
		
		@Override
		public void mouseDragged(MouseEvent e) {
			Component source = (Component)e.getSource();
			g2d = (Graphics2D) source.getGraphics();
			if (dragging == false)
			{
				return;
			}
		    int x = e.getX();   
		    int y = e.getY();   
		     
			g2d.setColor(selectedColor);
			g2d.setStroke(new BasicStroke(3));
			g2d.drawLine(prevX, prevY, x, y);
			prevX = x;
			prevY = y;
		        
		}

		@Override
		public void mouseReleased(MouseEvent e) 
		{ 
			if (dragging == false)
			{
				return;
			}
			dragging = false;
			g2d = null;
		}

		@Override
		public void mouseClicked(MouseEvent e) { }

		@Override
		public void mouseEntered(MouseEvent e) { }

		@Override
		public void mouseExited(MouseEvent e) { }

		@Override
		public void mouseMoved(MouseEvent e) { }
	}

	public static class Canvas extends JPanel {
		/**
		 * Crta onoliko pravougaonika različitih boja koliko imamo elemenata
		 * u listi palette.
		 * 
		 * Obratite pažnju da je palette privatni static atribut klase PainterPlus,
		 * međutim da mu još uvijek imamo pristup iz nested static klase Canvas.
		 * Ako bismo klasu Canvas izdvojili u poseban fajl, ne bismo više imali pristup
		 * privatnim atributima.  
		 */
		@Override
		public void paintComponent(Graphics g) {			
			for (int i = 0; i < palette.length; i++) {
				g.setColor(palette[i]);
				g.fillRect(colorPickerSize * i, getHeight() - colorPickerSize,
				           colorPickerSize, colorPickerSize);				
			}
			g.setColor(Color.BLACK);
			
		}
	}


}
