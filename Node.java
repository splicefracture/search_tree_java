/**
 * 
 * File : Node.java
 * @author : Alec Prassinos
 * @version : 10-24-05 
 */

import java.util.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class Node extends DefaultMutableTreeNode
{
    private Puzzle node;
    private int depth;
    private int manhattan;
    private int misplaced; 

    public Node(Puzzle _p, int _d)
    {
        node = new Puzzle(_p);
        depth = _d;
        manhattan = _p.man();
        misplaced = _p.mis();
    }
    
    public int getDepth()
    {
        return depth;    
    }   
   
    public int h0()
    {
        return 0;   
    }
   
    public int h1()
    {
        return misplaced;
    }
   
    public int h2()
    {
        return manhattan;   
    }
   
    public int hg(boolean h)
    {
        if (h)
            return depth + manhattan;
        else
            return depth + misplaced;
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
    
    
    public void print()
    {
        Puzzle temp = node;
        
        System.out.println("Depth :" + depth);
        temp.print();
        System.out.println("Manhattan :" + manhattan);
        System.out.println("Misplaced :" + misplaced);
        
    }
    
    public void printData()
    {
        int h1 = misplaced + depth;
        int h2 = manhattan + depth;
        System.out.println("Depth : " + depth + " Manhattan : " + manhattan + 
                           " Misplaced : " + misplaced + " h1 : " + h1 + " h2 : " + h2 );
    }
    
    
    public boolean equalTo(Node cNode)
    {
        if (cNode.node.equalTo(this.node))
            return true;
        else
            return false;
    }
    
    public Vector <Node> createChildren(Node root)
    {
        Puzzle temp = node;//(Puzzle) node.getUserObject();
        int _d = depth;
        _d++;
        
        Puzzle N = new Puzzle(temp);
        Puzzle E = new Puzzle(temp);
        Puzzle W = new Puzzle(temp);
        Puzzle S = new Puzzle(temp);
         
        N.moveNorthS(); //north
        E.moveEastW();  //east
        W.moveWestE();  //west
        S.moveSouthN(); //south
        
        Vector <Node> children = new Vector<Node>();
        
        Node Nnode = new Node(N,_d);
        Node Enode = new Node(E,_d);
        Node Wnode = new Node(W,_d);
        Node Snode = new Node(S,_d);
        
        if (isNoRepeats(Nnode, this))
        {
            this.add(Nnode);
            children.add(Nnode);
        }
        if (isNoRepeats(Enode, this))
        {
            this.add(Enode);
            children.add(Enode);
        }
        if (isNoRepeats(Wnode, this))
        {
            this.add(Wnode);
            children.add(Wnode);
        }
        if (isNoRepeats(Snode, this))
        {            
            this.add(Snode);
            children.add(Snode);
        }
        
        return children;        
    }    
    
    public Vector <Node> createChildrenA(Node root, int hFn)
    {
        Puzzle temp = node;
        int _d = depth;
        _d++;
        
        Puzzle N = new Puzzle(temp);
        Puzzle E = new Puzzle(temp);
        Puzzle W = new Puzzle(temp);
        Puzzle S = new Puzzle(temp);
         
        N.moveNorthS(); //north
        E.moveEastW();  //east
        W.moveWestE();  //west
        S.moveSouthN(); //south
        
        Vector <Node> children = new Vector<Node>();
        
        Node Nnode = new Node(N,_d);
        Node Enode = new Node(E,_d);
        Node Wnode = new Node(W,_d);
        Node Snode = new Node(S,_d);
      
        if (fullNoRepeats(Nnode, root, hFn))
        {
            this.add(Nnode);
            children.add(Nnode);
        }
        if (fullNoRepeats(Enode, root, hFn))
        {
            this.add(Enode);
            children.add(Enode);
        }
        if (fullNoRepeats(Wnode, root, hFn))
        {
            this.add(Wnode);
            children.add(Wnode);
        }
        if (fullNoRepeats(Snode, root, hFn))
        {            
            this.add(Snode);
            children.add(Snode);
        }
        
        return children;        
    }
    
    public Vector <Node> createChildrenB(Node root)
    {
        Puzzle temp = node;
        int _d = depth;
        _d++;
        
        Puzzle N = new Puzzle(temp);
        Puzzle E = new Puzzle(temp);
        Puzzle W = new Puzzle(temp);
        Puzzle S = new Puzzle(temp);
         
        N.moveNorthS(); //north
        E.moveEastW();  //east
        W.moveWestE();  //west
        S.moveSouthN(); //south
        
        Vector <Node> children = new Vector<Node>();
        
        Node Nnode = new Node(N,_d);
        Node Enode = new Node(E,_d);
        Node Wnode = new Node(W,_d);
        Node Snode = new Node(S,_d);
      
        if (fullNoRepeats2(Nnode, root))
        {
            this.add(Nnode);
            children.add(Nnode);
        }
        if (fullNoRepeats2(Enode, root))
        {
            this.add(Enode);
            children.add(Enode);
        }
        if (fullNoRepeats2(Wnode, root))
        {
            this.add(Wnode);
            children.add(Wnode);
        }
        if (fullNoRepeats2(Snode, root))
        {            
            this.add(Snode);
            children.add(Snode);
        }
        
        return children;        
    }
    
    private boolean isNoRepeats(Node child, Node parent)
    {     
        Node temp = parent;
        while (temp != null)
        {
            if (child.equalTo(temp))
               return false;
            temp = (Node) temp.getParent();
        }
        return true;
    }
    
    private boolean fullNoRepeats(Node child, Node root, int hFn)
    {
        Node t = root;
        int g_np;
        int g_n = child.getDepth();
        while(t != null)
        {
            g_np = t.getDepth();
            if ((g_n < g_np) && (t.equalTo(child)))
            {
                t.removeFromParent();
                return true;                
            }
            else if (t.equalTo(child))
            {
                return false;                
            }
            t = (Node) t.getNextNode();           
        }   
        return true;
    }
    
    private boolean fullNoRepeats2(Node child, Node root)
    {
        Node t = root;
        int g_np;
        int g_n = child.getDepth();
        while(t != null)
        {

            if (t.equalTo(child))
            {
                return false;                
            }
            t = (Node) t.getNextNode();           
        }   
        return true;
    }
    
    
    public static boolean isGoal(Node test)
    {
        if (test == null)
            return false;
            
        Puzzle tPuzz = test.node;
        if (tPuzz.equalTo(Puzzle.makeGoalState()))
            return true;
        else
            return false;
    }
    
    
}
