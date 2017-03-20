# Data-Structure-and-Algos
Code written by me for some Data Structure and Algorithms issues.
I try to write the explanation and readme file in details and treat them as my INFOCOM paper. 
Everyone is welcomed to refer to my code.

1. LRUCache <br />
A simple Least Recently Used(LRU) cache data structure is defined and implemented. <br /><br />
get(key): get the value of the key if the key exists in the cache otherwise return Integer.MIN_VALUE suppose the value in the cahce are all greater than Integer.MIN_VALUE; <br /> <br />
set(key, value): set the exisiting OR insert the new value to the cache when the cache hasn't reached its capacity. If the cache reached its capacity, the least recently used key should be erased first. <br /> <br /> <br /> <br />
2. Dijstra's algorithm<br />
A solution for single-source shortest path approached by Dijkstra's algorithm is described, explained and implemented. Its running time is analyzed here.<br /> <br />
Assumption: the graph is a directed graph with non-negative edge weight. The number of vertices are named from 0 to n-1, in other words, there are n vertices in the graph. n-1 is the maximum number among all vertices' ids. For example, if there are three nodes connected in the graph, say 0,2,4, and 1 and 3 are not in the graph, n is actually 5 since 4 is the maximum number among all vertices.<br /> <br />
The approach: the code is written in java and is self explanatory. It is well-commented for every reader to understand it. The algorithm is implemented with a binary min-heap. The relationship between the vertices and the corresponding heap elements are maintained via two integer arrays, pos2objId and objId2pos. The former array holds the relationship between the array index which is used to realize the heap and the id of the vertex. The latter array reflects the oppositive mirrorring. <br /> <br />
Time Complexity Analysis: Each extract-min operation takes time O(lgV) and there are V such operations as each vertex will be popped from the heap once and only once. Each decreaseKey and insert operation takes time O(lgV), and there are at most E such operations since each edge will be visited once. The total running time is therefore O((V+E)lgV). <br /> <br />
There is a straightforward way of implementation which is to use array instead of binary minheap for the priority queue. In that case, the time complexity is dominated by the extract-min operation since each one takes O(V) time, and the total running time is O(VE).<br /> <br />
If the graph is sparse enough, my Dijsktra's algo with binary minHeap will beat the O(VE) running time since O(ElgV) is much faster than that if V is a huge number.<br /> <br />
Test cases: the format is that in each line, the first number is the id of the vertex, followed by an array of its neigbor and the edge weight, the neigbor id and edge weight is seperated by comma. Each component in the row is seperated by a tab. <br /> <br />
Result and Conclusion: small test case is easy to be judged, while results of larger test cases will be given here. Suppose the source is vertex 1, its shortest distance to vertex 5 is 2525, 3540 to vertex 29 and 3068 to vertex 197.

