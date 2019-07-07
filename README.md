DESCRIPTION:
-----------
ASTRAL is a tool for estimating an unrooted species tree given a set of unrooted gene trees.
ASTRAL is statistically consistent under the multi-species coalescent model (and thus is useful for handling incomplete lineage sorting, i.e., ILS). Here constrained version of ASTRAL has been provided where the species tree compatible with a given constraint tree is inferred from a set of unrooted gene trees. 

Email: `astral-users@googlegroups.com` for questions.



Documentations
-----------

- The rest of this README file
- **Our [tutorial](astral-tutorial.md)**.
- For practical tips on using ASTRAL, including on how to prepare input and interpret output, see [this paper](https://arxiv.org/pdf/1904.03826.pdf).
- The chapter of Siavash Mirarab's dissertation that describes ASTRAL in detail is provided [here](thesis-astral.pdf).
- A [developer guide](developer-guide.md).

INSTALLATION:
-----------
* There is no installation required to run ASTRAL.
* Download using one of two approaches:
    * You simply need to download the [zip file](https://github.com/smirarab/ASTRAL/raw/master/Astral.5.6.7.zip) and extract the contents to a folder of your choice. 
    * Alternatively, you can clone the [github repository](https://github.com/smirarab/ASTRAL/). You then run `make.sh` to build the project or simply uncompress the zip file that is included with the repository.
* ASTRAL is a java-based application, and should run in any environment (Windows, Linux, Mac, etc.) as long as java is installed. 
  Java 1.5 or later is required. We have tested ASTRAL only on Linux and MAC.

* ASTRAL can be run from any directory (e.g., `/path/to/astral/`). Then, you just need to run:

  ``` bash
  java -jar /path/to/astral/astral.5.6.7.jar
  ```

* Also, you can move `astral.5.6.7.jar` to any location you like and run it from there, but note that you need to move the `lib` directory with it as well.


EXECUTION:
-----------
ASTRAL currently has no GUI. You need to run it through the command-line. In a terminal, go the location where you have downloaded the software, and issue the following command:

```
  java -jar astral.5.6.7.jar
```

This will give you a list of options available in ASTRAL.

To run Constrained ASTRAL, constraint tree should be given using `-e` .To save the results use the `-o` option:

```
java -jar astral.5.6.7.jar -i in.tree -o out.tre -e constrainttree.tre
```
To save the logs (**recommended**) and completed input trees, run:

```
java -jar astral.5.6.7.jar -i in.tree -o out.tre -e constrainttree.tre 2>out.log > completedtrees.tree
```

###### Input: 
* The input gene trees are in the Newick format
* The input trees can have missing taxa, polytomies (unresolved branches).
*  Taxon names cannot have quotation marks in their names (sorry!). This means you also cannot have weird characters like ? in the name (underscore is fine).

###### Output: 
The output species tree in is Newick format and gives: 

* the species tree topology, 
* branch lengths in coalescent units (only for internal branches or for terminal branches if that species has multiple individuals),
* branch supports measured as [local posterior probabilities](http://mbe.oxfordjournals.org/content/early/2016/05/12/molbev.msw079.short?rss=1). 
* It can also annotate branches with other quantities, such as quartet support, as described in the [tutorial](astral-tutorial.md).
* completed trees are constraint tree completed with each gene tree in input file (same order)

The ASTRAL tree leaves the branch length of terminal branches empty. Some tools for visualization and tree editing do not like this (e.g., ape). In FigTree, if you open the tree several times, it eventually opens up (at least on our machines). In ape, if you ask it to ignore branch lengths all together, it works. In general, if you tool does not like the lack of terminal branches, you can add a dummy branch length, [as in this script](https://github.com/smirarab/global/blob/master/src/mirphyl/utils/add-bl.py). 

### Other features (local posterior, bootstrapping):
Please refer to the [tutorial](astral-tutorial.md) for all other features, including bootstrapping, branch annotation, and local posterior probability.

### Memory:
For big datasets (say more than 5000 taxa), increasing the memory available to Java can result in speedups. Note that you should give Java only as much free memory as you have available on your machine. So, for example, if you have 3GB of free memory, you can invoke ASTRAL using the following command to make all the 3GB available to Java:

```
java -Xmx3000M -jar astral.5.6.7.jar -i in.tree -e constrainttree.tre
```

Acknowledgment
-----------
ASTRAL code uses bytecode and some reverse engineered code from PhyloNet package (with permission from the authors).


Bug Reports:
-----------
contact ``astral-users@googlegroups.com``
