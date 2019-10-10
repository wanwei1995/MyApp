package com.example.myapp.util;

import java.util.List;

public class ListUtil {
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    public static <T> void megareList(List<T> list, ListMegareOpr<T> listMegareOpr) {
        for (int i = 0; i < list.size(); i++) {
            T tMain = list.get(i);
            for (int j = 1; j < list.size(); j++) {
                T tTemp = list.get(j);
                if (i == j)
                    continue;

                if (listMegareOpr.isNeedMegare(tMain, tTemp)) {
                    listMegareOpr.megareOpr(tMain, tTemp);
                    list.remove(tTemp);
                    j--;
                }
            }
        }
    }

    public static <T> boolean isIn(List<T> tList, T t) {
        for (T temT : tList) {
            if (temT.equals(t)) {
                return true;
            }
        }
        return false;
    }

    public static interface ListMegareOpr<T> {
        public boolean isNeedMegare(T t1, T t2);

        public void megareOpr(T t1, T t2);
    }

}
