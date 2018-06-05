import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
/**
 * @author brandt
 *
 * ΑΕΜ: 2515
 * Όνομα: ΙΩΑΝΝΗΣ-ΚΡΙΣΤΟΦ
 * Επίθετο: ΜΠΡΑΝΤ-ΙΩΑΝΝΙΔΗΣ
 *
 */

public class Main {

    /**
     * @author brandt
     *
     * This function reads a set of points from a given .txt file and stores them in an ArrayList.
     * Due to the needs of the project, the ArrayList is then ordered in ascending order based on the x values of the
     * points.
     * This function has a complexity of O(nlogn).
     * 1) Reading the data from .txt file and adding them in ArrayList: O(n) complexity.
     * 2) Sorting the above mentioned ArrayList: O(nlogn) complexity.
     *
     * @param args: the input file which contains the set of points that are going to be used.
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        if (args.length == 0){
            System.out.println("You did not specify an input.txt file. The command you should be using to run " +
                    "this program is: java -jar 2515Algorithms1.txt path-to-txt-file/yourInputFile.txt");
        }
        else{
            String fileName = args[0];
            //the ArrayList where the points are stored.
            ArrayList<Point> pointList = new ArrayList<>();
            int size=0;

            // read the file -based on the given structure- and save every point into the ArrayList points.
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileName));

                //file flag is a "flag" variable used to read the file in different format for the first line, and another
                //one for the rest of the file.
                int fileFlag=0,i=0,j=0;
                String currentLine=null;

                while((currentLine = br.readLine())!= null){
                    if (fileFlag == 0){
                        size = Integer.valueOf(currentLine);
                        fileFlag++;
                    }
                    else{
                        String[] formatted = new String[2];

                        //some columns are separated by TAB,while some others by SPACE.
                        formatted = currentLine.split("\\s+");
                        int x = Integer.valueOf(formatted[0]);
                        int y = Integer.valueOf(formatted[1]);
                        Point newPoint = new Point(x,y);
                        pointList.add(newPoint);
                    }
                }
            } catch (NumberFormatException | IOException e) {
                // TODO Auto-generated catch block
                System.out.println("Couldn't read file.Either the file format is different from what expected or" +
                        "the file have not be found.Try again.");
                e.printStackTrace();
            }


            //sorting the points depending on their x value in ascending order.
            Collections.sort(pointList);

            //the algorithm starts to process the points.
            pointList = divide(pointList);
            System.out.println("Final skyline: ");
            printPoints(pointList);
            System.out.println();
        }

    }

    /**
     * @author brandt
     *
     * This function is the divide part of the algorithm as defined in the projects description.
     * It divides the ArrayList on left and right sub-arrays recursively until there is ,the most,1 or 2 elements in
     * each sub-array.Then the calculation of the final skyline is trivial.
     *
     * Analysis of the algorithm's complexity:
     * The copying of the elements into leftSubArray and rightSubArray, has a complexity of O(n)+O(n)=O(n).
     * The entire input must be iterated,and this takes place O(log(n)) times -the input can only be halved
     * O(log(n)) times. So: n items, iterated log(n) times => O(nlogn) complexity.
     *
     * The complexity of the merge function is trivial.Merge is called only when the divide function has met the end
     * condition,which means that it's called several times and for a very small dataset.The project description also
     * states that for the given algorithm and the way it works, the calculation of the skyline inside merge function
     * is trivial.
     *
     * @param points: a set of points which has it's x values ordered in ascending order.
     * @return the final skyline of the given set of points, as defined in the project description.
     */
    public static ArrayList<Point> divide(ArrayList<Point> points){

        if (points.size()<=1){
            return points;
        }

        //The sets S1 and S2 which are defined in the project desciption.
        ArrayList<Point> leftSkyline=new ArrayList<>();
        ArrayList<Point> rightSkyline=new ArrayList<>();

        //leftPoints: the set of points in left sub-arrays that are going to be processed.
        //rightPoints: the set of points in right sub-arrays that are going to be processed.
        ArrayList<Point> leftSubArray=new ArrayList<>();
        ArrayList<Point> rightSubArray=new ArrayList<>();

        //Copy left sub-array points O(n) complexity.
        for (int i=0;i<points.size()/2;i++){
            leftSubArray.add(points.get(i));
        }

        //Copy right sub-array points O(n) complexity.
        for (int i=points.size()/2;i<points.size();i++){
            rightSubArray.add(points.get(i));
        }

        leftSkyline=divide(leftSubArray);
        rightSkyline=divide(rightSubArray);

        leftSkyline=merge(leftSkyline,rightSkyline);

        return leftSkyline;
    }


    /**
     * @author brandt
     *
     * This function merges the sub-arrays by deleting the points that are being conquered from the left sub-arrays and
     * the right sub-arrays respectively.
     * Due to the ascending order of the x values of the points ,it is a fact that points from left sub-arrays can not
     * be conquered from points that belong to right sub-arrays.However, points from right sub-arrays will not be
     * conquered from points in left subarrays only if they have smaller Y values.The implementation is given below.
     *
     * Another issue is that points on left sub-arrays with same X values but different Y values need to be compared.
     * The ones with greater Y values will be removed from the final skyline.
     *
     * @param leftSkyline: the S1 set as defined in the project description.
     * @param rightSkyline: the S2 set as defined in the project description.
     */
    public static ArrayList<Point> merge(ArrayList<Point> leftSkyline,ArrayList<Point> rightSkyline){

        //Finds the minimum Y value inside the left sub-array which is used later to determine which points from the
        //right sub-array are going to be included in the skyline.
        int minY=Integer.MAX_VALUE;
        for (int i=0;i<leftSkyline.size();i++){
            if (leftSkyline.get(i).getY()<minY){
                minY=leftSkyline.get(i).getY();
            }
        }


        //Comparing and removing points with same X values and different Y.
        int leftSize = leftSkyline.size();
        for(int i=1;i<leftSize;i++){
            if (leftSkyline.get(i-1).getX() == leftSkyline.get(i).getX()){
                if (leftSkyline.get(i-1).getY() > leftSkyline.get(i).getY()){
                    leftSkyline.remove(i-1);
                }
                else {
                    leftSkyline.remove(i);
                }
                leftSize--;
                i--;
            }
        }


        //Based on the initial sorting, points on right sub-arrays have greater x values than the points on left sub-arrays.
        //Thus,a point B(x2,y2) on a right sub-array will be conquered from a point A(x1,y1) on a left sub-array if y1<y2.
        //The iteration is done in reversed order so that we don't miss any points due to the shifting of the ArrayList indexes
        //when a point is deleted.
        for(int i=rightSkyline.size()-1;i>=0;i--){
            if (rightSkyline.get(i).getY()>=minY){
                rightSkyline.remove(i);
            }
        }


        //Add points from rightSkyline-S2 which are not conquered by the points on leftSkyline-S1 to the final result.
        leftSkyline.addAll(rightSkyline);
        return leftSkyline;
    }

    //A simple method which prints the points of a given ArrayList.
    public static void printPoints(ArrayList<Point> points){
        for(int i=0;i<points.size();i++){
            System.out.print("(" + points.get(i).getX() + "," + points.get(i).getY() +"),");
        }
    }

}

/**
 * @author brandt
 * A simple class which describes the behaviour of a 2D point.
 *
 */
class Point implements Comparable<Point>{
    private int x,y;

    public Point(int x,int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setX(int x){
        this.x= x;
    }

    public void setY(int y){
        this.y=y;
    }

    public int compareTo(Point o1){
        int x = o1.getX();
        return this.x-x; //ascending order
    }

}

