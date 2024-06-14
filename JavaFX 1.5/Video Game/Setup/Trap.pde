public class Trap extends AnimatedSprite{
  float boundryLeft, boundryRight;
  public Trap(PImage img, float scale, float bLeft, float bRight){
   super(img, scale);
   standNeutral = new PImage[1];
   standNeutral[0] = loadImage("data/textures/trap.png");
   currentImages = standNeutral;
   boundryLeft = bLeft;
   boundryRight = bRight;

  }
}
