/**
 * 
 * File : SearchTree.java
 * @author : Alec Prassinos
 * @version : 10-24-05
 * 
 */

import java.util.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import static java.lang.Math.pow;

public class SearchTree
{
    private Node root;

    private int[] depthCount;
        
    private long initTime;
    private long finalTime;

    public SearchTree(){}

    public SearchTree(Puzzle rootState)
    {   
        initTime = 0;
        finalTime = 0;
        root = new Node(rootState, 0);
        depthCount = new int[45];
        for (int i = 0; i < 45; i++){
            depthCount[i] = 0;
		}
    }

    public void AlgorithmA(int hFn)
    {   
        root.removeAllChildren();
        System.out.println("Algorithm A Search : h" + hFn + "(n)");
        initTime();
        
        Vector <Node> open = new Vector<Node>();
        open.add(root);
        Node choice = null;
        int n = 0;
        root.print();
        while (open.size() > 0){
			
            choice = SearchLowestF(open, hFn);        
            open.remove(choice);

            if (Node.isGoal(choice)){
				choice.print();
                //printPath(choice);
                break;
            }
            
            Vector <Node> children = expandA(choice, hFn);  
            
            open.addAll(children);      
            n = choice.getDepth();
                          
            if (n > 15){
               break;
		   }
        }
        double q = finalTimeStamp();
        int d = countNodes();
		double speed = d/q;
        System.out.println("num of iterations : " + n);
        System.out.println("Total Nodes generated : " + d + "\n\n");
        System.out.println("Speed (Nodes/sec) :" + speed);
		return;
    }
   
    
    
    public void IDDF(){
		
        initTime();
       
        root.removeAllChildren();
        Node result = null;
     
        for (int i = 0; i < 25; i++)
        {
			root.removeAllChildren();
			result = DepthLimited(i);
			
			if (result != null){
				result.print();
				//printPath(result);
				break; 
			}
		}        
       
	
        double n = finalTimeStamp();
        System.out.println("Number of nodes generated at each depth limit");
        int d = 0;
        for (int j = 0; j < 35; j++)
        {
            d += depthCount[j];
            System.out.println("Depth : " +j+ " -- " + depthCount[j]);
        }
        
        System.out.println("Total nodes generated : " + d+ "\n\n");
		double speed = d/n;
		System.out.println("Speed (Nodes/sec) : " + speed);
        return;
    }       
               
           
    public Node DepthLimited(int depth)
    {   
		return RecursiveDLS(root, depth);
    }
          
    public Node RecursiveDLS(Node current, int depth)
    {
        depthCount[depth]++;  
        Vector <Node> children = new Vector<Node>();
        if (Node.isGoal(current)){
            
            return current;
        }else if (current.getDepth() == depth){
            
            return null;
        }else {
			
            Node result = null;
            children = expand(current);
            
            if (children.size() == 0){
                return null;            
			}
			
            for (Node i: children) {
				
                result = RecursiveDLS(i, depth);
                if (result != null){
                    return result;
				}
            }
           
        }
        return null;
    }
    
    public void HillClimbingSearch()
    {
        System.out.println("HillClimbing Search");
        
        initTime();
        
        Node temp;
        root.removeAllChildren();
        temp = root;
        Vector <Node> children = new Vector<Node>();
        int i = 0;
        boolean v = false;
        
        while (temp != null)
        {
            if (Node.isGoal(temp))
            {
                v = true;
                System.out.println("Goal Found");
                temp.print();
				//printPath(temp);
                break;
            }

            i++;
            children = expand(temp);
            temp = SearchLowestF(children, 2);
        }
        double n = finalTimeStamp();
        int d = countNodes();
		double speed = d/n;
        System.out.println("Nodes generated : " + d);
        System.out.println("Maximum Depth : " + i);
		System.out.println("Speed (Nodes/sec) : " + speed);
        
        if (!v) {
            System.out.println("Algorithm ended without finding a goal Node");   
        }
        
        System.out.println("\n\n");
        return;
    }
  
    private Vector <Node> expand(Node cNode)
    {
        return cNode.createChildren(root);
    }         

    private Vector <Node> expandA(Node cNode, int hfn)
    {
        return cNode.createChildrenA(root, hfn);
    }

    private Vector <Node> expandB(Node cNode, int hfn)
    {
        return cNode.createChildrenB(root);
    }
    
    private void initTime()
    {
        initTime = System.nanoTime();
        finalTime = 0;
    }

    private double finalTimeStamp()
    {
        finalTime = System.nanoTime();
        long tTime = finalTime - initTime;
        double sTime = tTime * pow(10, -9);
        System.out.println(" # of ns :" +tTime);
        System.out.println(" # of s :"+sTime);
		return sTime;
    }
  
    private Node searchGoal(Vector <Node> list)
    {
        for (int j = 0; j < list.size(); j++)
        {
            if (Node.isGoal(list.elementAt(j))){
                return (list.elementAt(j));
			}
        }
        return null;
    }
            
    private void printVector(Vector <Node> list)
	{       
		for (Node j: list){
           j.print();    
		}
	} 
    
    private void printVectorData(Vector <Node> list)
    {       
		for (Node j: list){
           j.printData();  
		}  
    } 
    
    private void printLeaves(Node r)
    {
        Node t = (Node) r.getNextNode();

        while(t != null)
        {
            if (t.isLeaf()){
                t.print();
			}
            t = (Node) t.getNextNode();
        }   

    }
    
    private int countNodes()
    {
        int i = 1;
		Node t = (Node) root.getNextNode();

        while(t != null)
        {
            i++;
            t = (Node) t.getNextNode();
        } 
        return i;
    }
    
    private Node SearchLowestF(Vector <Node> open, int hFn)
    {
        int lowestF = 9999999;
        Node lowestN = null;
        int f_n;
        
        for (Node i: open)
        {
            f_n = fValue(i, hFn);
            if (f_n < lowestF)
            {
                lowestF = f_n;
                lowestN = i;
            }
        }   
        return lowestN;
    }
    
    private int fValue(Node i, int hFn)
    {
        int f_n;
        if (hFn == 1)
            f_n = i.h1() + i.getDepth(); 
        else if (hFn == 2)
            f_n = i.h2() + i.getDepth(); 
        else
            f_n = i.h0() + i.getDepth(); 
            
         return f_n;            
    }
    
    private void printPath(Node g)
    {
        System.out.println("Solution Path");
        
        Vector <Node> list = new Vector<Node>();
        
        list.add(g);
        
        Node temp = (Node) g.getParent();

        while(temp != null)
        {
            list.add(g);
            temp = (Node) temp.getParent();
        }
        
        for (int i = list.size() - 1; i >= 0; i--)
        {
            list.elementAt(i).print();
        }
        
    }   
}
