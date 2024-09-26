package ait.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableInfo {
    public static void runInfo(Object object) {
        Class<?> clazz = object.getClass();
        runInfo(clazz);
    }

    public static void runInfo(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        runInfo(clazz);
    }

    public static void runInfo(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Table.class)) {
            System.out.println(clazz.getName() + " not a Scheme");
            return;
        }
        Table tableAnn = clazz.getAnnotation(Table.class);
        String tableName = "".equals(tableAnn.name()) ? clazz.getSimpleName().toLowerCase() : tableAnn.name();
        String idField = null;
        List<String> uniqueIndexes = new ArrayList<>();
        List<String> nonUnigueIndexes = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
//        for (Field field : fields) {
//            // handle @Id
//            if(field.isAnnotationPresent(Id.class)) {
//                if(idField != null) {
//                    throw new RuntimeException("Id duplicated");
//                }
//                idField = field.getName();
//            }
//            // handle @Index
//            Index indexAnn = field.getAnnotation(Index.class);
//            if(indexAnn != null) {
//               if(indexAnn.unique()) {
//                   uniqueIndexes.add(field.getName());
//               } else {
//                   nonUnigueIndexes.add(field.getName());
//               }
//            }
//        }
        //            throw new RuntimeException("Id is not defined");
//        }

//==============HOMEWORK=========================
        // handle @Id
        idField = Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(Id.class))
                .map(Field::getName)
                .reduce((a, b) -> {throw new RuntimeException("Id duplicated");})
                .orElseThrow(() -> new RuntimeException("Id is not defined"));
        // handle @Index
        uniqueIndexes = Stream.of(fields)
                .filter(field -> field.isAnnotationPresent(Index.class))
                .filter(field -> field.getAnnotation(Index.class).unique())
                .map(Field::getName)
                .collect(Collectors.toList());

        nonUnigueIndexes = Stream.of(fields)
                .filter(field -> field.isAnnotationPresent(Index.class))
                .filter(field -> !field.getAnnotation(Index.class).unique())
                .map(Field::getName)
                .collect(Collectors.toList());//        if (idField == null) {

        printTableInfo(tableName, idField, uniqueIndexes, nonUnigueIndexes);
    }

    private static void printTableInfo(String tableName, String idField, List<String> uniqueIndexes, List<String> nonUnigueIndexes) {
        System.out.println("Table: " + tableName);
        System.out.println("Id: " + idField);
        System.out.println("\tUnique Indexes ");
        uniqueIndexes.forEach(System.out::println);
        System.out.println("\tNon Unique Indexes ");
        nonUnigueIndexes.forEach(System.out::println);
    }
}
