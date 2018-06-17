package packfinder;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import java.io.IOException;
import static java.lang.System.out;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;

public class ClassFinder {

    public static void main(String[] args) {

        try {
            readAllLines(get(args[0]))
                    .stream()
                    .map(ClassItem::new)
                    .filter(item -> compare(args[1], item.getName()))
                    .sorted()
                    .forEach(out::println);
        } catch(IOException ex) {
            ex.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    public static ClassFinder create(List<String> list) {

        ClassFinder finder = new ClassFinder();
        finder.addAll(list);

        return finder;
    }

    public static<T> boolean check(List<T> list1, List<T> list2) {
        if (list1.size() != list2.size()) return false;

        Iterator<T> iter1 = list1.iterator(), iter2 = list2.iterator();

        while(iter1.hasNext())
            if (!iter1.next().equals(iter2.next())) return false;

        return true;
    }

    static class ClassItem implements Comparable<ClassItem> {
        private String path;
        private String name;

        public ClassItem(String pathToClass) {

            int pos = pathToClass.length() - 1;

            for(; pos>=0; --pos)
                if (pathToClass.charAt(pos) == '.') break;

            path = pathToClass.substring(0, pos + 1);
            name = pathToClass.substring(pos + 1);
        }

        public String getName() { return name;}

        public String getFullName() {
            StringBuilder fullname = new StringBuilder(path);
            return fullname.append(name).toString();
        }

        @Override
        public int compareTo(ClassItem other) {
            return name.compareTo(other.name);
        }

        @Override
        public String toString() { return getFullName(); }
    }

    private final List<ClassItem> storage;

    public ClassFinder() {
        storage = new LinkedList<>();
    }

    public ClassFinder(List<String> list) {
        storage = new LinkedList<>();
        addAll(list);
    }

    public void add(String one) {
        storage.add(new ClassItem(one));
    }

    public void addAll(List<String> list) {
        for(String item : list) add(item);
    }

    public List<String> findClass(String pattern) {

        List<ClassItem> list = new LinkedList<>();

        for(ClassItem oneClass : storage) {

            if (compare(pattern, oneClass.getName())) {
                list.add(oneClass);
            }
        }

        return  list.stream()
                .sorted()
                .map(x -> x.getFullName())
                .collect(Collectors.toList());
    }

    public static boolean compare(String pattern, String word) {

        if (pattern.equals(pattern.toLowerCase()))
            word = word.toLowerCase();

        boolean isSpcEnd = false;

        if (pattern.endsWith(" ")) {
            isSpcEnd = true;
            pattern = pattern.trim();
        }

        int pos1 = 0, pos2 = 0;
        int len1 = pattern.length(), len2 = word.length();

        int countOfMatches = 0;

        char symb1, symb2;

        while(pos1 < len1 && pos2 < len2) {

            symb1 = pattern.charAt(pos1);
            symb2 = word.charAt(pos2);

            if (symb1 == symb2 || symb1 == '*') {

                ++countOfMatches;
                ++pos1;
                ++pos2;

            } else {
                ++pos2;
            }
        }

        if (countOfMatches == len1) {
            if (isSpcEnd && pos2 < len2) return false;
            return true;
        }

        return false;
    }
}
