import java.io.*;
import java.util.*;



class constants{
    public static ArrayList<Move> PARENTS = new ArrayList<Move>();
    public static int MAX_DEPTH=3;
    public static int MAX_PLAYER = 1;
    //1 for black and 2 for white
}

class Move { 
     Coord from;
     Coord to;
     float eval; 
     ArrayList<Coord> path = new ArrayList<Coord>();
     char type;


    public Move(Coord from, Coord to, float eval, ArrayList<Coord> path,char type) {
        this.eval = eval;
        this.from = from; 
        this.path = path;
        this.to = to;
        this.type = type;
    } 

    public void setMove(Coord from, Coord to, float eval, ArrayList<Coord> path, char type) {
        this.eval = eval;
        this.from = from; 
        this.to = to;
        this.path = path;
        this.type = type;
    }

    public float geteval() { 
        return this.eval; 
    } 

    public void seteval(float p) { 
        this.eval = p; 
    } 

    public char getType() { 
        return this.type; 
    } 

    public void setType(char p) { 
        this.type = p; 
    } 

    public Coord getFrom() { 
        return this.from; 
    } 

    public void setFrom(Coord from) { 
        this.from = from; 
    } 

    public Coord getTo() { 
        return this.to; 
    } 

    public void setTo(Coord to) { 
        this.to = to; 
    }  

    public ArrayList<Coord> getPar() { 
        return this.path; 
    } 

    public void setPar(ArrayList<Coord> path) { 
        this.path = path; 
    } 

    

} 


class Coord { 
     int x;
     int y; 


    public Coord(int x, int y) {
        this.x = x;
        this.y = y; 
    } 

    public void setCoord(int x, int y) {
        this.x = x;
        this.y = y; 
    }

    public int getX() { 
        return this.x; 
    } 

    public void setX(int x) { 
        this.x = x; 
    } 

    public int getY() { 
        return this.y; 
    } 

    public void setY(int y) { 
        this.y = y; 
    } 

    
} 

class gridCell { 
     int x; 
     int y; 
     int pos; //0 for ., 1 for black, 2 for white

    public gridCell(int x, int y, int pos) {
        
        this.pos = pos;
        this.x = x; 
        this.y = y; 
    } 

    public void setCell(int x, int y, int pos) {
        this.pos = pos; 
        this.x = x; 
        this.y = y; 
    }

    public int getPos() { 
        return this.pos; 
    } 

    public void setPos(int p) { 
        this.pos = p; 
    } 

    public int getX() { 
        return this.x; 
    } 

    public void setX(int x) { 
        this.x = x; 
    } 

    public int getY() { 
        return this.y; 
    } 

    public void setY(int y) { 
        this.y = y; 
    } 

    
} 

class Board { 

    gridCell[][] cells = new gridCell[16][16]; 

    public Board() { 
        this.initBoard(); 
    } 

    
    public void initBoard() { 

        for (int i = 0; i < 16; i++) { 
            for (int j = 0; j < 16; j++) { 
                cells[i][j] = new gridCell(i,j,0);
            } 
        } 
    }


    public void printBoard(Board board) {
        for(int i=0;i<16;i++) {
            for(int j=0;j<16;j++) {
                int c;
                char tc;
                
                c = cells[i][j].getPos();
                if(c==0) tc = '.';
                else if(c==1) tc = 'B';
                else tc = 'W';
                System.out.print(tc);
                
            }
            System.out.println();
        }
    }

    public void setCellBoard(int x, int y, int col) {
        
        cells[x][y].setCell(x,y,col);
    }

    public void validMoves(Board board, gridCell[] positions, int color) {

        
        int x,y,endx=-1,endy=-1,c;
        String type="F";
        gridCell[] reserveOutCamp = new gridCell[19];
        gridCell[] reserveInCamp = new gridCell[19];
        gridCell[] reserveOppCamp = new gridCell[19];

        int campLoc;
        int countOut=0;
        int countIn=0;
        int countOpp = 0;
        //int baseEval = evalBoard(board,color);
        for(int i=0;i<19;i++) {
            x = positions[i].getX();
            y = positions[i].getY();
            c = positions[i].getPos();


            
            //write a function that takes in x,y and checks if it lies outside / home camp  / opposing camp
            campLoc = checkLocation(x,y,color);

            // if 0 then outside both camps, if 1 then in home camp, if 2 then in opposing camp
            
            if(campLoc==0) {
                //outside camp, keep for reserve
                reserveOutCamp[countOut] = new gridCell(x,y,c);
                countOut++;
                //System.out.println("COUNTOUT VALUE IS " + countOut);
                continue;
            }

            // if in opposing camp, then it can move wherever it wants, INSIDE THE CAMP ONLY
            if(campLoc ==2) {
                reserveOppCamp[countOpp] = new gridCell(x,y,c);
                countOpp++;
                continue;
            }
            
            reserveInCamp[countIn] = new gridCell(x,y,c);
            countIn++;
            System.out.println("right now x and y is " + x + "  " + y);

            if((x+1)<16 && (y)<16 && (x+1)>=0 && y>=0 && ((board.cells[x+1][y].getPos()==0)) && checkLocation(x+1,y,color)==0) {
                endx = x+1;
                type = "E";
                endy = y;

            }
            else if(y<16 && (x+2)<16 && (x+1)>=0 && y>=0 && board.cells[x+1][y].getPos()!=0 && board.cells[x+2][y].getPos()==0 && checkLocation(x+2,y,color)==0) {
                
                endx = x+2;
                type = "J";
                endy = y;
                
            }
            else if((x+1)<16 && (y+1)<16 && (x+1)>=0 && (y+1)>=0 && ((board.cells[x+1][y+1].getPos()==0) && checkLocation(x+1,y+1,color)==0)) { 
                endx = x+1;
                endy = y+1;
                type = "E";
            }
            else if ((x+2)<16 && (y+2)<16 &&  (x+1)>=0 && (y+1)>=0 && board.cells[x+1][y+1].getPos()!=0 && board.cells[x+2][y+2].getPos()==0 && checkLocation(x+2,y+2,color)==0) {
                endx = x+2;
                endy = y+2;
                type = "J";
            }
            else if((x-1)<16 && (y)<16 && (x-1)>=0 && y>=0 && ((board.cells[x-1][y].getPos()==0) && checkLocation(x-1,y,color)==0)) {
                endx = x-1;
                type = "E";
                endy = y;   
            }
            else if((x-2)>=0 && y<16 && (x-1)>=0 && y>=0 && board.cells[x-1][y].getPos()!=0 && board.cells[x-2][y].getPos()==0 && checkLocation(x-2,y,color)==0) {
                endx = x-2;
                type = "J";
                endy = y;
            }
            else if((x-1)<16 && (y-1)<16 && (x-1)>=0 && (y-1)>=0 && (board.cells[x-1][y-1].getPos()==0) && checkLocation(x-1,y-1,color)==0) {
                endx = x-1;
                endy = y-1;
                type = "E";
            }
            else if((x-2)>=0 && (y-2)>=0 && (x-2)<16 && (y-2)<16 && board.cells[x-1][y-1].getPos()!=0 && board.cells[x-2][y-2].getPos()==0 && checkLocation(x-2,y-2,color)==0) {
                endx = x-2;
                endy = y-2;
                type = "J";
            }

            else if((x)<16 && (y+1)<16 && (x)>=0 && (y+1)>=0 && ((board.cells[x][y+1].getPos()==0) && checkLocation(x,y+1,color)==0)) {
                endy = y+1;
                type = "E";
                endx = x;
            }
            else if ((y+2)<16 && x<16 && x>=0 && (y+2)>=0 && board.cells[x][y+1].getPos()!=0 && board.cells[x][y+2].getPos()==0  && checkLocation(x,y+2,color)==0) {
                endy = y+2;
                type = "J";
                endx = x;
            }
            else if((x)<16 && (y-1)<16 && (x)>=0 && (y-1)>=0 && ((board.cells[x][y-1].getPos()==0)  && checkLocation(x,y-1,color)==0)) {
                endy = y-1;
                type = "E";
                endx = x; 
            }
            else if((y-2)>=0 && x<16 && x>=0 && (y-2)<16 && board.cells[x][y-1].getPos()!=0 && board.cells[x][y-2].getPos()==0 && checkLocation(x,y-2,color)==0)  {
                endy = y-2;
                type = "J";
                endx = x;
            }
            else if((x+1)<16 && (y-1)<16 && (x+1)>=0 && (y-1)>=0 && ((board.cells[x+1][y-1].getPos()==0) && checkLocation(x+1,y-1,color)==0)) {
                endx = x+1;
                endy = y-1;
                type = "E";
            }
            else if((y-2)>=0 && (x+2)<16 && (y-2)<16 && (x+2)>=0 && board.cells[x+1][y-1].getPos()!=0 && board.cells[x+2][y-2].getPos()==0 && checkLocation(x+2,y-2,color)==0) {
                endy = y-2;
                endx = x+2;
                type = "J";
            }
            else if((x-1)<16 && (y+1)<16 && (x-1)>=0 && (y+1)>=0 && (board.cells[x-1][y+1].getPos()==0) && checkLocation(x-1,y+1,color)==0) {
                endx = x-1;
                endy = y+1;
                type = "E";
            }
            else if((y+2)<16 && (x-2)>=0 && (x-2)<16 && (y+2)>=0 && board.cells[x-1][y+1].getPos()!=0 && board.cells[x-2][y+2].getPos()==0 && checkLocation(x-2,y+2,color)==0) {
                endx = x-2;
                endy = y+2;
                type = "J";
            }
            else{
                

            }
            if(type!="F") {
                cells[x][y].setCell(x,y,0);
                cells[endx][endy].setCell(endx,endy,c);
                
                try{
                    Board.usingBufferedWritter(type,x,y,endx,endy); 
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
                return; 
            }   
        }

        //trying for multijumps ab
        for(int nm=0;nm<countIn;nm++) {
            int j;
            x = reserveInCamp[nm].getX();
            y = reserveInCamp[nm].getY();
            c = reserveInCamp[nm].getPos();
                
            //try for multijumps
            //try multijumps and see if any pt lies outside camp
            int[][] visited = new int[16][16];
            ArrayList<Move> moves = new ArrayList<Move>();
            ArrayList<Coord> path = new ArrayList<Coord>();
            path.add(new Coord(x,y));
            getJump(board,x,y,c,0,visited,moves,path,0,0);
            if(!moves.isEmpty()) {
                for(int m=0;m<moves.size();m++){
                    int ex = moves.get(m).getTo().getX();
                    int ey = moves.get(m).getTo().getY();
                    //System.out.println("excuse me color is " + color);
                    //System.out.println("excuse me x is  " + ex + " and y is " + ey);
                    if(checkLocation(ex,ey,color)==0){
                        //System.out.println("From (x,y): (" + moves.get(m).getFrom().getX() + "," + moves.get(m).getFrom().getY() + "), To (x,y): (" + moves.get(m).getTo().getX() + "," + moves.get(m).getTo().getY() + ") ," + moves.get(m).geteval()+ " ");
                        //System.out.println("Path is ");
                        for(j=0;j<moves.get(m).getPar().size();j++) {
                            //System.out.print(moves.get(m).getPar().get(j).getX() + "  " + moves.get(m).getPar().get(j).getY() + "  ");
                            if(moves.get(m).getPar().get(j).getX()==moves.get(m).getTo().getX() && moves.get(m).getPar().get(j).getY()==moves.get(m).getTo().getY()) {
                                break;
                            }
                        }
                        for(int l = moves.get(m).getPar().size() - 1; l > j; l--)
                            moves.get(m).getPar().remove(l);
                        //System.out.println("ABHI");
                        
                        ArrayList<Coord> actPath = new ArrayList<Coord>();
                        //for(j=moves.get(m).getPar().size()-1;j>=0;j--) {
                        j = moves.get(m).getPar().size()-1;
                        actPath.add(new Coord(moves.get(m).getPar().get(j).getX(),moves.get(m).getPar().get(j).getY()));
                        int temp = m;
                        if(m!=0){
                            do{
                                while(moves.get(temp).getFrom().getX()!=moves.get(m-1).getTo().getX() && moves.get(temp).getFrom().getY()!=moves.get(m-1).getTo().getY()) {
                                    m--;
                                }
                                actPath.add(new Coord(moves.get(m-1).getTo().getX(),moves.get(m-1).getTo().getY()));
                                //System.out.println("abey m ka value is " + m);
                                temp = m-1;
                                m--;
                            }while(m>=1);
                            actPath.add(new Coord(moves.get(0).getFrom().getX(),moves.get(0).getFrom().getY()));
                            //System.out.println("me is the best");
                        }
                        else{
                            actPath.add(new Coord(moves.get(m).getFrom().getX(),moves.get(m).getFrom().getY()));
                            actPath.add(new Coord(x,y));

                        }
                        
                        
                        try{
                            //Board.usingBufferedWritter("J",x,y,endx,endy); 
                            int from1, from2, to1, to2;                   
                            String fileContent;
                            //System.out.println("print kyum nahi ho raha bey");
                            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
                            for(int k=actPath.size()-1;k>1;k--){
                                from1 = actPath.get(k).getY();
                                from2 = actPath.get(k).getX();
                                to1 = actPath.get(k-1).getY();
                                to2 = actPath.get(k-1).getX();
                                fileContent ="J" + " " + Integer.toString(from1) + "," + Integer.toString(from2) + " " + Integer.toString(to1) + "," + Integer.toString(to2) + "\n";
                                writer.write(fileContent);
                            }
                            from1 = actPath.get(1).getY();
                            from2 = actPath.get(1).getX();
                            to1 = actPath.get(0).getY();
                            to2 = actPath.get(0).getX();
                            fileContent ="J" + " " + Integer.toString(from1) + "," + Integer.toString(from2) + " " + Integer.toString(to1) + "," + Integer.toString(to2);
                            writer.write(fileContent);
                            writer.close();
                        }
                        catch(IOException e) {
                            e.printStackTrace();
                        }
                        return;

                    }
                }
                
            }
        }
                


        
        //tried all positions for all coins inside camp. nahi hua. now for pieces in camp, try to move further away
        for(int i=0;i<countIn;i++) {
            x = reserveInCamp[i].getX();
            y = reserveInCamp[i].getY();
            c = reserveInCamp[i].getPos();

            //System.out.println("c ka value" + c);

            if(c==1) {
                if((x+1)<16 && (y)<16 && (x+1)>=0 && y>=0 && (board.cells[x+1][y].getPos()==0) && checkLocation(x+1,y,color)==1) {
                endx = x+1;
                type = "E";
                endy = y;
                
                }
                else if(y<16 && (x+2)<16 && (x+1)>=0 && y>=0 && board.cells[x+2][y].getPos()==0 && board.cells[x+1][y].getPos()!=0 && checkLocation(x+2,y,color)==1) {
                    endx = x+2;
                    type = "J";
                    endy = y;
                    
                }
                else if((x+1)<16 && (y+1)<16 && (x+1)>=0 && (y+1)>=0 && (board.cells[x+1][y+1].getPos()==0) && checkLocation(x+1,y+1,color)==1) { 
                    endx = x+1;
                    endy = y+1;
                    type = "E";
                }
                else if ((x+2)<16 && (y+2)<16 &&  (x+1)>=0 && (y+1)>=0 && board.cells[x+2][y+2].getPos()==0 && board.cells[x+1][y+1].getPos()!=0 && checkLocation(x+2,y+2,color)==1) {
                    endx = x+2;
                    endy = y+2;
                    type = "J";
                }
                else if((x)<16 && (y+1)<16 && (x)>=0 && (y+1)>=0 && (board.cells[x][y+1].getPos()==0) && checkLocation(x,y+1,color)==1) {
                    endy = y+1;
                    type = "E";
                    endx = x;
                }
                else if ((y+2)<16 && x<16 && x>=0 && (y+2)>=0 && board.cells[x][y+2].getPos()==0 && board.cells[x][y+1].getPos()!=0 && checkLocation(x,y+2,color)==1) {
                    endy = y+2;
                    type = "J";
                    endx = x;
                }
            }
            else if(c==2){
                if((x-1)<16 && (y)<16 && (x-1)>=0 && y>=0 && (board.cells[x-1][y].getPos()==0) && checkLocation(x-1,y,color)==1) {
                    endx = x-1;
                    type = "E";
                    endy = y;   
                }
                else if((x-2)>=0 && y<16 && (x-1)>=0 && y>=0 && board.cells[x-2][y].getPos()==0 && board.cells[x-1][y].getPos()!=0 && checkLocation(x-2,y,color)==1) {
                    endx = x-2;
                    type = "J";
                    endy = y;
                }
                else if((x-1)<16 && (y-1)<16 && (x-1)>=0 && (y-1)>=0 && (board.cells[x-1][y-1].getPos()==0) && checkLocation(x-1,y-1,color)==1) {
                    endx = x-1;
                    endy = y-1;
                    type = "E";
                }
                else if((x-2)>=0 && (y-2)>=0 && (x-2)<16 && (y-2)<16 && board.cells[x-2][y-2].getPos()==0 && board.cells[x-1][y-1].getPos()!=0 && checkLocation(x-2,y-2,color)==1) {
                    endx = x-2;
                    endy = y-2;
                    type = "J";
                }
                else if((x)<16 && (y-1)<16 && (x)>=0 && (y-1)>=0 && (board.cells[x][y-1].getPos()==0) && checkLocation(x,y-1,color)==1) {
                    endy = y-1;
                    type = "E";
                    endx = x; 
                }
                else if((y-2)>=0 && x<16 && x>=0 && (y-2)<16 && board.cells[x][y-2].getPos()==0 && board.cells[x][y-1].getPos()!=0 && checkLocation(x,y-2,color)==1)  {
                    endy = y-2;
                    type = "J";
                    endx = x;
                }
            }
            if(type!="F") {
                cells[x][y].setCell(x,y,0);
                cells[endx][endy].setCell(endx,endy,c);
                //System.out.println("found in reserveinCamp");
                //System.out.print("for x as "+x+" and y as "+y+" i found end x as " + endx+ " and end y as "+endy);
                try{
                    Board.usingBufferedWritter(type,x,y,endx,endy); 
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
                return; 
            }
        }// reserveincamp for loop ends

        

        //try for pieces outside camp. 
        for(int i=0;i<countOut;i++) {
            //System.out.println("inside third for loop");
            x = reserveOutCamp[i].getX();
            y = reserveOutCamp[i].getY();
            c = reserveOutCamp[i].getPos();

            if((x+1)<16 && (y)<16 && (x+1)>=0 && y>=0 && ((board.cells[x+1][y].getPos()==0)) && checkLocation(x+1,y,color)!=1) {
                endx = x+1;
                type = "E";
                endy = y;
                
            }
            else if(y<16 && (x+2)<16 && (x+1)>=0 && y>=0 && board.cells[x+2][y].getPos()==0 && board.cells[x+1][y].getPos()!=0 && checkLocation(x+2,y,color)!=1) {
                endx = x+2;
                type = "J";
                endy = y;
                
            }
            else if( (x+1)<16 && (y+1)<16 && (x+1)>=0 && (y+1)>=0 && ((board.cells[x+1][y+1].getPos()==0) && checkLocation(x+1,y+1,color)!=1)) { 
                endx = x+1;
                endy = y+1;
                type = "E";
            }
            else if ((x+2)<16 && (y+2)<16 &&  (x+1)>=0 && (y+1)>=0 && board.cells[x+2][y+2].getPos()==0 && board.cells[x+1][y+1].getPos()!=0 && checkLocation(x+2,y+2,color)!=1) {
                endx = x+2;
                endy = y+2;
                type = "J";
            }
            else if((x-1)<16 && (y)<16 && (x-1)>=0 && y>=0 && ((board.cells[x-1][y].getPos()==0) && checkLocation(x-1,y,color)!=1)) {
                endx = x-1;
                type = "E";
                endy = y;   
            }
            else if((x-2)>=0 && y<16 && (x-1)>=0 && y>=0 && board.cells[x-2][y].getPos()==0 && board.cells[x-1][y].getPos()!=0 && checkLocation(x-2,y,color)!=1) {
                endx = x-2;
                type = "J";
                endy = y;
            }
            else if((x-1)<16 && (y-1)<16 && (x-1)>=0 && (y-1)>=0 && (board.cells[x-1][y-1].getPos()==0) && checkLocation(x-1,y-1,color)!=1) {
                endx = x-1;
                endy = y-1;
                type = "E";
            }
            else if((x-2)>=0 && (y-2)>=0 && (x-2)<16 && (y-2)<16 && board.cells[x-2][y-2].getPos()==0 && board.cells[x-1][y-1].getPos()!=0 && checkLocation(x-2,y-2,color)!=1) {
                endx = x-2;
                endy = y-2;
                type = "J";
            }

            else if((x)<16 && (y+1)<16 && (x)>=0 && (y+1)>=0 && ((board.cells[x][y+1].getPos()==0) && checkLocation(x,y+1,color)!=1)) {
                endy = y+1;
                type = "E";
                endx = x;
            }
            else if ((y+2)<16 && x<16 && x>=0 && (y+2)>=0 && board.cells[x][y+2].getPos()==0 && board.cells[x][y+1].getPos()!=0 && checkLocation(x,y+2,color)!=1) {
                endy = y+2;
                type = "J";
                endx = x;
            }
            else if((x)<16 && (y-1)<16 && (x)>=0 && (y-1)>=0 && ((board.cells[x][y-1].getPos()==0)  && checkLocation(x,y-1,color)!=1)) {
                endy = y-1;
                type = "E";
                endx = x; 
            }
            else if((y-2)>=0 && x<16 && x>=0 && (y-2)<16 && board.cells[x][y-2].getPos()==0 && board.cells[x][y-1].getPos()!=0 && checkLocation(x,y-2,color)!=1)  {
                endy = y-2;
                type = "J";
                endx = x;
            }
            else if((x+1)<16 && (y-1)<16 && (x+1)>=0 && (y-1)>=0 && ((board.cells[x+1][y-1].getPos()==0) && checkLocation(x+1,y-1,color)!=1)) {
                endx = x+1;
                endy = y-1;
                type = "E";
            }
            else if((y-2)>=0 && (x+2)<16 && (y-2)<16 && (x+2)>=0 && board.cells[x+2][y-2].getPos()==0 && board.cells[x+1][y-1].getPos()!=0 && checkLocation(x+2,y-2,color)!=1) {
                endy = y-2;
                endx = x+2;
                type = "J";
            }
            else if((x-1)<16 && (y+1)<16 && (x-1)>=0 && (y+1)>=0 && (board.cells[x-1][y+1].getPos()==0) && checkLocation(x-1,y+1,color)!=1) {
                endx = x-1;
                endy = y+1;
                type = "E";
            }
            else if((y+2)<16 && (x-2)>=0 && (x-2)<16 && (y+2)>=0 && board.cells[x-2][y+2].getPos()==0 && board.cells[x-1][y+1].getPos()!=0 && checkLocation(x-2,y+2,color)!=1) {
                endx = x-2;
                endy = y+2;
                type = "J";
            }
            if(type!="F") {
                cells[x][y].setCell(x,y,0);
                cells[endx][endy].setCell(endx,endy,c);
                //System.out.println("found in reserveOutCamp");
                //System.out.print("for x as "+x+" and y as "+y+" i found end x as " + endx+ " and end y as "+endy);
                try{
                    Board.usingBufferedWritter(type,x,y,endx,endy); 
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
                return; 
            }
        }
        
        //trying for pieces in opposing camp to move in opposing camp
        for(int i=0;i<countOpp;i++) {
            //System.out.println("inside opposing camp for loop");
            x = reserveOppCamp[i].getX();
            y = reserveOppCamp[i].getY();
            c = reserveOppCamp[i].getPos();

            if((x+1)<16 && (y)<16 && (x+1)>=0 && y>=0 && ((board.cells[x+1][y].getPos()==0)) && checkLocation(x+1,y,color)==2) {
                endx = x+1;
                type = "E";
                endy = y;
                
            }
            else if(y<16 && (x+2)<16 && (x+1)>=0 && y>=0 && board.cells[x+1][y].getPos()!=0 && board.cells[x+2][y].getPos()==0 && checkLocation(x+2,y,color)==2) {
                endx = x+2;
                type = "J";
                endy = y;
                
            }
            else if( (x+1)<16 && (y+1)<16 && (x+1)>=0 && (y+1)>=0 && ((board.cells[x+1][y+1].getPos()==0) && checkLocation(x+1,y+1,color)==2)) { 
                endx = x+1;
                endy = y+1;
                type = "E";
            }
            else if ((x+2)<16 && (y+2)<16 &&  (x+1)>=0 && (y+1)>=0 && board.cells[x+1][y+1].getPos()!=0 && board.cells[x+2][y+2].getPos()==0 && checkLocation(x+2,y+2,color)==2) {
                endx = x+2;
                endy = y+2;
                type = "J";
            }
            else if((x-1)<16 && (y)<16 && (x-1)>=0 && y>=0 && ((board.cells[x-1][y].getPos()==0) && checkLocation(x-1,y,color)==2)) {
                endx = x-1;
                type = "E";
                endy = y;   
            }
            else if((x-2)>=0 && y<16 && (x-1)>=0 && y>=0 && board.cells[x-1][y].getPos()!=0 && board.cells[x-2][y].getPos()==0 && checkLocation(x-2,y,color)==2) {
                endx = x-2;
                type = "J";
                endy = y;
            }
            else if((x-1)<16 && (y-1)<16 && (x-1)>=0 && (y-1)>=0 && (board.cells[x-1][y-1].getPos()==0) && checkLocation(x-1,y-1,color)==2) {
                endx = x-1;
                endy = y-1;
                type = "E";
            }
            else if((x-2)>=0 && (y-2)>=0 && (x-2)<16 && (y-2)<16 && board.cells[x-1][y-1].getPos()!=0 && board.cells[x-2][y-2].getPos()==0 && checkLocation(x-2,y-2,color)==2) {
                endx = x-2;
                endy = y-2;
                type = "J";
            }

            else if((x)<16 && (y+1)<16 && (x)>=0 && (y+1)>=0 && ((board.cells[x][y+1].getPos()==0) && checkLocation(x,y+1,color)==2)) {
                endy = y+1;
                type = "E";
                endx = x;
            }
            else if ((y+2)<16 && x<16 && x>=0 && (y+2)>=0 && board.cells[x][y+1].getPos()!=0 && board.cells[x][y+2].getPos()==0  && checkLocation(x,y+2,color)==2) {
                endy = y+2;
                type = "J";
                endx = x;
            }
            else if((x)<16 && (y-1)<16 && (x)>=0 && (y-1)>=0 && ((board.cells[x][y-1].getPos()==0)  && checkLocation(x,y-1,color)==2)) {
                endy = y-1;
                type = "E";
                endx = x; 
            }
            else if((y-2)>=0 && x<16 && x>=0 && (y-2)<16 && board.cells[x][y-1].getPos()!=0 && board.cells[x][y-2].getPos()==0 && checkLocation(x,y-2,color)==2)  {
                endy = y-2;
                type = "J";
                endx = x;
            }
            else if((x+1)<16 && (y-1)<16 && (x+1)>=0 && (y-1)>=0 && ((board.cells[x+1][y-1].getPos()==0) && checkLocation(x+1,y-1,color)==2)) {
                endx = x+1;
                endy = y-1;
                type = "E";
            }
            else if((y-2)>=0 && (x+2)<16 && (y-2)<16 && (x+2)>=0 && board.cells[x+1][y-1].getPos()!=0 && board.cells[x+2][y-2].getPos()==0 && checkLocation(x+2,y-2,color)==2) {
                endy = y-2;
                endx = x+2;
                type = "J";
            }
            else if((x-1)<16 && (y+1)<16 && (x-1)>=0 && (y+1)>=0 && (board.cells[x-1][y+1].getPos()==0) && checkLocation(x-1,y+1,color)==2) {
                endx = x-1;
                endy = y+1;
                type = "E";
            }
            else if((y+2)<16 && (x-2)>=0 && (x-2)<16 && (y+2)>=0 && board.cells[x-1][y+1].getPos()!=0 && board.cells[x-2][y+2].getPos()==0 && checkLocation(x-2,y+2,color)==2) {
                endx = x-2;
                endy = y+2;
                type = "J";
            }
            else{}
            if(type!="F") {
                cells[x][y].setCell(x,y,0);
                cells[endx][endy].setCell(endx,endy,c);
                //System.out.println("found opposing camp to opposing camp");
                //System.out.print("for x as "+x+" and y as "+y+" i found end x as " + endx+ " and end y as "+endy);
                try{
                    Board.usingBufferedWritter(type,x,y,endx,endy); 
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
                return; 
            }   

        }
    }

    public void getSingleFurtherAway(Board board, int x, int y, int c, int baseEval, ArrayList<Move> moves,int dest) {
        int endx, endy;
        float neweval;
        //String color;
        char type;
        /*if(c==1) color="BLACK";
        else color="WHITE";*/
        int color = c;
                //System.out.println("INSIDE GET SINGLE FURTHER AWAY");


        if(c==1) {
            if ((x+2)<16 && (y+2)<16 &&  (x+1)>=0 && (y+1)>=0 && board.cells[x+2][y+2].getPos()==0 && board.cells[x+1][y+1].getPos()!=0 && checkLocation(x+2,y+2,color)==dest) {
                endx = x+2;
                endy = y+2;
                type = 'J';
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
            }
            if((x+1)<16 && (y+1)<16 && (x+1)>=0 && (y+1)>=0 && (board.cells[x+1][y+1].getPos()==0) && checkLocation(x+1,y+1,color)==dest) { 
                endx = x+1;
                endy = y+1;
                type = 'E';
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
            }
            if(y<16 && (x+2)<16 && (x+1)>=0 && y>=0 && board.cells[x+2][y].getPos()==0 && board.cells[x+1][y].getPos()!=0 && checkLocation(x+2,y,color)==dest) {
                endx = x+2;
                endy = y;
                type = 'J';
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
                
            }
            if ((y+2)<16 && x<16 && x>=0 && (y+2)>=0 && board.cells[x][y+2].getPos()==0 && board.cells[x][y+1].getPos()!=0 && checkLocation(x,y+2,color)==dest) {
                endy = y+2;
                type = 'J';
                endx = x;
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
            }
            if((x+1)<16 && (y)<16 && (x+1)>=0 && y>=0 && (board.cells[x+1][y].getPos()==0) && checkLocation(x+1,y,color)==dest) {
                endx = x+1;
                endy = y;
                type = 'E';
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
            }
            if((x)<16 && (y+1)<16 && (x)>=0 && (y+1)>=0 && (board.cells[x][y+1].getPos()==0) && checkLocation(x,y+1,color)==dest) {
                endy = y+1;
                type = 'E';
                endx = x;
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
            }  
        }
        else if(c==2){
            if((x-2)>=0 && (y-2)>=0 && (x-2)<16 && (y-2)<16 && board.cells[x-2][y-2].getPos()==0 && board.cells[x-1][y-1].getPos()!=0 && checkLocation(x-2,y-2,color)==dest) {
                endx = x-2;
                endy = y-2;
                type = 'J';
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
            }
            if((x-1)<16 && (y-1)<16 && (x-1)>=0 && (y-1)>=0 && (board.cells[x-1][y-1].getPos()==0) && checkLocation(x-1,y-1,color)==dest) {
                endx = x-1;
                endy = y-1;
                type = 'E';
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));

            }
            if((x-2)>=0 && y<16 && (x-2)>=0 && y>=0 && board.cells[x-2][y].getPos()==0 && board.cells[x-1][y].getPos()!=0 && checkLocation(x-2,y,color)==dest) {
                endx = x-2;
                type = 'J';
                endy = y;
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
            }
            if((y-2)>=0 && x<16 && x>=0 && (y-2)<16 && board.cells[x][y-2].getPos()==0 && board.cells[x][y-1].getPos()!=0 && checkLocation(x,y-2,color)==dest)  {
                endy = y-2;
                type = 'J';
                endx = x;
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
            }
            if((x-1)<16 && (y)<16 && (x-1)>=0 && y>=0 && (board.cells[x-1][y].getPos()==0) && checkLocation(x-1,y,color)==dest) {
                endx = x-1;
                type = 'E';
                endy = y;   
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
            }
            if((x)<16 && (y-1)<16 && (x)>=0 && (y-1)>=0 && (board.cells[x][y-1].getPos()==0) && checkLocation(x,y-1,color)==dest) {
                endy = y-1;
                type = 'E';
                endx = x; 
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,type));
            }
        }
    }

    public static void usingBufferedWritter(String type,int starty, int startx, int endy, int endx) throws IOException {
        
        String fileContent =type + " " + Integer.toString(startx) + "," + Integer.toString(starty) + " " + Integer.toString(endx) + "," + Integer.toString(endy);
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        writer.write(fileContent);
        writer.close();
    } 

    public static int checkLocation(int x, int y, int color) {
        int campLoc=0;
        if(color==2) { //white color
                if(((x==14 || x==15) && (y>=11 && y<=15)) || ((y==14 || y==15) && (x>=11 && x<=15)) || ((x==12 || x==13) && (y==13)) || ((y==13 || y==12) && (x==13)))
                    //inHomeCamp = 1;
                    campLoc = 1;
                else if(((x==0 || x==1) && (y>=0 && y<=4)) || ((y==0 || y==1) && (x>=0 && x<=4)) || ((x==2 || x==3) && (y==2)) || ((y==3 || y==2) && (x==2)))
                    //inOppCamp = 2;
                    campLoc = 2;
                
        }
        else { 
            //color==1 black
            if(((x==14 || x==15) && (y>=11 && y<=15)) || ((y==14 || y==15) && (x>=11 && x<=15)) || ((x==12 || x==13) && (y==13)) || ((y==13 || y==12) && (x==13)))
                //inOppCamp = 2;
                campLoc = 2;
            else if(((x==0 || x==1) && (y>=0 && y<=4)) || ((y==0 || y==1) && (x>=0 && x<=4)) || ((x==2 || x==3) && (y==2)) || ((y==3 || y==2) && (x==2)))
                campLoc = 1;
            
        }
        return campLoc;

    }

    public boolean isGoalState(Board board) {
        int x,y;
        int flagw=0, flagb=0, flageb=0,flagew=0;
        //for black color, our goal state is near white camp
        for(y=11;y<=15;y++) {
            if(board.cells[14][y].getPos()==0 || board.cells[15][y].getPos()==0)
                flageb=1;
            else if(board.cells[14][y].getPos()==1 || board.cells[15][y].getPos()==1) {
                flagb=1;
            }
        }
        if(flageb==0) {
            for(x=11;x<=15;x++) {
                if(board.cells[x][14].getPos()==0 || board.cells[x][15].getPos()==0)
                    flageb=1;
                else if(board.cells[x][14].getPos()==1 || board.cells[x][15].getPos()==1) {
                    flagb=1;
                }
            }
        }
        
        if(flageb==0) {
            if(board.cells[12][13].getPos()==0 || board.cells[13][12].getPos()==0 || board.cells[13][13].getPos()==0) flageb=1;
            else if(board.cells[12][13].getPos()==1 || board.cells[13][12].getPos()==1 || board.cells[13][13].getPos()==1) flagb=1;
        }
        

        if(flagb==1 && flageb==0) return true;

        //for white color, our goal state is near black camp
        for(y=0;y<=4;y++) {
            if(board.cells[0][y].getPos()==0 || board.cells[1][y].getPos()==0)
                flagew=1;
            else if(board.cells[0][y].getPos()==2 || board.cells[1][y].getPos()==2) {
                flagw=1;
            }
        }
        if(flagew==0) {
            for(x=0;x<=4;x++) {
                if(board.cells[x][0].getPos()==0 || board.cells[x][1].getPos()==0)
                    flagew=1;
                else if(board.cells[x][0].getPos()==2 || board.cells[x][1].getPos()==2) {
                    flagw=1;
                }
            }
        }
        
        if(flagew==0) {
            if(board.cells[2][3].getPos()==0 || board.cells[3][2].getPos()==0 || board.cells[2][2].getPos()==0) flagew=1;
            else if(board.cells[2][3].getPos()==2 || board.cells[3][2].getPos()==2 || board.cells[2][2].getPos()==2) flagw=1;
        }
        
        if(flagw==1 && flagew==0) return true;

        return false;
    }

    public void getSingle(Board board, int x, int y, int c, int baseEval, ArrayList<Move> moves,int dest, int evalFlag) {
        int endx, endy;
        float neweval;
        
        int color = c;
        if(c==1) {
            if((x+1)<16 && (y+1)<16 && (x+1)>=0 && (y+1)>=0 && ((board.cells[x+1][y+1].getPos()==0) && checkLocation(x+1,y+1,color)==dest)) { 
                endx = x+1;
                endy = y+1;
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));  
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
            }
            if((x+1)<16 && (y)<16 && (x+1)>=0 && y>=0 && ((board.cells[x+1][y].getPos()==0)) && checkLocation(x+1,y,color)==dest) {
                endx = x+1;
                endy = y;    
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
            }
            if((x)<16 && (y+1)<16 && (x)>=0 && (y+1)>=0 && ((board.cells[x][y+1].getPos()==0) && checkLocation(x,y+1,color)==dest)) {
                endy = y+1;
                endx = x;
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
            }
            if((x+1)<16 && (y-1)<16 && (x+1)>=0 && (y-1)>=0 && ((board.cells[x+1][y-1].getPos()==0) && checkLocation(x+1,y-1,color)==dest)) {
                endx = x+1;
                endy = y-1;
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
            }
            if((x-1)<16 && (y+1)<16 && (x-1)>=0 && (y+1)>=0 && (board.cells[x-1][y+1].getPos()==0) && checkLocation(x-1,y+1,color)==dest) {
                endx = x-1;
                endy = y+1;
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));

            }  
            if((x-1)<16 && (y)<16 && (x-1)>=0 && y>=0 && ((board.cells[x-1][y].getPos()==0) && checkLocation(x-1,y,color)==dest)) {
                endx = x-1;
                endy = y; 
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E')); 
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
            }
            if((x)<16 && (y-1)<16 && (x)>=0 && (y-1)>=0 && ((board.cells[x][y-1].getPos()==0)  && checkLocation(x,y-1,color)==dest)) {
                endy = y-1;
                endx = x; 
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
            }
            if((x-1)<16 && (y-1)<16 && (x-1)>=0 && (y-1)>=0 && (board.cells[x-1][y-1].getPos()==0) && checkLocation(x-1,y-1,color)==dest) {
                endx = x-1;
                endy = y-1;
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
            }
        }
        else {
            if((x-1)<16 && (y-1)<16 && (x-1)>=0 && (y-1)>=0 && (board.cells[x-1][y-1].getPos()==0) && checkLocation(x-1,y-1,color)==dest) {
                endx = x-1;
                endy = y-1;
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
            }
            if((x-1)<16 && (y)<16 && (x-1)>=0 && y>=0 && ((board.cells[x-1][y].getPos()==0) && checkLocation(x-1,y,color)==dest)) {
                endx = x-1;
                endy = y; 
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E')); 
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
            }
            if((x)<16 && (y-1)<16 && (x)>=0 && (y-1)>=0 && ((board.cells[x][y-1].getPos()==0)  && checkLocation(x,y-1,color)==dest)) {
                endy = y-1;
                endx = x; 
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
            }
            if((x+1)<16 && (y-1)<16 && (x+1)>=0 && (y-1)>=0 && ((board.cells[x+1][y-1].getPos()==0) && checkLocation(x+1,y-1,color)==dest)) {
                endx = x+1;
                endy = y-1;
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
            }
            if((x-1)<16 && (y+1)<16 && (x-1)>=0 && (y+1)>=0 && (board.cells[x-1][y+1].getPos()==0) && checkLocation(x-1,y+1,color)==dest) {
                endx = x-1;
                endy = y+1;
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
            }  
            if((x+1)<16 && (y)<16 && (x+1)>=0 && y>=0 && ((board.cells[x+1][y].getPos()==0)) && checkLocation(x+1,y,color)==dest) {
                endx = x+1;
                endy = y;    
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
            }
            if((x)<16 && (y+1)<16 && (x)>=0 && (y+1)>=0 && ((board.cells[x][y+1].getPos()==0) && checkLocation(x,y+1,color)==dest)) {
                endy = y+1;
                endx = x;
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
            }
            if((x+1)<16 && (y+1)<16 && (x+1)>=0 && (y+1)>=0 && ((board.cells[x+1][y+1].getPos()==0) && checkLocation(x+1,y+1,color)==dest)) { 
                endx = x+1;
                endy = y+1;
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                path.add(new Coord(endx,endy));
                moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));  
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'E'));
            }
        }
    }

    public  void getJump(Board board, int x, int y, int c, float baseEval, int[][] visited, ArrayList<Move> moves, ArrayList<Coord> path, int dest, int evalFlag) {
        int endx=0,endy=0,startx,starty;
        int color = c;
        float neweval=0;
        startx = path.get(0).getX();
        starty = path.get(0).getY();
        
        //FOR BLACK TAKE IN THIS ORDER
        if(c==1) {
            if ((x+2)<16 && (y+2)<16 &&  (x+1)>=0 && (y+1)>=0 && board.cells[x+1][y+1].getPos()!=0 && board.cells[x+2][y+2].getPos()==0 && visited[x+2][y+2]==0) {
                endx = x+2;
                endy = y+2;
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);    
            }
            if ((y+2)<16 && x<16 && x>=0 && (y+2)>=0 && board.cells[x][y+1].getPos()!=0 && board.cells[x][y+2].getPos()==0  && visited[x][y+2]==0) {
                endy = y+2;
                endx = x;
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);
            }
            if(y<16 && (x+2)<16 && (x+2)>=0 && y>=0 && board.cells[x+1][y].getPos()!=0 && board.cells[x+2][y].getPos()==0 && visited[x+2][y]==0) {
                endx = x+2;
                endy = y; 
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);  
            }  
            if((y-2)>=0 && (x+2)<16 && (y-2)<16 && (x+2)>=0 && board.cells[x+1][y-1].getPos()!=0 && board.cells[x+2][y-2].getPos()==0 && visited[x+2][y-2]==0) {
                endy = y-2;
                endx = x+2;
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)    
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);
            }
            if((y+2)<16 && (x-2)>=0 && (x-2)<16 && (y+2)>=0 && board.cells[x-1][y+1].getPos()!=0 && board.cells[x-2][y+2].getPos()==0  && visited[x-2][y+2]==0) {
                endx = x-2;
                endy = y+2;                
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);
            }
            if((x-2)>=0 && y<16 && (x-2)<16 && y>=0 && board.cells[x-1][y].getPos()!=0 && board.cells[x-2][y].getPos()==0 &&  visited[x-2][y]==0) {
                endx = x-2;
                endy = y;                
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);
            } 
            if((y-2)>=0 && x<16 && x>=0 && (y-2)<16 && board.cells[x][y-1].getPos()!=0 && board.cells[x][y-2].getPos()==0 && visited[x][y-2]==0)  {
                endy = y-2;
                endx = x;
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);
            }
            if((x-2)>=0 && (y-2)>=0 && (x-2)<16 && (y-2)<16 && board.cells[x-1][y-1].getPos()!=0 && board.cells[x-2][y-2].getPos()==0 && visited[x-2][y-2]==0) {
                endx = x-2;
                endy = y-2;         
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);
            }
        }

        else{
            if((x-2)>=0 && (y-2)>=0 && (x-2)<16 && (y-2)<16 && board.cells[x-1][y-1].getPos()!=0 && board.cells[x-2][y-2].getPos()==0 && visited[x-2][y-2]==0) {
                endx = x-2;
                endy = y-2;         
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);
            }
            if((x-2)>=0 && y<16 && (x-2)<16 && y>=0 && board.cells[x-1][y].getPos()!=0 && board.cells[x-2][y].getPos()==0 &&  visited[x-2][y]==0) {
                endx = x-2;
                endy = y;                
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);
            } 
            if((y-2)>=0 && x<16 && x>=0 && (y-2)<16 && board.cells[x][y-1].getPos()!=0 && board.cells[x][y-2].getPos()==0 && visited[x][y-2]==0)  {
                endy = y-2;
                endx = x;
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);
            }
            if((y-2)>=0 && (x+2)<16 && (y-2)<16 && (x+2)>=0 && board.cells[x+1][y-1].getPos()!=0 && board.cells[x+2][y-2].getPos()==0 && visited[x+2][y-2]==0) {
                endy = y-2;
                endx = x+2;
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)    
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);
            }
            if((y+2)<16 && (x-2)>=0 && (x-2)<16 && (y+2)>=0 && board.cells[x-1][y+1].getPos()!=0 && board.cells[x-2][y+2].getPos()==0  && visited[x-2][y+2]==0) {
                endx = x-2;
                endy = y+2;                
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);
            }
            if ((y+2)<16 && x<16 && x>=0 && (y+2)>=0 && board.cells[x][y+1].getPos()!=0 && board.cells[x][y+2].getPos()==0  && visited[x][y+2]==0) {
                endy = y+2;
                endx = x;
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);
            }
            if(y<16 && (x+2)<16 && (x+2)>=0 && y>=0 && board.cells[x+1][y].getPos()!=0 && board.cells[x+2][y].getPos()==0 && visited[x+2][y]==0) {
                endx = x+2;
                endy = y; 
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);  
            }  
            if ((x+2)<16 && (y+2)<16 &&  (x+1)>=0 && (y+1)>=0 && board.cells[x+1][y+1].getPos()!=0 && board.cells[x+2][y+2].getPos()==0 && visited[x+2][y+2]==0) {
                endx = x+2;
                endy = y+2;
                visited[endx][endy]=1;
                path.add(new Coord(endx,endy));
                if(checkLocation(endx,endy,c)==dest)
                    moves.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                constants.PARENTS.add(new Move(new Coord(x,y), new Coord(endx,endy), 0, path,'J'));
                getJump(board,endx,endy,c,0,visited,moves,path,dest,evalFlag);    
            }
        }
        
    }

    
    public float evaluate(Board board) {
        float score=0;
        int val;

        if(constants.MAX_PLAYER==1) val=15;
        else val=0;
        
        for(int i=0;i<16;i++) {
            for(int j=0;j<16;j++) {
                if(board.cells[i][j].getPos()==constants.MAX_PLAYER) {
                    score = score + (float)Math.sqrt((val-i)*(val-i) + (val-j)*(val-j));
                }   
            }   
        }
        score = score*-1;
        return score;
    }

    public float evaluateLast(Board board,int col) {
        float score=0;
        ArrayList<Coord> empty = new ArrayList<Coord>();
        ArrayList<Coord> outside = new ArrayList<Coord>();

        int flagw=0, flagb=0,i,j;
        if(constants.MAX_PLAYER==1) {
            //for black color, our goal state is near white camp
            for(j=11;j<=15;j++) {
                if(board.cells[14][j].getPos()==0) {
                    empty.add(new Coord(14,j));
                }
                if(board.cells[15][j].getPos()==0) {
                    empty.add(new Coord(15,j));
                }
            }
            for(i=11;i<=13;i++) {
                if(board.cells[i][14].getPos()==0) {
                    empty.add(new Coord(i,14));
                } 
                if(board.cells[i][15].getPos()==0) {
                    empty.add(new Coord(i,15));
                }
            }
            if(board.cells[12][13].getPos()==0) empty.add(new Coord(12,13));
            if(board.cells[13][12].getPos()==0) empty.add(new Coord(13,12));
            if(board.cells[13][13].getPos()==0) empty.add(new Coord(13,13));
        }
        
        else{
            //for white color, our goal state is near black camp
            for(j=0;j<=4;j++) {
                if(board.cells[0][j].getPos()==0) {
                    empty.add(new Coord(0,j));
                }
                if(board.cells[1][j].getPos()==0) {
                    empty.add(new Coord(1,j));
                }
            }
            for(i=2;i<=4;i++) {
                if(board.cells[i][0].getPos()==0) {
                    empty.add(new Coord(i,0));
                } 
                if(board.cells[i][1].getPos()==0) {
                    empty.add(new Coord(i,1));
                }
            }
            if(board.cells[2][3].getPos()==0) empty.add(new Coord(2,3));
            if(board.cells[3][2].getPos()==0) empty.add(new Coord(3,2));
            if(board.cells[2][2].getPos()==0) empty.add(new Coord(2,2));
        }

        for(i=0;i<16;i++) {
            for(j=0;j<16;j++) {
                if((board.cells[i][j].getPos()==constants.MAX_PLAYER) && checkLocation(i,j,constants.MAX_PLAYER)==0) {
                    outside.add(new Coord(i,j));
                }
            }
        }

        
        for(i=0;i<outside.size();i++) {
            for(j=0;j<empty.size();j++){
                score+= (float)Math.sqrt(((empty.get(j).getX() - outside.get(i).getX())*(empty.get(j).getX() - outside.get(i).getX()))+((empty.get(j).getY() - outside.get(i).getY())*(empty.get(j).getY() - outside.get(i).getY())));
            }
            
        }
        score = score*-1;
        return score;
     
    }

    public ArrayList<Move> generateTree(Board board,int color, gridCell[] positions) {
        //color - 0 for empty, 1 for black, 2 for white
        //generate tree till given depth

        int evalFlag = 0;
        //0 stands for euclidean
        //1 stands for misplaces tiles

        gridCell[] reserveOutCamp = new gridCell[19];
        gridCell[] reserveInCamp = new gridCell[19];
        gridCell[] reserveOppCamp = new gridCell[19];

        ArrayList<Move> moves = new ArrayList<Move>();

        int campLoc,campOut=0,countIn=0,countOpp=0,countOut=0;
        int x,y,endx,endy,startx, starty,c;
        int baseEval=0;
        int countMovesAfter=0;
        int countMovesBefore=0;

        for(int i=0;i<19;i++) {
            //System.out.println("inside first for loop");
            x = positions[i].getX();
            y = positions[i].getY();
            c = positions[i].getPos();

            campLoc = checkLocation(x,y,color);

            if(campLoc==0) {
                //outside camp, keep for reserve
                reserveOutCamp[countOut] = new gridCell(x,y,c);
                countOut++;
                continue;
            }

            // if in opposing camp, then it can move wherever it wants, INSIDE THE CAMP ONLY
            if(campLoc ==2) {
                reserveOppCamp[countOpp] = new gridCell(x,y,c);
                countOpp++;
                continue;
            }
            
            reserveInCamp[countIn] = new gridCell(x,y,c);
            countIn++;

            int[][] visited = new int[16][16];


            ArrayList<Coord> path = new ArrayList<Coord>();
            path.add(new Coord(x,y));
            int dest = 0;
            //i.e it should go outside camp
            getJump(board,x,y,c,baseEval,visited,moves,path,dest,evalFlag);
            getSingle(board, x, y, c, baseEval,moves,dest,evalFlag);         

        }



        if(countOpp>16) {
            evalFlag=1;
        }

        if(moves.isEmpty()) {
            //check if no move from inside camp to outside camp
            for(int i=0;i<countIn;i++) {
                //System.out.println("inside second for loop");
                x = reserveInCamp[i].getX();
                y = reserveInCamp[i].getY();
                c = reserveInCamp[i].getPos();
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                int dest = 1;
                getSingleFurtherAway(board, x, y, c, baseEval, moves,dest);
            }
        }

        if(moves.isEmpty()) {
                for(int i=0;i<countOut;i++) {
                //System.out.println("inside opposing camp for loop");
                x = reserveOutCamp[i].getX();
                y = reserveOutCamp[i].getY();
                c = reserveOutCamp[i].getPos();


                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                int dest=2;

                int[][] visited = new int[16][16];
                getJump(board,x,y,c,baseEval,visited,moves,path,dest,evalFlag);
                getSingle(board, x, y, c, baseEval, moves,dest,evalFlag);

                dest = 0;
                path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                visited = new int[16][16];
                getJump(board,x,y,c,baseEval,visited,moves,path,dest,evalFlag);
                getSingle(board, x, y, c, baseEval, moves,dest,evalFlag);
                
                }
            
            
            for(int i=0;i<countOpp;i++) {
                //System.out.println("inside opposing camp for loop");
                x = reserveOppCamp[i].getX();
                y = reserveOppCamp[i].getY();
                c = reserveOppCamp[i].getPos();

                int dest = 2;
                ArrayList<Coord> path = new ArrayList<Coord>();
                path.add(new Coord(x,y));
                int[][] visited = new int[16][16];
                getJump(board,x,y,c,baseEval,visited,moves,path,dest,evalFlag);
                getSingle(board, x, y, c, baseEval, moves,dest,evalFlag);
                
            }
        }

        float eval;
        for(int i=0;i<moves.size();i++) {

            //for each move, get startx, starty, endx, endy
            //send to new func eval
            //set eval to that value and return.

            endx = moves.get(i).getTo().getX();
            endy = moves.get(i).getTo().getY();

            startx = moves.get(i).getPar().get(0).getX();
            starty = moves.get(i).getPar().get(0).getY();
            
            board.cells[startx][starty].setCell(startx,starty,0);
            board.cells[endx][endy].setCell(endx,endy,color);

            if(evalFlag==0)
                eval = evaluate(board);
            else {
                eval = evaluateLast(board,color);
            }

            moves.get(i).seteval(eval);
            board.cells[startx][starty].setCell(startx,starty,color);
            board.cells[endx][endy].setCell(endx,endy,0);

        }
        
        return moves;
    }

    
    
}


public class homework {

    //public static ArrayList<Move> PARENTS = new ArrayList<Move>();
    public static long startTime;
    public static long endTime;
    public static long givenTime;

	public static void main(String args[]) throws IOException{
		startTime = System.nanoTime();

		Board board = new Board();
		gridCell[] positionsHome = new gridCell[19];
        gridCell[] positionsOpp = new gridCell[19];

		
		String mode;
		String color;
		String time;
		int type=0;
		File file = new File("input.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String readLine = "";
        //System.out.println("Reading file using Buffered Reader");
        mode  = bufferedReader.readLine();
        color = bufferedReader.readLine();
        time = bufferedReader.readLine();
        
        char temp[];
        
        int count=0;
        int countOpp=0;
        for(int i=0;i<16;i++) {
        	readLine = bufferedReader.readLine();
        	temp = readLine.toCharArray();
        	for(int j=0;j<16;j++) {
        		if(temp[j]=='.') type=0;
        		else if(temp[j]=='B') {
        			type=1;
        			if(color.equals("BLACK")) {
        				positionsHome[count] = new gridCell(i,j,1);
        				count++;
        			}
                    else {
                        positionsOpp[countOpp] = new gridCell(i,j,1);
                        countOpp++;
                    }
        		}
        		else if(temp[j]=='W'){
        			type=2;
        			if(color.equals("WHITE")) {
        				positionsHome[count] = new gridCell(i,j,2);
        				count++;
        			}
                    else {
                        positionsOpp[countOpp] = new gridCell(i,j,2);
                        countOpp++;
                    }
        		}

        		board.setCellBoard(i,j,type);
        	}
        }
        

        int col;
        if(mode.equals("SINGLE")) {
            if(color.equals("BLACK")) col=1;
            else col=2;
            

            int[][] visited = new int[19][19];
            ArrayList<Move> moves = new ArrayList<Move>();
            ArrayList<Coord> path = new ArrayList<Coord>();
            int dest = 0;
            board.validMoves(board,positionsHome,col);
            

        }
        else {
            //mode = GAME
            if(color.equals("BLACK")) {
                constants.MAX_PLAYER = 1;
                type=1;
            }
            else {
                type=2;
                int tempx,tempy;
                constants.MAX_PLAYER=2;
                Collections.reverse(Arrays.asList(positionsHome));
                
            } 
            ArrayList<Move> moves = new ArrayList<Move>();

            Move move = MaxValue(null,board, Integer.MIN_VALUE, Integer.MAX_VALUE,constants.MAX_DEPTH, type,positionsHome,positionsOpp);
            
            
            homework.findAndPrint(move,constants.PARENTS);

        }
        

        long endTime = System.nanoTime();
		long duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.
		System.out.println("time = " + duration);
        
	}

    
    public static void findAndPrint(Move move, ArrayList<Move> PARENTS) {
        int i,m,temp=-1,inter,j,q,l,ih;

        if(move.type=='E') {
            //DO SIMPLE PRINTING
            int startx,starty,endy,endx;
            startx = move.getFrom().getY();
            starty = move.getFrom().getX();
            endx = move.getTo().getY();
            endy = move.getTo().getX();
            try{
                String fileContent ="E" + " " + Integer.toString(startx) + "," + Integer.toString(starty) + " " + Integer.toString(endx) + "," + Integer.toString(endy);
                System.out.println(fileContent);
                BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
                writer.write(fileContent);
                writer.close();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        
            return;
        }



        for(ih=0;ih<=constants.PARENTS.size()-1;ih++) {
            if(move.getFrom().getX()==constants.PARENTS.get(ih).getFrom().getX() && move.getFrom().getY()==constants.PARENTS.get(ih).getFrom().getY() && move.getTo().getX()==constants.PARENTS.get(ih).getTo().getX() && move.getTo().getY()==constants.PARENTS.get(ih).getTo().getY()) {
                    temp = ih;
                    break;
                }
        }

        

        ArrayList<Coord> actPath = new ArrayList<Coord>();
        
            actPath.add(new Coord(move.getTo().getX(),move.getTo().getY()));
            int findx = move.getFrom().getX();
            int findy = move.getFrom().getY();
            int flag=0;
            //System.out.println("temp ka value is " + temp);
            for(m=temp-1;m>=0;m--) {
                //start looking for next element in path
                if(findx==constants.PARENTS.get(m).getTo().getX() && findy==constants.PARENTS.get(m).getTo().getY()) {
                    flag=1;
                    actPath.add(new Coord(findx,findy));
                    findx = constants.PARENTS.get(m).getFrom().getX();
                    findy = constants.PARENTS.get(m).getFrom().getY();
                }
            }
            actPath.add(new Coord(findx,findy));
        //}

        

        try{
            //Board.usingBufferedWritter("J",x,y,endx,endy); 
            int from1, from2, to1, to2;                   
            String fileContent;
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            for(int k=actPath.size()-1;k>1;k--){
                from1 = actPath.get(k).getY();
                from2 = actPath.get(k).getX();
                to1 = actPath.get(k-1).getY();
                to2 = actPath.get(k-1).getX();
                fileContent ="J" + " " + Integer.toString(from1) + "," + Integer.toString(from2) + " " + Integer.toString(to1) + "," + Integer.toString(to2) + "\n";
                System.out.println(fileContent);
                writer.write(fileContent);
            }
            from1 = actPath.get(1).getY();
            from2 = actPath.get(1).getX();
            to1 = actPath.get(0).getY();
            to2 = actPath.get(0).getX();
            fileContent ="J" + " " + Integer.toString(from1) + "," + Integer.toString(from2) + " " + Integer.toString(to1) + "," + Integer.toString(to2);
            System.out.println(fileContent);
            writer.write(fileContent);
            writer.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return;
    }

    

    public static Move MaxValue(Move bestMove, Board board, float alpha, float beta, int dep, int col, gridCell[] positionsHome, gridCell[] positionsOpp) {
        float v = -2147483648;
        int oppcol;

        //checkTerminal
        if(board.isGoalState(board)) {
            System.out.println("IN GOAL");
            return bestMove;
        }
        if(dep==0)
            return bestMove;

        Move move=null;
        ArrayList<Move> moves = new ArrayList<Move>();
        
        int flag;
        
        moves = board.generateTree(board,col,positionsHome);
        if(col==1) oppcol=2;
        else oppcol=1;
        //generated moves best eval wise

        int x,y,endx,endy,startx,starty,n,positionTempx=-1;

        if(moves.isEmpty()) {
            return bestMove;
        }

        for(int i=0;i<moves.size();i++) {
            //for each move
            //generate next state
            //System.out.println("in the for loop");
            x = moves.get(i).getFrom().getX();
            y = moves.get(i).getFrom().getY();
            endx = moves.get(i).getTo().getX();
            endy = moves.get(i).getTo().getY();

            startx = moves.get(i).getPar().get(0).getX();
            starty = moves.get(i).getPar().get(0).getY();

            board.cells[startx][starty].setCell(startx,starty,0);
            board.cells[endx][endy].setCell(endx,endy,col);

            for(n=0;n<19;n++) {

                if(positionsHome[n].getX()==startx && positionsHome[n].getY()==starty) {
                    positionsHome[n].setCell(endx,endy,col);
                    positionTempx = n;
                    break;
                }
                
            }
            

            move = MinValue(moves.get(i), board, alpha,beta,dep-1,oppcol,positionsHome,positionsOpp);


            board.cells[startx][starty].setCell(startx,starty,col);
            board.cells[endx][endy].setCell(endx,endy,0);

            positionTempx=-1;
            for(n=0;n<19;n++) {
                if(positionsHome[n].getX()==endx && positionsHome[n].getY()==endy) {
                    positionsHome[n].setCell(startx,starty,col);
                    positionTempx = n;
                    break;
                }
                
            }

            
            if(v<=move.geteval()) {
                v = move.geteval();
                bestMove = moves.get(i);
            }
            if(v>=beta) {
                return bestMove;
            }
            alpha = Math.max(alpha,v);
        }

        
        return bestMove;
    }

    public static Move MinValue(Move bestMove, Board board, float alpha, float beta, int dep, int col, gridCell[] positionsHome, gridCell[] positionsOpp) {
        float v = 2147483647;
        int oppcol;
        //checkTerminal
        if(board.isGoalState(board)) {
            System.out.println("IN GOAL");
            return bestMove;
        }

        //checkDepth
        if(dep==0)
            return bestMove;

        Move move = null;
        ArrayList<Move> moves = new ArrayList<Move>();
        moves = board.generateTree(board,col,positionsOpp);
        
        if(moves.isEmpty()) {
            return bestMove;
        }

        if(col==1) oppcol=2;
        else oppcol=1;

        int startx,starty;
        int x,y,endx,endy;
        int positionTempx,n;

        for(int i=0;i<moves.size();i++) {
            //for each move


            //generate next state
            x = moves.get(i).getFrom().getX();
            y = moves.get(i).getFrom().getY();
            endx = moves.get(i).getTo().getX();
            endy = moves.get(i).getTo().getY();

            startx = moves.get(i).getPar().get(0).getX();
            starty = moves.get(i).getPar().get(0).getY();
            
            board.cells[startx][starty].setCell(startx,starty,0);
            board.cells[endx][endy].setCell(endx,endy,col);

            positionTempx=-1;
            for(n=0;n<19;n++) {
                if(positionsOpp[n].getX()==startx && positionsOpp[n].getY()==starty) {
                    positionsOpp[n].setCell(endx,endy,col);
                    positionTempx = n;
                    break;
                }
                
            }

            move = MaxValue(moves.get(i), board,alpha,beta,dep-1,oppcol,positionsHome,positionsOpp);

            board.cells[startx][starty].setCell(startx,starty,col);
            board.cells[endx][endy].setCell(endx,endy,0);


            positionTempx=-1;
            for(n=0;n<19;n++) {
                if(positionsOpp[n].getX()==endx && positionsOpp[n].getY()==endy) {
                    positionsOpp[n].setCell(startx,starty,col);
                    positionTempx = n;
                    break;
                }
                
            }

            if(v>=move.geteval()) {
                v = move.geteval();
                bestMove = moves.get(i);
            }
            if(v<=alpha) {
                return bestMove;
            }
            beta = Math.min(beta,v);
        }
        
        return bestMove;
    }

}