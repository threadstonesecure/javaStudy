package dlt.study.fulltext.lucene;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by denglt on 2017/4/12.
 */
public class ScanFile {
    public static void main(String[] args) throws IOException {
        Path startingDir = Paths.get("/Users/denglt/mywork/code/hs-hdp/");
        //System.out.println(startingDir.toUri().getPath());
        //遍历目录
        Files.walkFileTree(startingDir, new FindJavaVisitor());
    }

    private static class FindJavaVisitor extends SimpleFileVisitor<Path> {


        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            System.out.println("go in : " + dir.toUri().getPath());

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

            if (file.toString().endsWith(".java")) {
                System.out.println(file.toUri().getPath());
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            System.out.println("go out : " + dir.toUri().getPath());

            return FileVisitResult.CONTINUE;
        }
    }
}
