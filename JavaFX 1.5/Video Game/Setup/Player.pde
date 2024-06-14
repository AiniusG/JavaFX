public class Player extends AnimatedSprite{ 
  int lives;
  boolean onPlatform, inPlace;
  PImage[] standLeft;
  PImage[] standRight;
  PImage[] jumpLeft;
  PImage[] jumpRight;
  public Player(PImage img, float scale){ //constructor
    super(img, scale);
    if(setupFirstTime)
      lives = 3;
    else
      lives = leftover;
    direction = RIGHT_FACING;
    onPlatform = true;
    inPlace = true;
    
    standLeft = new PImage[1];
    standLeft[0] = loadImage("data/player/idle1.png");
    standRight = new PImage [1];
    standRight[0] = loadImage("data/player/idle0.png");
    moveRight = new PImage[5];
    moveRight[0] = loadImage("data/player/run/run_right0.png");
    moveRight[1] = loadImage("data/player/run/run_right1.png");
    moveRight[2] = loadImage("data/player/run/run_right2.png");
    moveRight[3] = loadImage("data/player/run/run_right3.png");
    moveRight[4] = loadImage("data/player/run/run_right4.png");
    moveLeft = new PImage[5];
    moveLeft[0] = loadImage("data/player/run/run_left0.png");
    moveLeft[1] = loadImage("data/player/run/run_left1.png");
    moveLeft[2] = loadImage("data/player/run/run_left2.png");
    moveLeft[3] = loadImage("data/player/run/run_left3.png");
    moveLeft[4] = loadImage("data/player/run/run_left4.png");
    jumpRight = new PImage[3];
    jumpRight[0] = loadImage("data/player/jump/jump_right0.png");
    jumpRight[1] = loadImage("data/player/jump/jump_right1.png");
    jumpRight[2] = loadImage("data/player/jump/jump_right2.png");
    jumpLeft = new PImage[3];
    jumpLeft[0] = loadImage("data/player/jump/jump_left0.png");
    jumpLeft[1] = loadImage("data/player/jump/jump_left1.png");
    jumpLeft[2] = loadImage("data/player/jump/jump_left2.png");
    currentImages = standRight;
    
  }
  
  @Override
  public void updateAnimation(){
    onPlatform = isOnPlatform(this, platforms);  
    inPlace = change_x == 0 && change_y == 0;
    super.updateAnimation();
  }
  @Override
  public void selectDirection(){
    if(change_x > 0)
      direction = RIGHT_FACING;
    else if (change_x < 0)
      direction = LEFT_FACING;
    
  }
  @Override
  public void selectCurrentImages(){
    if(direction == RIGHT_FACING){
       if(inPlace){
         currentImages = standRight; 
       }
       else if(!onPlatform){
         currentImages = jumpRight;
       }
       else{
         currentImages = moveRight; 
       }
    }
    
    
    else if(direction == LEFT_FACING){
       if(inPlace){
         currentImages = standLeft; 
        }
       else if(!onPlatform){
         currentImages = jumpLeft;
       }
       else{
         currentImages = moveLeft; 
       }
    }
  }
}
