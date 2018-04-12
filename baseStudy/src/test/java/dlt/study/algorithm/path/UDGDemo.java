package dlt.study.algorithm.path;

import org.junit.Test;

public class UDGDemo {

    @Test
    public void matrixUDG() {
        char[] vexs = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        int INF = MatrixUDG.INF;
        int matrix[][] = {
                        /*A*//*B*//*C*//*D*//*E*//*F*//*G*/
                /*A*/ {   0,  12, INF, INF, INF,  16,  14},
                /*B*/ {  12,   0,  10, INF, INF,   7, INF},
                /*C*/ { INF,  10,   0,   3,   5,   6, INF},
                /*D*/ { INF, INF,   3,   0,   4, INF, INF},
                /*E*/ { INF, INF,   5,   4,   0,   2,   8},
                /*F*/ {  16,   7,   6, INF,   2,   0,   9},
                /*G*/ {  14, INF, INF, INF,   8,   9,   0}};

        MatrixUDG pG;

        pG = new MatrixUDG(vexs, matrix);

        pG.print();   // 打印图
        pG.DFS();     // 深度优先遍历
        pG.BFS();     // 广度优先遍历
        pG.prim(0);   // prim算法生成最小生成树
        pG.kruskal(); // Kruskal算法生成最小生成树

        int[] prev = new int[vexs.length];
        int[] dist = new int[vexs.length];
        // dijkstra算法获取"第4个顶点"到其它各个顶点的最短距离
        pG.dijkstra(3, prev, dist);
        System.out.println("prev");
        for (int i : prev) {
            System.out.print(i+","); // i = 0 说明直接连接
        }
        System.out.println();
        System.out.print("dist");
        for (int i : dist) {
            System.out.print(i+",");
        }
        System.out.println();

        int[][] path = new int[vexs.length][vexs.length];
        int[][] floy = new int[vexs.length][vexs.length];
        // floyd算法获取各个顶点之间的最短距离
        pG.floyd(path, floy);
    }

    @Test
    public void listUDG(){
        char[] vexs = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        ListUDG.EData[] edges = {
                // 起点 终点 权
                new ListUDG.EData('A', 'B', 12),
                new ListUDG.EData('A', 'F', 16),
                new ListUDG.EData('A', 'G', 14),
                new ListUDG.EData('B', 'C', 10),
                new ListUDG.EData('B', 'F',  7),
                new ListUDG.EData('C', 'D',  3),
                new ListUDG.EData('C', 'E',  5),
                new ListUDG.EData('C', 'F',  6),
                new ListUDG.EData('D', 'E',  4),
                new ListUDG.EData('E', 'F',  2),
                new ListUDG.EData('E', 'G',  8),
                new ListUDG.EData('F', 'G',  9),
        };
        ListUDG pG;

        // 自定义"图"(输入矩阵队列)
        //pG = new ListUDG();
        // 采用已有的"图"
        pG = new ListUDG(vexs, edges);

        //pG.print();   // 打印图
        //pG.DFS();     // 深度优先遍历
        //pG.BFS();     // 广度优先遍历
        //pG.prim(0);   // prim算法生成最小生成树
        //pG.kruskal(); // Kruskal算法生成最小生成树

        int[] prev = new int[vexs.length];
        int[] dist = new int[vexs.length];
        // dijkstra算法获取"第4个顶点"到其它各个顶点的最短距离
        //pG.dijkstra(3, prev, dist);

        int[][] path = new int[vexs.length][vexs.length];
        int[][] floy = new int[vexs.length][vexs.length];
        // floyd算法获取各个顶点之间的最短距离
        pG.floyd(path, floy);
    }
}
