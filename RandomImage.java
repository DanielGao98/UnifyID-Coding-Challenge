// Daniel Gao, 01/07/2018

import java.io.*;
import java.net.*;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// This class creates an RGB bitmap picture of 128x128 pixels.
public class RandomImage {
  public static void main(String[] args) throws Exception
  {
    //create 3 parallel arrays with red, green, and blue values for each pixel 
    //If array is null, quota was insufficient
    int[] red = getRandomValues(); //create array of red values
    if (red == null) {
      System.exit(0);
    }
    
    //create array of green values
    int[] green = getRandomValues();
    if (green == null) {
      System.exit(0);
    }
    
    //create array of blue values
    int[] blue = getRandomValues();
    if (blue == null) {
      System.exit(0);
    }
    
    //initialize image to be created
    BufferedImage resultImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB); 
    
    int counter = 0;//counter to access array elements
    
    //Set pixel values inside picture
    for (int x = 0; x < 128; x++) {
      for (int y = 0; y < 128; y++) {
        int value = red[counter];
        value = (value << 8) + green[counter]; 
        value = (value << 8) + blue[counter];
        resultImage.setRGB(x, y, value);
        counter++;
      }
    }
    
    //output image to a file 
    File outputFile = new File("/output.bmp");
    ImageIO.write(resultImage, "bmp", outputFile); 
  }
  
  /**
   * Makes API call to check if quota is positive
   * @param none
   * @return true if positive, else negative
   */
  private static boolean checkQuota() throws Exception {
    //make the check quota API call
    URL url = new URL("https://www.random.org/quota/?ip=134.226.36.80&format=plain");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    //read contents of request into a string
    String quotaValue = reader.readLine();
    System.out.println(quotaValue);
    //return true if there is positive quota space available, else false
    if (Integer.parseInt(quotaValue) > 0) {
      return true;
    }
    else {
      return false;
    }
  }
  
  /**
   * Retrieves 16384 random values using the API call
   * Performs a while loop twice, each time getting 8192 values because
   * max requested at one time is 10^4
   * @param none
   * @return RGBValues array of random values
   */
  private static int[] getRandomValues() throws Exception {
    int[] RGBValues = new int[128*128];//create an array size 16384
    
    int i = 0; //counter for rgb array
    int j = 0; //Make call twice b/c max requested is 10^4 and need 16384
    while (j<2) {
      //make sure quota is sufficient
      if (checkQuota() == false) {
        return null;
      }
      //request random series of 8192 numbers, will do twice to get needed 16384
      URL url = new URL("https://www.random.org/integers/?num=8192&min=1&max=256&col=1&base=10&format=plain&rnd=new");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      //check quota again
      if (checkQuota() == false) {
        return null;
      }
       //read the requested random numbers
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      //read lines from reader until end, parse as ints into array
      while ((line = reader.readLine()) != null) {
        if (i == 16384) {
          break;
        }
        RGBValues[i] = Integer.parseInt(line);
        i++;
      }
      reader.close();
    }  
    return RGBValues;  
  }
  
}