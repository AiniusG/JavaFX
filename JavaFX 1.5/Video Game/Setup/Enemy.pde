public class Enemy extends AnimatedSprite{
  float boundryLeft, boundryRight;
  public Enemy(PImage img, float scale, float bLeft, float bRight){
    super(img, scale);
    moveLeft = new PImage[6];
    moveLeft[0] = loadImage("data/enemy/walk_left0.png");
    moveLeft[1] = loadImage("data/enemy/walk_left0.png");
    moveLeft[2] = loadImage("data/enemy/walk_left1.png");
    moveLeft[3] = loadImage("data/enemy/walk_left1.png");
    moveLeft[4] = loadImage("data/enemy/walk_left2.png");
    moveLeft[5] = loadImage("data/enemy/walk_left2.png");
    moveRight = new PImage[6];
    moveRight[0] = loadImage("data/enemy/walk_right0.png");
    moveRight[1] = loadImage("data/enemy/walk_right0.png");
    moveRight[2] = loadImage("data/enemy/walk_right1.png");
    moveRight[3] = loadImage("data/enemy/walk_right1.png");
    moveRight[4] = loadImage("data/enemy/walk_right2.png");
    moveRight[5] = loadImage("data/enemy/walk_right2.png");
    currentImages = moveRight;
    direction = RIGHT_FACING;
    boundryLeft = bLeft;
    boundryRight = bRight;
    change_x = 2;
  }
    void update(){
     super.update();
     if(getLeft() <= boundryLeft){
       setLeft(boundryLeft);
       change_x *= -1;
     }
     else if(getRight() >= boundryRight){
      setRight(boundryRight);
      change_x *= -1;
     }
    }
}
