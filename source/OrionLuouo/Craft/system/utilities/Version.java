package OrionLuouo.Craft.system.utilities;

public class Version {
    int[] codes;
    String speciality;

    public Version(int[] codes) {
        this.codes = codes;
    }

    public Version(int[] codes , String speciality) {
        this.codes = codes;
        this.speciality = speciality;
    }

    public Version(String version) {
        String[] codes = version.split("\\.");
        this.codes = new int[codes.length];
        int length = codes.length - 1;
        int index = 0;
        for (; index < length ; ) {
            this.codes[index] = Integer.parseInt(codes[index++]);
        }
        try {
            this.codes[index] = Integer.parseInt(codes[index]);
        } catch (NumberFormatException e) {
            char[] chars = codes[index].toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : chars) {
                if (c >= '0' && c <= '9') {
                    builder.append(c);
                }
                else {
                    break;
                }
            }
            this.codes[index] = Integer.parseInt(builder.toString());
            speciality = codes[index].substring(builder.length());
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(codes[0]);
        int index = 1;
        while (index < codes.length) {
            builder.append('.');
            builder.append(codes[index++]);
        }
        if (speciality != null) {
            builder.append('-');
            builder.append(speciality);
        }
        return builder.toString();
    }

    public boolean advancedThan(Version version) {
        for (int index = 0 ; index < codes.length ; ) {
            if (index == version.codes.length) {
                return true;
            }
            if (codes[index] > version.codes[index]) {
                return true;
            }
            if (codes[index] < version.codes[index++]) {
                return false;
            }
        }
        return false;
    }

    public boolean obsoleteThan(Version version) {
        for (int index = 0 ; index < codes.length ; ) {
            if (index == version.codes.length) {
                return false;
            }
            if (codes[index] < version.codes[index]) {
                return true;
            }
            if (codes[index] > version.codes[index++]) {
                return false;
            }
        }
        return false;
    }

    public boolean equals(Version version) {
        if (codes.length != version.codes.length) {
            return false;
        }
        if (speciality != version.speciality || (speciality != null && !speciality.equals(version.speciality))) {
            return false;
        }
        for (int index = 0 ; index < codes.length ; ) {
            if (codes[index] != version.codes[index++]) {
                return false;
            }
        }
        return true;
    }

    public String getVersionCode() {
        StringBuilder builder = new StringBuilder(codes[0]);
        int index = 1;
        while (index < codes.length) {
            builder.append('.');
            builder.append(codes[index++]);
        }
        return builder.toString();
    }

    public String getSpeciality() {
        return speciality;
    }
}
