//@author Ainius Gecas 3 grupe 1 pogrupis
final static float MOVE_SPEED = 10;
final static float SPRITE_SCALE = 128/128;
final static float SPRITE_SIZE = 128;
final static float GRAVITY = 0.6;
final static float JUMP_SPEED = 20;

final static int NEUTRAL_FACING = 0;
final static int RIGHT_FACING = 1;
final static int LEFT_FACING = 2;

final static float WIDTH = SPRITE_SIZE * 16;
final static float HEIGHT = SPRITE_SIZE * 12;
final static float GROUND_LEVEL = HEIGHT - SPRITE_SIZE;
final static float ABYS = 1500;
final static float VICTORY = 7500;

final static float RIGHT_MARGIN = 960;
final static float LEFT_MARGIN = 960;
final static float VERTICAL_MARGIN = 540;


int levels = 0, leftover;
boolean isGameOver, setupFirstTime = true;
Heart h;
Enemy e;
Player p; // p - player
Sprite tr;
String map1 = "map1.csv", map2 = "map2.csv", map3 = "map3.csv";
PImage grass, dirt, grass1, grass2, side1, side2, corner1, corner2, i, pumpkin, trap, spider, heart;
ArrayList<Sprite> platforms;
ArrayList<Sprite> objects;
ArrayList<Sprite> health;
float view_x = 0;
float view_y = 0;

void setup(){ //called once in the begining or restart
  //if(levels == 0) //<>//
    //map = "data/map1.csv";
  size(1920,1080);
  imageMode(CENTER);
  
  i = loadImage("data/player/idle0.png");
  spider = loadImage("data/enemy/walk_right0.png");
  p = new Player(i , 1.5);
  setupFirstTime = false;
  p.setBottom(900);
  p.center_x = 1500;
  p.change_x = 0;
  p.change_y = 0;
  isGameOver = false;
  view_x = 0;
  view_y = 0;
  
  health = new ArrayList<Sprite>();
  platforms = new ArrayList<Sprite>();
  objects = new ArrayList<Sprite>();
  pumpkin = loadImage("data/textures/pumpkin.png");
  trap = loadImage("data/textures/trap.png");
  grass = loadImage("data/textures/grass.png");
  dirt = loadImage("data/textures/dirt.png");
  grass1 = loadImage("data/textures/grass2.png");
  grass2 = loadImage("data/textures/grass3.png");
  side1 = loadImage("data/textures/side1.png");
  side2 = loadImage("data/textures/side2.png");
  corner1 = loadImage("data/textures/corner1.png");
  corner2 = loadImage("data/textures/corner2.png");
  heart = loadImage("data/textures/heart.png");
  if(levels == 0)
    createPlatforms(map1);
  if(levels == 1)
    createPlatforms(map2);
  if(levels == 2)
    createPlatforms(map3);
    
}

void draw(){ //called auto every 60fps
  if(levels == 0)
    background(135,206,235);
  else if(levels == 1)
    background(254,147,128);
  else if(levels == 2)
    background(71,93,133);
  scroll();
  
  displayAll();
  
  if(!isGameOver){
    updateAll();
    checkDeath();
  }
}
void displayAll(){
  for(Sprite s: platforms){
    s.display();
  }
  for(Sprite pump: objects){
    pump.display();
  }
  for(Sprite h: health){
    h.display();
}
  p.display();
  e.display();
  
  fill(0);
  textSize(64);
  text("Lives: " + p.lives, view_x + 50, view_y + 100);
  
  if(isGameOver){
    fill(139,0,0);
    textSize(128);
    if(p.lives == 0){
      text("GAME OVER", view_x + width/2 - 380 , view_y + height/2 - 100 );
    }
    else if (p.lives >= 1){
      text("YOU WIN", view_x + width/2 - 280 , view_y + height/2 - 100);
    }
    text("Press SPACE to restart", view_x + width/2 - 600, view_y + height/2 + 28);
   }
}

void updateAll(){
  p.updateAnimation();
  resolvePlatformCollisions(p, platforms);
  e.update();
  e.updateAnimation();
  HeartCollision();
  if(p.getRight() >= VICTORY && levels == 0){
    leftover = p.lives;
    levels++;
    setup();
  }
  else if(p.getRight() >= VICTORY && levels == 1){
    leftover = p.lives;
    levels++;
    setup();
  }
  else if(p.getRight() >= VICTORY && levels == 2){
  isGameOver = true;
  }
}

void scroll(){
 float right_boundry = view_x + width - RIGHT_MARGIN;
 if(p.getRight() > right_boundry){
    view_x += p.getRight() - right_boundry; 
 }
 float left_boundry = view_x + LEFT_MARGIN;
 if(p.getLeft() < left_boundry){
    view_x -= left_boundry - p.getLeft();
 }
 float bottom_boundry = view_y + height - VERTICAL_MARGIN;
   if(p.getBottom() > bottom_boundry){
      view_y += p.getBottom() - bottom_boundry; 
   }
   
 float top_boundry = view_y + VERTICAL_MARGIN;
   if(p.getTop() < top_boundry){
      view_y -= top_boundry - p.getTop(); 
   }
   
   translate(-view_x, -view_y);
}

//void collectheart(){
//  if(checkCollision(p, h))
//    p.lives++;
    
  

void checkDeath(){
  boolean collideEnemy = checkCollision(p, e); 
  boolean fallOff = p.getBottom() > ABYS;
  if(collideEnemy|| fallOff){
   p.lives--;
   
    if(p.lives == 0){
      isGameOver = true;
    }
    else {
     p.center_x = 1500;
     p.setBottom(900);
    }
  }
}

void HeartCollision()
{
 ArrayList<Sprite> collisionList = checkCollisionList(p,health);
 if(collisionList.size() > 0)
 {
  for(Sprite h:collisionList)
    {
     health.remove(h);
     p.lives++;
    }
 }
}

public boolean isOnPlatform(Sprite s, ArrayList<Sprite> walls){
  s.center_y += 5;
  ArrayList<Sprite> col_list = checkCollisionList(s, walls);
  s.center_y -= 5;
  if(col_list.size() > 0){
    return true;
  }
  else
    return false;
}

public void resolvePlatformCollisions(Sprite s, ArrayList<Sprite> walls){
  s.change_y += GRAVITY;
  
  
  s.center_y += s.change_y;
  ArrayList<Sprite> col_list = checkCollisionList(s, walls);
  if(col_list.size() > 0){
   Sprite collided = col_list.get(0);
   if(s.change_y > 0){
     s.setBottom(collided.getTop());
   }
   else if(s.change_y < 0){
     s.setTop(collided.getBottom()); 
   }
   s.change_y = 0;
  }
  
  s.center_x += s.change_x;
  col_list = checkCollisionList(s, walls);
  if(col_list.size() > 0){
   Sprite collided = col_list.get(0);
   if(s.change_x > 0){
     s.setRight(collided.getLeft());
   }
   else if(s.change_x < 0){
     s.setLeft(collided.getRight()); 
   }
   s.change_y = 0;
  }
}

boolean checkCollision(Sprite s1, Sprite s2){
  boolean noXOverlap = s1.getRight() <= s2.getLeft() || s1.getLeft() >= s2.getRight();
  boolean noYOverlap = s1.getBottom() <= s2.getTop() || s1.getTop() >= s2.getBottom();
  if(noXOverlap || noYOverlap){
    return false;
  }
  else{
    return true;
  }
}



public ArrayList<Sprite> checkCollisionList(Sprite s, ArrayList<Sprite> list){
  ArrayList<Sprite> collision_list = new ArrayList<Sprite>();
  for(Sprite p: list){
   if(checkCollision(s,p))
     collision_list.add(p);
  }
  return collision_list;
}

void createPlatforms(String filename){
  String[] lines = loadStrings(filename);
  for(int row = 0; row < lines.length; row++){
    String[] values = split(lines[row],";");
    for(int col = 0; col < values.length; col++){
      if(values[col].equals("1")){
        Sprite s = new Sprite(grass, SPRITE_SCALE);
        s.center_x = SPRITE_SIZE/2 + col * SPRITE_SIZE;
        s.center_y = SPRITE_SIZE/2 + row * SPRITE_SIZE;
        platforms.add(s);
      }
      else if(values[col].equals("2")){
        Sprite s = new Sprite(dirt, SPRITE_SCALE);
        s.center_x = SPRITE_SIZE/2 + col * SPRITE_SIZE;
        s.center_y = SPRITE_SIZE/2 + row * SPRITE_SIZE;
        platforms.add(s);
      }
      else if(values[col].equals("gc1")){
        Sprite s = new Sprite(grass1, SPRITE_SCALE);
        s.center_x = SPRITE_SIZE/2 + col * SPRITE_SIZE;
        s.center_y = SPRITE_SIZE/2 + row * SPRITE_SIZE;
        platforms.add(s);
      }
      else if(values[col].equals("gc2")){
        Sprite s = new Sprite(grass2, SPRITE_SCALE);
        s.center_x = SPRITE_SIZE/2 + col * SPRITE_SIZE;
        s.center_y = SPRITE_SIZE/2 + row * SPRITE_SIZE;
        platforms.add(s);
      }
      else if(values[col].equals("s1")){
        Sprite s = new Sprite(side1, SPRITE_SCALE);
        s.center_x = SPRITE_SIZE/2 + col * SPRITE_SIZE;
        s.center_y = SPRITE_SIZE/2 + row * SPRITE_SIZE;
        platforms.add(s);
      }
      else if(values[col].equals("s2")){
        Sprite s = new Sprite(side2, SPRITE_SCALE);
        s.center_x = SPRITE_SIZE/2 + col * SPRITE_SIZE;
        s.center_y = SPRITE_SIZE/2 + row * SPRITE_SIZE;
        platforms.add(s);
      }
      else if(values[col].equals("c1")){
        Sprite s = new Sprite(corner1, SPRITE_SCALE);
        s.center_x = SPRITE_SIZE/2 + col * SPRITE_SIZE;
        s.center_y = SPRITE_SIZE/2 + row * SPRITE_SIZE;
        platforms.add(s);
      }
      else if(values[col].equals("c2")){
        Sprite s = new Sprite(corner2, SPRITE_SCALE);
        s.center_x = SPRITE_SIZE/2 + col * SPRITE_SIZE;
        s.center_y = SPRITE_SIZE/2 + row * SPRITE_SIZE;
        platforms.add(s);
      }
      else if(values[col].equals("p")){
        Pumpkin pump = new Pumpkin(pumpkin, SPRITE_SCALE);
        pump.center_x = SPRITE_SIZE/2 + col * SPRITE_SIZE;
        pump.center_y = SPRITE_SIZE/1.3 + row * SPRITE_SIZE;
        objects.add(pump);
      }
       else if(values[col].equals("t")){
        Sprite tr = new Sprite(trap, SPRITE_SCALE);
        tr.center_x = SPRITE_SIZE/2 + col * SPRITE_SIZE;
        tr.center_y = SPRITE_SIZE/2 + row * SPRITE_SIZE;
        objects.add(tr);
      }
        else if(values[col].equals("spi")){
          float bLeft = col * SPRITE_SIZE;
          float bRight = bLeft + 4 * SPRITE_SIZE;
          e = new Enemy(spider, SPRITE_SCALE, bLeft, bRight);
          e.center_x = SPRITE_SIZE/2 + col * SPRITE_SIZE;
          e.center_y = SPRITE_SIZE/1.3 + row * SPRITE_SIZE;
        }
        else if(values[col].equals("h")){
          Heart h = new Heart(heart, SPRITE_SCALE);
          h.center_x = SPRITE_SIZE/2 + col * SPRITE_SIZE;
          h.center_y = SPRITE_SIZE/2 + row * SPRITE_SIZE;
          health.add(h);
        }
    }
  }
}

void keyPressed(){ //call when key is pressed
  if(keyCode == RIGHT){
     p.change_x = MOVE_SPEED; 
  }
  else if(keyCode == LEFT){
     p.change_x = -MOVE_SPEED; 
  }
  else if(keyCode == UP && isOnPlatform(p, platforms)){
     p.change_y = -JUMP_SPEED;
  }
  else if (isGameOver && key == ' '){
    levels = 0;
    leftover = 3;
    setup();
  }
}

void keyReleased(){ //call when key is released
  if(keyCode == RIGHT){
     p.change_x = 0; 
  }
  if(keyCode == LEFT){
     p.change_x = 0; 
  }
}
