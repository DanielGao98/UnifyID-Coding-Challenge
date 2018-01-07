import java.io.*;
import java.net.*;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class RandomImage {
  public static void main(String[] args) throws Exception
  {
    //CHECK quota
    
    int[] red = makeAPICall();
    int[] green = makeAPICall();
    int[] blue = makeAPICall();
    
    BufferedImage resultImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB); 
    
    int counter = 0;
    for (int x = 0; x < 128; x++) {
      for (int y = 0; y < 128; y++) {
        int rgb = red[counter];
        rgb = (rgb << 8) + green[counter]; 
        rgb = (rgb << 8) + blue[counter];
        resultImage.setRGB(x, y, rgb);
        counter++;
      }
    }
    
    File outputFile = new File("/output.bmp");
    ImageIO.write(resultImage, "bmp", outputFile);
    
    
  }
  private static boolean checkQuota() throws Exception {
    URL url = new URL("https://www.random.org/quota/?format=plain");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    //read the requested random numbers
    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    if (reader.readLine() == null) {
      return false;
    }
    String quotaValue = reader.readLine();
    if (Integer.parseInt(quotaValue) > 0) {
      return true;
    }
    else {
      return false;
    }
  }
  
  private static int[] makeAPICall() throws Exception {
    int[] RGBValues = new int[128*128];
    
    int i = 0; //counter for rgb array
    int j = 0; //Make call twice b/c max requested is 10^4 and need 16384
    while (j<2) {
      //request random series of 8192 numbers, will do twice to get needed 16384
      URL url = new URL("https://www.random.org/integers/?num=10&min=1&max=6&col=1&base=10&format=plain&rnd=new");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      //read the requested random numbers
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      //read lines from reader until end, parse as ints into array
      while ((line = reader.readLine()) != null) {
        RGBValues[i] = Integer.parseInt(line);
        i++;
      }
      reader.close();
    }
    
    return RGBValues;  
  }
  
  
}