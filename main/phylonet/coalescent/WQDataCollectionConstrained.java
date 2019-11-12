package phylonet.coalescent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import phylonet.lca.SchieberVishkinLCA;
import phylonet.tree.model.MutableTree;
import phylonet.tree.model.TMutableNode;
import phylonet.tree.model.TNode;
import phylonet.tree.model.Tree;
import phylonet.tree.model.sti.STINode;
import phylonet.tree.model.sti.STITree;
import phylonet.tree.model.sti.STITreeCluster;
import phylonet.tree.model.sti.STITreeCluster.Vertex;
import phylonet.tree.util.Trees;
import phylonet.util.BitSet;

/**
 * Sets up the set X
 * 
 * @author smirarab
 * 
 */
public class WQDataCollectionConstrained extends WQDataCollection {
	
	@Override
	protected List<STITree> preProcessTreesBeforeAddingToX(STITree tre) {
		return TreeCompletion.treeCompletionRepeat((STITree)tre, (STITree)constraintTree.get(0),1);
	}

	@Override
	protected Collection<Tree> preProcessTreesBeforeAddingToX(Collection<Tree> trees) {
		Collection<Tree> out = new ArrayList<Tree>();
		for (Tree tree: trees)
			out.addAll(preProcessTreesBeforeAddingToX((STITree) tree));
		return out;
	}


	@Override
	protected void addFromConsensusTreesToX(Collection<Tree> allGreedies) {
		for (Tree tr: allGreedies){
			super.addBipartitionsFromSignleIndTreesToX(tr, null, GlobalMaps.taxonNameMap.getSpeciesIdMapper().getSTTaxonIdentifier(), false, true);
		}
	}

	@Override
	protected void addBipartitionsFromSignleIndTreesToX(Tree tr, Collection<Tree> baseTrees, TaxonIdentifier id, boolean polytomy, boolean saveClusteres) {
		for (STITree tre: TreeCompletion.treeCompletionRepeat((STITree)tr, (STITree)constraintTree.get(0),1))
			super.addBipartitionsFromSignleIndTreesToX(tre, baseTrees, id, polytomy, saveClusteres);
	}
	

	@Override
	protected boolean shouldDoQuadratic(int th, TNode greedyNode, int j) {
		return false; 
	}
	
	@Override
	public void addExtraBipartitionByDistance(){
		return;
	}

	private List<Tree> constraintTree;

	public WQDataCollectionConstrained(WQClusterCollection clusters, AbstractInference<Tripartition> inference) {
		super(clusters, inference);
		this.constraintTree = inference.constraintTree;
//		if (multiind)
//			for(TNode l:this.constraintTree.get(0).postTraverse()){
//				if(l.isLeaf()){
//					GlobalMaps.taxonNameMap.getSpeciesIdMapper().getTaxaForSpecies(l.getID())
//					for()
//				}
//			}
//		for (int s = 0; s< GlobalMaps.taxonNameMap.getSpeciesIdMapper().getSpeciesCount(); s++){
//    		List<Integer> stTaxa = GlobalMaps.taxonNameMap.getSpeciesIdMapper().getTaxaForSpecies(s);
//    		int tid = stTaxa.get(GlobalMaps.random.nextInt(stTaxa.size()));
//			//sampleSpecificTaxonIdentifier.taxonId(sampleNames.get(sampleNames.size()-1));
//    	}
	}
	
	
}
