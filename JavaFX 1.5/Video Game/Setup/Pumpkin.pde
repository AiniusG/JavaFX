public class Pumpkin extends AnimatedSprite{
  public Pumpkin(PImage img, float scale){
   super(img, scale);
   standNeutral = new PImage[1];
   standNeutral[0] = loadImage("data/textures/pumpkin.png");
   currentImages = standNeutral;
  }
}
