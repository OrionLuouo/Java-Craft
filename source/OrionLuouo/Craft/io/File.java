package OrionLuouo.Craft.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class File extends java.io.File {
    public File(String pathname) {
        super(pathname);
    }

    public File(java.io.File file) {
        this(file.getAbsolutePath());
    }

    private void delete(java.io.File file) {
        if (file.isDirectory()) {
            java.io.File[] files = file.listFiles();
            if (files != null)
                for (java.io.File f : files)
                    delete(f);
        }
        file.delete();
    }

    public boolean createNewFile() {
        java.io.File file = new java.io.File(getAbsolutePath()), p = file.getParentFile();
        if (!p.exists())
            p.mkdirs();
        try {
            file.createNewFile();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }

    public boolean delete() {
        if (isDirectory()) {
            java.io.File[] files = listFiles();
            if (files != null)
                for (java.io.File f : files)
                    delete(f);
        }
        super.delete();
        return true;
    }

    public boolean copyTo(java.io.File dst) {
        return copyTo(dst, 1048576 * 64);
    }

    /**
     * 该对象与dst对象类型不匹配时，不进行操作
     * 该对象指向的路径不存在时，不进行操作
     * dst对象为空时，不进行操作
     * dst对象不存在时，自动创建
     * 该对象与dst对象名称可不同，但内容将完全相同（除去复制文件夹时dst本身含有的不存在于src的文件）
     * 复制文件夹时，如果目标路径下已存在文件并与上述规则发生规则，已执行的操作不会撤销，并返回false
     * 不违反规则情况下，对目标路径下的已有文件会执行覆写，并继续执行复制
     *
     * @param maxReadByteCount 每次最大读取字节数。
     */
    public boolean copyTo(java.io.File dst, int maxReadByteCount) {
        if (dst == null || !exists() || ((isFile() ^ dst.isFile()) && dst.exists()))
            return false;
        if (!dst.exists()) {
            if (isFile())
                try {
                    dst.createNewFile();
                } catch (IOException e) {
                }
            else
                dst.mkdir();
        }
        return copyTo(this, dst, maxReadByteCount);
    }

    private boolean copyTo(java.io.File src, java.io.File dst, int maxReadByteCount) {
        if (src.isFile()) {
            try (FileInputStream input = new FileInputStream(src); FileOutputStream output = new FileOutputStream(dst, false)) {
                long length = src.length();
                if (length >= maxReadByteCount) {
                    byte[] bytes = new byte[maxReadByteCount];
                    do {
                        input.read(bytes);
                        output.write(bytes);
                    } while ((length -= maxReadByteCount) >= maxReadByteCount);
                }
                if (length != 0) {
                    byte[] bytes = new byte[(int) length];
                    input.read(bytes);
                    output.write(bytes);
                }
                output.flush();
            } catch (IOException ignored) {
            }
        } else {
            if (dst.isFile())
                return false;
            String path = dst.getPath();
            java.io.File[] files = src.listFiles();
            if (files == null)
                return true;
            for (java.io.File file : files) {
                File f = new File(path + '/' + file.getName());
                if (!f.exists()) {
                    if (file.isFile())
                        f.createNewFile();
                    else f.mkdir();
                }
                if (!copyTo(file, f, maxReadByteCount))
                    return false;
            }
        }
        return true;
    }

    public File[] listFiles() {
        java.io.File[] files = super.listFiles();
        File[] files1 = new File[files.length];
        for (int i = 0; i < files1.length; i++)
            files1[i] = new File(files[i]);
        return files1;
    }
}
