package cn.yj.readbook.utils.jFile;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yj.readbook.utils.jFile.bean.FileInfo;

/**
 * Created by yangjie on 2017/3/29.
 */

public class JFileUtil {
    public enum Type {EXCEL, WORD, PPT, AUDIO, PDF, PIC, RAR, ZIP, TXT}
    private static Uri defaultUri = Uri.parse("content://media/external/file");


    public static ArrayList<FileInfo> scanFile(Activity activity, Type... types) {
        return scanFile(activity, defaultUri, types);
    }

        public static ArrayList<FileInfo> scanFile(Activity activity, Uri uri, Type... types) {
        ArrayList<FileInfo> infoList = new ArrayList<>();
        String baseSql = " or " + MediaStore.Files.FileColumns.DATA + " like ?";


        // 根据传入的参数拼接sql
        String sql = "";
        List<String> typeList = new ArrayList<>();
        for (Type type : types) {
            switch (type) {
                case EXCEL:
                    sql += baseSql + baseSql;
                    typeList.add("%.xlsx");
                    typeList.add("%.xls");
                    break;
                case WORD:
                    sql += baseSql + baseSql;
                    typeList.add("%.doc");
                    typeList.add("%.docx");
                    break;
                case PPT:
                    sql += baseSql + baseSql;
                    typeList.add("%.ppt");
                    typeList.add("%.pptx");
                    break;
                case PDF:
                    sql += baseSql;
                    typeList.add("%.pdf");
                    break;
                case AUDIO:
                    sql += baseSql + baseSql;
                    typeList.add("%.mp3");
                    typeList.add("%.wav");
                    break;
                case PIC:
                    sql += baseSql + baseSql + baseSql;
                    typeList.add("%.jpg");
                    typeList.add("%.jpeg");
                    typeList.add("%.png");
                    break;
                case RAR:
                    sql += baseSql;
                    typeList.add("%.rar");
                    break;
                case ZIP:
                    sql += baseSql;
                    typeList.add("%.zip");
                    break;
                case TXT:
                    sql += baseSql;
                    typeList.add("%.txt");
                    break;
            }
        }
        if (sql.length() > 0) {
            sql = sql.replaceFirst("or", "");
        }

        // 开始构建ContentProvider
        String[] projection = new String[]{
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE
        };
        // 重命名后才找出来，有延迟
        if (types.length > 0) {
            Cursor cursor = activity.getContentResolver().query(
                    uri,
                    projection,
                    sql,
                    typeList.toArray(new String[typeList.size()]),
                    null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int idindex = cursor
                            .getColumnIndex(MediaStore.Files.FileColumns._ID);
                    int dataindex = cursor
                            .getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    int sizeindex = cursor
                            .getColumnIndex(MediaStore.Files.FileColumns.SIZE);
                    do {
                        String id = cursor.getString(idindex);
                        String path = cursor.getString(dataindex);
                        String size = cursor.getString(sizeindex);
                        int dot = path.lastIndexOf("/");
                        String name = path.substring(dot + 1);
                        infoList.add(new FileInfo(name, path, size));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }
        return infoList;
    }


    /**
     *
     * @param fileInfos 文件列表
     * @param types 需要过滤封装的类型
     * @return 按照传入types的顺序，返回对应的ArrayList<FileInfo>[]
     *  比如传入 JFileUtil.Type.EXCEL, JFileUtil.Type.WORD, JFileUtil.Type.PPT
     *  返回的数据：
     *  ArrayList<FileInfo>[0]的数据是excel文件
     *   ArrayList<FileInfo>[1]的数据是WORD文件
     *    ArrayList<FileInfo>[2]的数据是PPT文件
     *
     * */
    public static ArrayList<FileInfo>[] filter(ArrayList<FileInfo> fileInfos, Type... types) {
        boolean isFilterExcel = false;
        boolean isFilterWord = false;
        boolean isFilterPpt = false;
        boolean isFilterAudio = false;
        boolean isFilterPDF = false;
        boolean isFilterRar = false;
        boolean isFilterZip = false;
        boolean isFilterTxt = false;

        Map<Type, Integer> map = new HashMap<>();
        ArrayList<FileInfo>[] arrays = new ArrayList[types.length];
        for(int i = 0; i < types.length; i++) {
            arrays[i] = new ArrayList<>();
        }
        // 保存顺序
        for (int i = 0; i < types.length; i++) {
            map.put(types[i], i);
        }

        // 判断是否过滤
        for (Type t : types) {
            switch (t) {
                case EXCEL:
                    isFilterExcel = true;
                    break;
                case WORD:
                    isFilterWord = true;
                    break;
                case PPT:
                    isFilterPpt = true;
                    break;
                case PDF:
                    isFilterPDF = true;
                    break;
                case AUDIO:
                    isFilterAudio = true;
                    break;
                case RAR:
                    isFilterRar = true;
                    break;
                case ZIP:
                    isFilterZip = true;
                    break;
                case TXT:
                    isFilterTxt = true;
                    break;
            }
        }
        // 开始过滤，组装返回值
        for (FileInfo info : fileInfos) {
            int i = info.name.lastIndexOf(".");
            if (isFilterWord && info.name.substring((i+1),info.name.length()).contains("doc")) {
                arrays[map.get(Type.WORD)].add(info);
            } else if (isFilterPpt && info.name.substring((i+1),info.name.length()).contains("ppt")) {
                arrays[map.get(Type.PPT)].add(info);
            } else if (isFilterExcel && info.name.substring((i+1),info.name.length()).contains("xls")) {
                arrays[map.get(Type.EXCEL)].add(info);
            } else if (isFilterPDF && info.name.substring((i+1),info.name.length()).contains("pdf")) {
                arrays[map.get(Type.PDF)].add(info);
            } else if (isFilterAudio && info.name.substring((i+1),info.name.length()).contains("wav") || info.name.substring((i+1),info.name.length()).contains("mp3")) {
                arrays[map.get(Type.AUDIO)].add(info);
            }else if (isFilterRar && info.name.substring((i+1),info.name.length()).contains("rar")) {
                arrays[map.get(Type.RAR)].add(info);
            }else if (isFilterZip && info.name.substring((i+1),info.name.length()).contains("zip")) {
                arrays[map.get(Type.ZIP)].add(info);
            }else if (isFilterTxt && info.name.substring((i+1),info.name.length()).contains("txt")) {
                arrays[map.get(Type.TXT)].add(info);
            }
        }
        return arrays;
    }


    public static Map<String,ArrayList<FileInfo>> filter(ArrayList<FileInfo> fileInfos) {
        Map<String,ArrayList<FileInfo>> map = new HashMap<>();
        for (FileInfo info : fileInfos) {
            String parentPath = info.path.substring(0, info.path.lastIndexOf("/"));

            // 添加key
            boolean isPutToMap = false;
            for (String key : map.keySet()) {
                if (key.equals(parentPath)) {
                    isPutToMap = true;
                    break;
                }
            }
            if (!isPutToMap) {
                map.put(parentPath, new ArrayList<FileInfo>());
            }

            //
            map.get(parentPath).add(info);
        }
        return map;
    }


    }
