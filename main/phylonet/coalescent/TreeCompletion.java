package phylonet.coalescent;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import phylonet.lca.SchieberVishkinLCA;
import phylonet.tree.io.NewickReader;
import phylonet.tree.io.ParseException;
import phylonet.tree.model.TMutableNode;
import phylonet.tree.model.TNode;
import phylonet.tree.model.Tree;
import phylonet.tree.model.sti.STINode;
import phylonet.tree.model.sti.STITree;
import phylonet.tree.util.Trees;

public class TreeCompletion {
	
	public static void main(String[] args) throws IOException, ParseException{
		//String tr1 = "((1,(2,3)a)b,(4,(5,(6,7)d)c));";
//		String tr1 = "((1,2)a,(3,(4,(6,(5,7))d)c));";
		String tr1 = "((7,2)a,(3,(4,(6,(5,9))d)c),(0,11),99);";
		String tr2 = "(3,(11,(5,9)a)b)c;";
//		String tr2 = "(9,7,3,4);";
		NewickReader nr = new NewickReader(new StringReader(tr1));
		STITree<Double> gt = new STITree<Double>(true);
		nr.readTree(gt);
		
		NewickReader nr2 = new NewickReader(new StringReader(tr2));
		STITree<Double> st = new STITree<Double>(true);
		nr2.readTree(st);
		
//		for (TNode gtNode : gt.postTraverse()) {
//			if(gtNode.getName().equals("c")){
//				System.err.println(gtNode.getID()+"**");
//				System.err.println(((STINode<Double>) gtNode).toNewick());
//			TMutableNode newNode = new NewickReader(new StringReader(((STINode<Double>) gtNode).toNewick())).readTree().getRoot();
//			st = addToTree(st,(STINode) st.getNode(0),(STINode)gtNode);
//
//			}
//		}
		System.err.println();
		System.err.println(st.toNewick());
		st = treeCompletion(gt,st);
		System.err.println("out: "+st.toNewick());

	}
	
	static STITree addToTree(STITree tree , STINode adoptingNode, STINode toMoveNode){
		
		STINode newNode = null;
		try {
			newNode = new STITree(((STINode<Double>) toMoveNode).toNewick()).getRoot();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!adoptingNode.isRoot()){
			STINode newinternalnode = adoptingNode.getParent().createChild();
			newinternalnode.adoptChild(adoptingNode);
			newinternalnode.adoptChild(newNode);
			return tree;
		}
		else{

			STINode newinternalnode = adoptingNode.createChild();
			TNode child = (TNode) adoptingNode.getChildren().iterator().next();
			newinternalnode.adoptChild((TMutableNode) child );
			newinternalnode.adoptChild(newNode);
			return tree;
			
		}
		
	}
	
static STITree addToTreePolytomy(STITree tree , STINode adoptingNode, ArrayList<STINode> redChildren){
		
		ArrayList<STINode> newnodes = new ArrayList<STINode>();
		try {
			for(STINode n : redChildren)
				newnodes.add(new STITree(((STINode<Double>)n).toNewick()).getRoot());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(!adoptingNode.isRoot()){
			
			STINode newinternalnode = adoptingNode.getParent().createChild();
			newinternalnode.adoptChild(adoptingNode);
			for(STINode n : newnodes)
				newinternalnode.adoptChild(n);
			return tree;
		}
		else{
			
//			System.err.println(tree.toNewick());
//			System.err.println(tree.getRoot().getID());
//			Tree tt = tree;
//			for(TNode t : tt.postTraverse())
//				System.err.println(t.getID()+" "+t.getName());
//			tree.rerootTreeAtEdge(tree.getNode(2));
//			System.err.println(tree.toNewick());
//			tt = tree;
//			for(TNode t : tt.postTraverse())
//				System.err.println(t.getID()+" "+t.getName());
//			System.out.println(tree.getRoot().getID());
////			System.err.println(adoptingNode.getID());
//			STINode newinternalnode = adoptingNode.getParent().createChild();
//			
			STINode newinternalnode = adoptingNode.createChild();
			TNode child = (TNode) adoptingNode.getChildren().iterator().next();
			newinternalnode.adoptChild((TMutableNode) child );
			for(STINode n : newnodes)
				newinternalnode.adoptChild(n);
			return tree;
			
//			STITree s = new STITree();
//			s.getRoot().setName("hey");
//			for(STINode n : newnodes)
//				s.getRoot().adoptChild(n);
//			s.getRoot().adoptChild(tree.getRoot());
//			return s;
		}
		
	}
	
	static void nodeColoring(STITree stTree, Tree gtTree) {
		for (TNode gtNode : gtTree.postTraverse()) {
			if(gtNode.isLeaf()){
				String name = gtNode.getName();
				if (Arrays.asList(stTree.getLeaves()).contains(name)){////////************
					
					((STINode) gtNode).setData("G");
				}
				else{
					((STINode) gtNode).setData("R");
				}
//				System.err.println("node label: "+gtNode.getName()+" id:"+ gtNode.getID()+" "+ ((STINode) gtNode).getData());
			}
			else{
				boolean allred = true;
				boolean allgreen = true;
				boolean hasred = false;
				for (TNode child: gtNode.getChildren()){
					
					String data = (String) ((STINode) child).getData();
					if(data.equals("B") || data.equals("BM") ){
						allred = false;
						allgreen = false;
					}
					else if(data.equals("G")){
						allred = false;
					}
					else if(data.equals("R")){
						allgreen = false;
						hasred = true;
					}
				}
					if(allred) 
						((STINode) gtNode).setData("R");
					else if(allgreen)
						((STINode) gtNode).setData("G");
					else if(hasred)
						((STINode) gtNode).setData("BM");
					else
						((STINode) gtNode).setData("B");
//					System.err.println("node name: "+gtNode.getName()+" id: "+ gtNode.getID()+" "+ ((STINode) gtNode).getData());
			}
		}
		
	}
	
	static ArrayList<STITree> treeCompletionRepeat(STITree gTree, STITree sTree){
		int REPEATS = 1;
		SchieberVishkinLCA lcaLookup = new SchieberVishkinLCA(sTree);
		String[] gtLeaves = gTree.getLeaves();
		String[] stLeaves = sTree.getLeaves();
		List<String> common = new ArrayList<String>(Arrays.asList(gtLeaves));
		common.retainAll(Arrays.asList(stLeaves));
		ArrayList<STITree> results = new ArrayList<STITree>();
		ArrayList<STITree> temps = new ArrayList<STITree>();
		for(String s:common){
			temps.add(new STITree(sTree));
		}

		for(int i=0 ; i< REPEATS && i < common.size(); i++){
			String root = common.get(i);
			System.err.println("Both trees are rooted at "+root);
			temps.get(i).rerootTreeAtNode(temps.get(i).getNode(root));
			gTree.rerootTreeAtNode(gTree.getNode(root));
			results.add(treeCompletion(gTree, temps.get(i)));
		}
		return results;
		
		
	}
	static STITree treeCompletion(STITree gTree, STITree sTree){
//		String[] gtLeaves = gTree.getLeaves();
//		String[] stLeaves = sTree.getLeaves();
//		List<String> common = new ArrayList<String>(Arrays.asList(gtLeaves));
//		common.retainAll(Arrays.asList(stLeaves));
//		sTree.rerootTreeAtNode(sTree.getNode(common.get(0)));
//		gTree.rerootTreeAtNode(gTree.getNode(common.get(0)));
		nodeColoring(sTree, gTree);
		HashMap<Integer, Integer> LCAMap = createLCAMap(sTree, gTree);
	  
	        // Create an empty stack and push root to it ,preorder on non-root nodes
	        Stack<TNode> nodeStack = new Stack<TNode>(); 
	        nodeStack.push(gTree.getRoot());
//	        TNode root = gTree.getRoot();
//	        for(TNode t: root.getChildren())
//        		nodeStack.push(t);
	        while (nodeStack.empty() == false) { 
	              
	            // Pop the top item from stack and print it 
	            TNode mynode = nodeStack.peek(); 
//	            System.err.print(((STINode<Double>) mynode).getData() + " "); 
	            if(((STINode) mynode).getData().equals("BM")){
	            	int childrenCount = mynode.getChildCount();
	            	int redchild = 0;
	            	ArrayList<STINode> redChildren = new ArrayList<STINode>();
	            	for(TNode child:mynode.getChildren())
	            		if(((STINode) child).getData().equals("R")){
	            			redChildren.add((STINode) child);
	            			redchild += 1;
	            	}
	            	int id = LCAMap.get(mynode.getID());
	            	System.err.println("id: "+id);
	    	        STINode snode = sTree.getNode(id);
	            	if(redchild ==1 && mynode.getChildCount() == 2){	            	
		    	        sTree = addToTree(sTree, snode, (STINode) redChildren.get(0));
		    			
	            	}
	            	else{
	            		sTree = addToTreePolytomy(sTree, snode,  redChildren);
	            	}
//	            	System.err.println(sTree.toNewick());
	            	
				}
	            nodeStack.pop(); 
	            
	            // Push children of current node to the stack
	            //since at first we added children of the root, we skip it here

	            for(TNode t: mynode.getChildren())
	            		nodeStack.push(t); 

	    }  
	        return sTree;
	}
	
	static HashMap<Integer,Integer> createLCAMap(Tree stTree, Tree gtTree) {
//oneTreeCompletion
//		for (Tree stTree : stTrees) {
		HashMap<Integer, Integer> LCAMap = new HashMap<Integer,Integer>();
//		System.err.println(stTree.getRoot().getID());
			SchieberVishkinLCA lcaLookup = new SchieberVishkinLCA(stTree);
//			for (Tree gtTree : gtTrees) {
				Stack<TNode> stack = new Stack<TNode>();
				for (TNode gtNode : gtTree.postTraverse()) {
					if (gtNode.isLeaf()) {
						if(((STINode) gtNode).getData().equals("G")){
							TNode t = stTree.getNode(gtNode.getName());
							stack.push(t);
							LCAMap.put(gtNode.getID(), t.getID());
						}
						if(((STINode) gtNode).getData().equals("R")){
							stack.push(null);
						}
					} else {
						if(gtNode.getChildCount()==2){
							TNode rightLCA = stack.pop();
							TNode leftLCA = stack.pop();
							// If gene trees are incomplete, we can have this case
							if (rightLCA == null && leftLCA == null) {
								stack.push(null);
								continue;
							}
							else if (rightLCA == null || leftLCA == null) {
								if(rightLCA != null){
									stack.push(rightLCA);
									LCAMap.put(gtNode.getID(), rightLCA.getID());
								}
								if(leftLCA != null){
									stack.push(leftLCA);
									LCAMap.put(gtNode.getID(), leftLCA.getID());
								}
	
								continue;
							}
							
							TNode lca = lcaLookup.getLCA(leftLCA, rightLCA);
							stack.push(lca);
							LCAMap.put(gtNode.getID(), lca.getID());
						}
						else{
							TNode[] children = new TNode[gtNode.getChildCount()];
							boolean allnull = true;
							for(int i =0;i < gtNode.getChildCount();i++){
								children[i] = stack.pop();
								if(children[i] != null)
									allnull = false;
							}
							if (allnull) {
								stack.push(null);
								continue;
							}
							else {
								Set<TNode> notnulls = new HashSet<TNode>();
								for(TNode child: children){
									if(child != null)
										notnulls.add(child);
								}
								TNode lca = lcaLookup.getLCA(notnulls);								
								stack.push(lca);
								LCAMap.put(gtNode.getID(), lca.getID());
								continue;
							}
							
						}
						if(((STINode) gtNode).getData().equals("B")){
							
						}
						
						
//						if (lca != leftLCA && lca != rightLCA) {
//
//						}
					}
				}
				System.err.println(LCAMap);
				return LCAMap;
			}
	
	}


	

